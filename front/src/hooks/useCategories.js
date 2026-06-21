import { useEffect, useState } from "react";
import { API_BASE_URL } from "../constants/admin";
import { extractApiData, extractErrorMessage } from "../utils/http";

const DEFAULT_PAGE_SIZE = 10;

/**
 * 관리자 카테고리 목록, 검색, 페이징, 인라인 편집 상태를 함께 관리합니다.
 */
export function useCategories({ enabled }) {
  const [categories, setCategories] = useState([]);
  const [categoriesLoading, setCategoriesLoading] = useState(false);
  const [categorySubmitting, setCategorySubmitting] = useState(false);
  const [categoryError, setCategoryError] = useState("");
  const [categoryEditor, setCategoryEditor] = useState(null);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [searchInput, setSearchInput] = useState("");
  const [page, setPage] = useState(0);
  const [size] = useState(DEFAULT_PAGE_SIZE);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  /**
   * 서버에서 카테고리 목록을 검색 조건과 페이지 정보로 다시 조회합니다.
   */
  async function fetchCategories({ nextPage = page, nextKeyword = searchKeyword } = {}) {
    setCategoriesLoading(true);
    setCategoryError("");

    const params = new URLSearchParams({
      keyword: nextKeyword,
      page: String(nextPage),
      size: String(size)
    });

    try {
      const response = await fetch(`${API_BASE_URL}/admin/categories?${params.toString()}`, {
        credentials: "include"
      });

      if (!response.ok) {
        throw new Error("카테고리 목록을 불러오지 못했습니다.");
      }

      const data = await extractApiData(response);
      setCategories(data.content ?? []);
      setPage(data.page ?? 0);
      setTotalPages(data.totalPages ?? 0);
      setTotalElements(data.totalElements ?? 0);
      setHasNext(Boolean(data.hasNext));
      setHasPrevious(Boolean(data.hasPrevious));
      setSearchKeyword(data.keyword ?? "");
      setSearchInput(data.keyword ?? "");
    } catch (error) {
      console.error(error);
      setCategoryError("카테고리 목록을 불러오지 못했습니다.");
    } finally {
      setCategoriesLoading(false);
    }
  }

  useEffect(() => {
    if (!enabled) {
      setCategoryEditor(null);
      setCategoryError("");
      return;
    }

    fetchCategories({ nextPage: 0, nextKeyword: "" });
  }, [enabled]);

  /**
   * 검색 입력 값을 갱신합니다.
   */
  function updateSearchInput(event) {
    setSearchInput(event.target.value);
  }

  /**
   * 현재 입력값 기준으로 첫 페이지부터 다시 조회합니다.
   */
  async function submitSearch(event) {
    event.preventDefault();
    await fetchCategories({
      nextPage: 0,
      nextKeyword: searchInput.trim()
    });
  }

  /**
   * 특정 페이지로 이동합니다.
   */
  async function goToPage(nextPage) {
    if (nextPage < 0 || nextPage >= totalPages || nextPage === page) {
      return;
    }

    await fetchCategories({
      nextPage,
      nextKeyword: searchKeyword
    });
  }

  /**
   * 새 카테고리를 추가할 수 있도록 빈 편집 상태를 엽니다.
   */
  function startCreateCategory() {
    setCategoryEditor({
      id: null,
      name: "",
      memo: "",
      isNew: true
    });
    setCategoryError("");
  }

  /**
   * 선택한 카테고리를 인라인 수정 모드로 전환합니다.
   */
  function startCategoryEdit(category) {
    setCategoryEditor({
      id: category.id,
      name: category.name,
      memo: category.memo ?? "",
      isNew: false
    });
    setCategoryError("");
  }

  /**
   * 현재 편집 상태를 종료합니다.
   */
  function cancelCategoryEdit() {
    setCategoryEditor(null);
    setCategoryError("");
  }

  /**
   * 편집 중인 입력 값을 갱신합니다.
   */
  function updateCategoryEditor(event) {
    const { name, value } = event.target;
    setCategoryEditor((previous) => ({
      ...previous,
      [name]: value
    }));
  }

  /**
   * 한글 조합 입력 중 Enter 기본 동작만 막고 실제 저장은 keyup에서 처리합니다.
   */
  function handleCategoryEditorKeyDown(event) {
    if (event.nativeEvent.isComposing || event.key !== "Enter" || categorySubmitting) {
      return;
    }

    event.preventDefault();
  }

  /**
   * Enter 입력이 끝나면 현재 편집 중인 카테고리를 저장합니다.
   */
  function handleCategoryEditorKeyUp(event) {
    if (event.nativeEvent.isComposing || event.key !== "Enter" || categorySubmitting) {
      return;
    }

    event.preventDefault();
    saveCategory();
  }

  /**
   * 현재 편집 상태를 생성 또는 수정 요청으로 저장합니다.
   */
  async function saveCategory() {
    if (!categoryEditor) {
      return;
    }

    const trimmedName = categoryEditor.name.trim();
    const trimmedMemo = categoryEditor.memo.trim();

    if (!trimmedName) {
      setCategoryError("카테고리명은 비워둘 수 없습니다.");
      return;
    }

    setCategorySubmitting(true);
    setCategoryError("");

    const isEditMode = !categoryEditor.isNew && Boolean(categoryEditor.id);
    const requestUrl = isEditMode
      ? `${API_BASE_URL}/admin/categories/${categoryEditor.id}`
      : `${API_BASE_URL}/admin/categories`;
    const requestMethod = isEditMode ? "PUT" : "POST";

    try {
      const response = await fetch(requestUrl, {
        method: requestMethod,
        credentials: "include",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          name: trimmedName,
          memo: trimmedMemo
        })
      });

      if (!response.ok) {
        const message = await extractErrorMessage(response, "카테고리를 저장하지 못했습니다.");
        throw new Error(message);
      }

      await fetchCategories({ nextPage: page, nextKeyword: searchKeyword });
      setCategoryEditor(null);
    } catch (error) {
      console.error(error);
      setCategoryError(error.message || "카테고리를 저장하지 못했습니다.");
    } finally {
      setCategorySubmitting(false);
    }
  }

  /**
   * 선택한 카테고리를 삭제하고 현재 페이지를 다시 조회합니다.
   */
  async function deleteCategory(categoryId) {
    const shouldDelete = window.confirm("이 카테고리를 삭제할까요?");
    if (!shouldDelete) {
      return;
    }

    setCategoryError("");

    try {
      const response = await fetch(`${API_BASE_URL}/admin/categories/${categoryId}`, {
        method: "DELETE",
        credentials: "include"
      });

      if (!response.ok) {
        const message = await extractErrorMessage(response, "카테고리를 삭제하지 못했습니다.");
        throw new Error(message);
      }

      const fallbackPage = categories.length === 1 && page > 0 ? page - 1 : page;
      await fetchCategories({
        nextPage: fallbackPage,
        nextKeyword: searchKeyword
      });

      if (categoryEditor?.id === categoryId) {
        setCategoryEditor(null);
      }
    } catch (error) {
      console.error(error);
      setCategoryError(error.message || "카테고리를 삭제하지 못했습니다.");
    }
  }

  return {
    categories,
    categoriesLoading,
    categorySubmitting,
    categoryError,
    categoryEditor,
    searchInput,
    page,
    totalPages,
    totalElements,
    hasNext,
    hasPrevious,
    updateSearchInput,
    submitSearch,
    goToPage,
    startCreateCategory,
    startCategoryEdit,
    cancelCategoryEdit,
    updateCategoryEditor,
    handleCategoryEditorKeyDown,
    handleCategoryEditorKeyUp,
    saveCategory,
    deleteCategory
  };
}
