import { useEffect, useMemo, useState } from "react";
import { API_BASE_URL } from "../constants/admin";
import { extractApiData, extractErrorMessage } from "../utils/http";

const DEFAULT_PAGE_SIZE = 10;
const OPTION_PAGE_SIZE = 100;

export function useGears({ enabled }) {
  const [gears, setGears] = useState([]);
  const [gearsLoading, setGearsLoading] = useState(false);
  const [gearSubmitting, setGearSubmitting] = useState(false);
  const [gearError, setGearError] = useState("");
  const [gearModal, setGearModal] = useState(null); // null | gear(수정) | { isNew: true }(추가)
  const [categories, setCategories] = useState([]);
  const [makers, setMakers] = useState([]);
  const [categoriesLoading, setCategoriesLoading] = useState(false);
  const [makersLoading, setMakersLoading] = useState(false);
  const [reviewGear, setReviewGear] = useState(null);
  const [selectedCategoryId, setSelectedCategoryId] = useState("");
  const [selectedMakerId, setSelectedMakerId] = useState("");
  const [appliedCategoryId, setAppliedCategoryId] = useState("");
  const [appliedMakerId, setAppliedMakerId] = useState("");
  const [page, setPage] = useState(0);
  const [size] = useState(DEFAULT_PAGE_SIZE);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  const categoryOptions = useMemo(
    () =>
      categories.map((category) => ({
        value: String(category.id),
        label: `${category.id}. ${category.name}`
      })),
    [categories]
  );

  const makerOptions = useMemo(
    () =>
      makers.map((maker) => ({
        value: String(maker.id),
        label: maker.nameEng ? `${maker.id}. ${maker.name} / ${maker.nameEng}` : `${maker.id}. ${maker.name}`
      })),
    [makers]
  );

  async function fetchCategories() {
    setCategoriesLoading(true);
    try {
      const params = new URLSearchParams({ keyword: "", page: "0", size: String(OPTION_PAGE_SIZE) });
      const response = await fetch(`${API_BASE_URL}/admin/categories?${params}`, { credentials: "include" });
      if (!response.ok) throw new Error("카테고리 목록을 불러오지 못했습니다.");
      const data = await extractApiData(response);
      setCategories(data.content ?? []);
    } catch (error) {
      console.error(error);
      setGearError("카테고리 목록을 불러오지 못했습니다.");
    } finally {
      setCategoriesLoading(false);
    }
  }

  async function fetchMakers() {
    setMakersLoading(true);
    try {
      const params = new URLSearchParams({ keyword: "", page: "0", size: String(OPTION_PAGE_SIZE) });
      const response = await fetch(`${API_BASE_URL}/admin/makers?${params}`, { credentials: "include" });
      if (!response.ok) throw new Error("메이커 목록을 불러오지 못했습니다.");
      const data = await extractApiData(response);
      setMakers(data.content ?? []);
    } catch (error) {
      console.error(error);
      setGearError("메이커 목록을 불러오지 못했습니다.");
    } finally {
      setMakersLoading(false);
    }
  }

  async function fetchGears({
    nextPage = page,
    nextCategoryId = appliedCategoryId,
    nextMakerId = appliedMakerId
  } = {}) {
    setGearsLoading(true);
    setGearError("");

    const params = new URLSearchParams({ page: String(nextPage), size: String(size), keyword: "" });
    if (nextCategoryId) params.set("categoryId", nextCategoryId);
    if (nextMakerId) params.set("makerId", nextMakerId);

    try {
      const response = await fetch(`${API_BASE_URL}/admin/gears?${params}`, { credentials: "include" });
      if (!response.ok) throw new Error("용품 목록을 불러오지 못했습니다.");
      const data = await extractApiData(response);
      setGears(data.content ?? []);
      setPage(data.page ?? 0);
      setTotalPages(data.totalPages ?? 0);
      setTotalElements(data.totalElements ?? 0);
      setHasNext(Boolean(data.hasNext));
      setHasPrevious(Boolean(data.hasPrevious));
      setAppliedCategoryId(data.categoryId ? String(data.categoryId) : "");
      setAppliedMakerId(data.makerId ? String(data.makerId) : "");
      setSelectedCategoryId(data.categoryId ? String(data.categoryId) : "");
      setSelectedMakerId(data.makerId ? String(data.makerId) : "");
    } catch (error) {
      console.error(error);
      setGearError("용품 목록을 불러오지 못했습니다.");
    } finally {
      setGearsLoading(false);
    }
  }

  useEffect(() => {
    if (!enabled) return;
    fetchCategories();
    fetchMakers();
    fetchGears({ nextPage: 0, nextCategoryId: "", nextMakerId: "" });
  }, [enabled]);

  function openGearReviews(gear) {
    setReviewGear(gear);
  }

  function closeGearReviews() {
    setReviewGear(null);
  }

  function openCreateGearModal() {
    setGearModal({ isNew: true });
    setGearError("");
  }

  function openEditGearModal(gear) {
    setGearModal(gear);
    setGearError("");
  }

  function closeGearModal() {
    setGearModal(null);
    setGearError("");
  }

  function changeSelectedCategoryId(value) {
    setSelectedCategoryId(value);
  }

  function changeSelectedMakerId(value) {
    setSelectedMakerId(value);
  }

  async function applyGearFilters() {
    await fetchGears({ nextPage: 0, nextCategoryId: selectedCategoryId, nextMakerId: selectedMakerId });
  }

  async function saveGear(formData) {
    const trimmedName = formData.name.trim();
    if (!trimmedName) {
      setGearError("용품명은 비워둘 수 없습니다.");
      return;
    }
    if (!formData.categoryId) {
      setGearError("카테고리는 필수입니다.");
      return;
    }
    if (!formData.makerId) {
      setGearError("메이커는 필수입니다.");
      return;
    }

    setGearSubmitting(true);
    setGearError("");

    const isEditMode = gearModal && !gearModal.isNew && Boolean(gearModal.id);
    const url = isEditMode ? `${API_BASE_URL}/admin/gears/${gearModal.id}` : `${API_BASE_URL}/admin/gears`;
    const method = isEditMode ? "PUT" : "POST";

    try {
      const response = await fetch(url, {
        method,
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          name: trimmedName,
          link: formData.link.trim(),
          imageUrl: formData.imageUrl.trim(),
          categoryId: Number(formData.categoryId),
          makerId: Number(formData.makerId),
          memo: formData.memo.trim()
        })
      });

      if (!response.ok) {
        const message = await extractErrorMessage(response, "용품을 저장하지 못했습니다.");
        throw new Error(message);
      }

      await fetchGears();
      setGearModal(null);
    } catch (error) {
      console.error(error);
      setGearError(error.message || "용품을 저장하지 못했습니다.");
    } finally {
      setGearSubmitting(false);
    }
  }

  async function deleteGear(gearId) {
    if (!window.confirm("이 용품을 삭제할까요?")) return;

    setGearError("");
    try {
      const response = await fetch(`${API_BASE_URL}/admin/gears/${gearId}`, {
        method: "DELETE",
        credentials: "include"
      });
      if (!response.ok) {
        const message = await extractErrorMessage(response, "용품을 삭제하지 못했습니다.");
        throw new Error(message);
      }
      const fallbackPage = gears.length === 1 && page > 0 ? page - 1 : page;
      await fetchGears({ nextPage: fallbackPage });
      if (gearModal?.id === gearId) setGearModal(null);
    } catch (error) {
      console.error(error);
      setGearError(error.message || "용품을 삭제하지 못했습니다.");
    }
  }

  async function goToPage(nextPage) {
    if (nextPage < 0 || nextPage >= totalPages || nextPage === page) return;
    await fetchGears({ nextPage });
  }

  return {
    reviewGear,
    openGearReviews,
    closeGearReviews,
    gearModal,
    openCreateGearModal,
    openEditGearModal,
    closeGearModal,
    categoryOptions,
    makerOptions,
    categoriesLoading,
    makersLoading,
    gears,
    gearsLoading,
    gearSubmitting,
    gearError,
    selectedCategoryId,
    selectedMakerId,
    page,
    totalPages,
    totalElements,
    hasNext,
    hasPrevious,
    changeSelectedCategoryId,
    changeSelectedMakerId,
    applyGearFilters,
    saveGear,
    deleteGear,
    goToPage
  };
}
