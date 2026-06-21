import { useEffect, useState } from "react";
import { API_BASE_URL } from "../constants/admin";
import { extractApiData, extractErrorMessage } from "../utils/http";

const DEFAULT_PAGE_SIZE = 10;

export function useUsers({ enabled }) {
  const [users, setUsers] = useState([]);
  const [usersLoading, setUsersLoading] = useState(false);
  const [userSubmitting, setUserSubmitting] = useState(false);
  const [userError, setUserError] = useState("");
  const [userEditor, setUserEditor] = useState(null);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [searchInput, setSearchInput] = useState("");
  const [roleFilter, setRoleFilter] = useState("");
  const [page, setPage] = useState(0);
  const [size] = useState(DEFAULT_PAGE_SIZE);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  async function fetchUsers({ nextPage = page, nextKeyword = searchKeyword, nextRole = roleFilter } = {}) {
    setUsersLoading(true);
    setUserError("");

    const params = new URLSearchParams({
      keyword: nextKeyword,
      page: String(nextPage),
      size: String(size)
    });

    if (nextRole) {
      params.set("role", nextRole);
    }

    try {
      const response = await fetch(`${API_BASE_URL}/admin/users?${params.toString()}`, {
        credentials: "include"
      });

      if (!response.ok) {
        throw new Error("유저 목록을 불러오지 못했습니다.");
      }

      const data = await extractApiData(response);
      setUsers(data.content ?? []);
      setPage(data.page ?? 0);
      setTotalPages(data.totalPages ?? 0);
      setTotalElements(data.totalElements ?? 0);
      setHasNext(Boolean(data.hasNext));
      setHasPrevious(Boolean(data.hasPrevious));
      setSearchKeyword(data.keyword ?? "");
      setSearchInput(data.keyword ?? "");
      setRoleFilter(data.role ?? "");
    } catch (error) {
      console.error(error);
      setUserError("유저 목록을 불러오지 못했습니다.");
    } finally {
      setUsersLoading(false);
    }
  }

  useEffect(() => {
    if (!enabled) {
      return;
    }

    fetchUsers({ nextPage: 0, nextKeyword: "", nextRole: "" });
  }, [enabled]);

  function updateSearchInput(event) {
    setSearchInput(event.target.value);
  }

  function updateRoleFilter(event) {
    setRoleFilter(event.target.value);
  }

  async function submitSearch(event) {
    event.preventDefault();
    await fetchUsers({
      nextPage: 0,
      nextKeyword: searchInput.trim(),
      nextRole: roleFilter
    });
  }

  async function goToPage(nextPage) {
    if (nextPage < 0 || nextPage >= totalPages || nextPage === page) {
      return;
    }

    await fetchUsers({
      nextPage,
      nextKeyword: searchKeyword,
      nextRole: roleFilter
    });
  }

  function startUserEdit(user) {
    setUserEditor({
      id: user.id,
      role: user.role
    });
    setUserError("");
  }

  function cancelUserEdit() {
    setUserEditor(null);
    setUserError("");
  }

  function updateUserEditor(event) {
    const { value } = event.target;
    setUserEditor((previous) => ({
      ...previous,
      role: value
    }));
  }

  async function saveUserRole() {
    if (!userEditor?.id || !userEditor.role) {
      return;
    }

    setUserSubmitting(true);
    setUserError("");

    try {
      const response = await fetch(`${API_BASE_URL}/admin/users/${userEditor.id}/role`, {
        method: "PUT",
        credentials: "include",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          role: userEditor.role
        })
      });

      if (!response.ok) {
        const message = await extractErrorMessage(response, "유저 권한을 변경하지 못했습니다.");
        throw new Error(message);
      }

      await fetchUsers({
        nextPage: page,
        nextKeyword: searchKeyword,
        nextRole: roleFilter
      });
      setUserEditor(null);
    } catch (error) {
      console.error(error);
      setUserError(error.message || "유저 권한을 변경하지 못했습니다.");
    } finally {
      setUserSubmitting(false);
    }
  }

  return {
    users,
    usersLoading,
    userSubmitting,
    userError,
    userEditor,
    searchInput,
    roleFilter,
    page,
    totalPages,
    totalElements,
    hasNext,
    hasPrevious,
    updateSearchInput,
    updateRoleFilter,
    submitSearch,
    goToPage,
    startUserEdit,
    cancelUserEdit,
    updateUserEditor,
    saveUserRole
  };
}
