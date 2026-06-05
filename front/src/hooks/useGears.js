import { useEffect, useMemo, useState } from "react";
import { API_BASE_URL } from "../constants/admin";
import { extractApiData, extractErrorMessage } from "../utils/http";

const DEFAULT_PAGE_SIZE = 10;
const OPTION_PAGE_SIZE = 100;

/**
 * 관리자 용품 목록, 필터, 페이징, 인라인 편집 상태를 함께 관리합니다.
 */
export function useGears({ enabled }) {
  const [gears, setGears] = useState([]);
  const [gearsLoading, setGearsLoading] = useState(false);
  const [gearSubmitting, setGearSubmitting] = useState(false);
  const [gearError, setGearError] = useState("");
  const [gearEditor, setGearEditor] = useState(null);
  const [categories, setCategories] = useState([]);
  const [makers, setMakers] = useState([]);
  const [categoriesLoading, setCategoriesLoading] = useState(false);
  const [makersLoading, setMakersLoading] = useState(false);
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

  /**
   * 카테고리 선택 박스에 사용할 옵션 목록을 계산합니다.
   */
  const categoryOptions = useMemo(
    () =>
      categories.map((category) => ({
        value: String(category.id),
        label: `${category.id}. ${category.name}`
      })),
    [categories]
  );

  /**
   * 메이커 선택 박스에 사용할 옵션 목록을 계산합니다.
   */
  const makerOptions = useMemo(
    () =>
      makers.map((maker) => ({
        value: String(maker.id),
        label: maker.nameEng ? `${maker.id}. ${maker.name} / ${maker.nameEng}` : `${maker.id}. ${maker.name}`
      })),
    [makers]
  );

  /**
   * 카테고리 목록을 조회해 필터와 편집용 옵션으로 사용합니다.
   */
  async function fetchCategories() {
    setCategoriesLoading(true);

    try {
      const params = new URLSearchParams({
        keyword: "",
        page: "0",
        size: String(OPTION_PAGE_SIZE)
      });

      const response = await fetch(`${API_BASE_URL}/admin/categories?${params.toString()}`, {
        credentials: "include"
      });

      if (!response.ok) {
        throw new Error("카테고리 목록을 불러오지 못했습니다.");
      }

      const data = await extractApiData(response);
      setCategories(data.content ?? []);
    } catch (error) {
      console.error(error);
      setGearError("카테고리 목록을 불러오지 못했습니다.");
    } finally {
      setCategoriesLoading(false);
    }
  }

  /**
   * 메이커 목록을 조회해 필터와 편집용 옵션으로 사용합니다.
   */
  async function fetchMakers() {
    setMakersLoading(true);

    try {
      const params = new URLSearchParams({
        keyword: "",
        page: "0",
        size: String(OPTION_PAGE_SIZE)
      });

      const response = await fetch(`${API_BASE_URL}/admin/makers?${params.toString()}`, {
        credentials: "include"
      });

      if (!response.ok) {
        throw new Error("메이커 목록을 불러오지 못했습니다.");
      }

      const data = await extractApiData(response);
      setMakers(data.content ?? []);
    } catch (error) {
      console.error(error);
      setGearError("메이커 목록을 불러오지 못했습니다.");
    } finally {
      setMakersLoading(false);
    }
  }

  /**
   * 서버에서 용품 목록을 조회하고 현재 필터/페이징 상태를 동기화합니다.
   */
  async function fetchGears({
    nextPage = page,
    nextCategoryId = appliedCategoryId,
    nextMakerId = appliedMakerId
  } = {}) {
    setGearsLoading(true);
    setGearError("");

    const params = new URLSearchParams({
      page: String(nextPage),
      size: String(size),
      keyword: ""
    });

    if (nextCategoryId) {
      params.set("categoryId", nextCategoryId);
    }

    if (nextMakerId) {
      params.set("makerId", nextMakerId);
    }

    try {
      const response = await fetch(`${API_BASE_URL}/admin/gears?${params.toString()}`, {
        credentials: "include"
      });

      if (!response.ok) {
        throw new Error("용품 목록을 불러오지 못했습니다.");
      }

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
    if (!enabled) {
      return;
    }

    fetchCategories();
    fetchMakers();
    fetchGears({
      nextPage: 0,
      nextCategoryId: "",
      nextMakerId: ""
    });
  }, [enabled]);

  /**
   * 선택한 카테고리 필터 값을 갱신합니다.
   */
  function changeSelectedCategoryId(value) {
    setSelectedCategoryId(value);
  }

  /**
   * 선택한 메이커 필터 값을 갱신합니다.
   */
  function changeSelectedMakerId(value) {
    setSelectedMakerId(value);
  }

  /**
   * 현재 선택된 카테고리/메이커 기준으로 첫 페이지부터 다시 조회합니다.
   */
  async function applyGearFilters() {
    setGearEditor(null);
    await fetchGears({
      nextPage: 0,
      nextCategoryId: selectedCategoryId,
      nextMakerId: selectedMakerId
    });
  }

  /**
   * 새 용품을 추가할 수 있도록 빈 편집 행을 엽니다.
   */
  function startCreateGear() {
    setGearEditor({
      id: null,
      name: "",
      link: "",
      imageUrl: "",
      categoryId: selectedCategoryId || "",
      makerId: selectedMakerId || "",
      memo: "",
      isNew: true
    });
    setGearError("");
  }

  /**
   * 기존 용품을 인라인 수정 모드로 전환합니다.
   */
  function startGearEdit(gear) {
    setGearEditor({
      id: gear.id,
      name: gear.name,
      link: gear.link ?? "",
      imageUrl: gear.imageUrl ?? "",
      categoryId: gear.categoryId ? String(gear.categoryId) : "",
      makerId: gear.makerId ? String(gear.makerId) : "",
      memo: gear.memo ?? "",
      isNew: false
    });
    setGearError("");
  }

  /**
   * 현재 인라인 편집 상태를 종료합니다.
   */
  function cancelGearEdit() {
    setGearEditor(null);
    setGearError("");
  }

  /**
   * 인라인 편집 중인 입력 값을 갱신합니다.
   */
  function updateGearEditor(event) {
    const { name, value } = event.target;
    setGearEditor((previous) => ({
      ...previous,
      [name]: value
    }));
  }

  /**
   * 검색 가능한 셀렉트에서 바뀐 값을 편집 상태에 반영합니다.
   */
  function updateGearEditorSelect(field, value) {
    setGearEditor((previous) => ({
      ...previous,
      [field]: value
    }));
  }

  /**
   * 한글 조합 입력 중 Enter 기본 동작만 막고 실제 저장은 keyup에서 처리합니다.
   */
  function handleGearEditorKeyDown(event) {
    if (event.nativeEvent.isComposing || event.key !== "Enter" || gearSubmitting) {
      return;
    }

    event.preventDefault();
  }

  /**
   * Enter 입력이 끝나면 현재 편집 중인 용품을 저장합니다.
   */
  function handleGearEditorKeyUp(event) {
    if (event.nativeEvent.isComposing || event.key !== "Enter" || gearSubmitting) {
      return;
    }

    event.preventDefault();
    saveGear();
  }

  /**
   * 현재 편집 중인 용품을 생성하거나 수정합니다.
   */
  async function saveGear() {
    if (!gearEditor) {
      return;
    }

    const trimmedName = gearEditor.name.trim();
    const trimmedLink = gearEditor.link.trim();
    const trimmedImageUrl = gearEditor.imageUrl.trim();
    const trimmedMemo = gearEditor.memo.trim();

    if (!trimmedName) {
      setGearError("용품명은 비워둘 수 없습니다.");
      return;
    }

    if (!gearEditor.categoryId) {
      setGearError("카테고리는 필수입니다.");
      return;
    }

    if (!gearEditor.makerId) {
      setGearError("메이커는 필수입니다.");
      return;
    }

    setGearSubmitting(true);
    setGearError("");

    const isEditMode = !gearEditor.isNew && Boolean(gearEditor.id);
    const requestUrl = isEditMode
      ? `${API_BASE_URL}/admin/gears/${gearEditor.id}`
      : `${API_BASE_URL}/admin/gears`;
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
          link: trimmedLink,
          imageUrl: trimmedImageUrl,
          categoryId: Number(gearEditor.categoryId),
          makerId: Number(gearEditor.makerId),
          memo: trimmedMemo
        })
      });

      if (!response.ok) {
        const message = await extractErrorMessage(response, "용품을 저장하지 못했습니다.");
        throw new Error(message);
      }

      await fetchGears();
      setGearEditor(null);
    } catch (error) {
      console.error(error);
      setGearError(error.message || "용품을 저장하지 못했습니다.");
    } finally {
      setGearSubmitting(false);
    }
  }

  /**
   * 선택한 용품을 삭제하고 현재 필터 기준 목록을 다시 조회합니다.
   */
  async function deleteGear(gearId) {
    const shouldDelete = window.confirm("이 용품을 삭제할까요?");
    if (!shouldDelete) {
      return;
    }

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

      if (gearEditor?.id === gearId) {
        setGearEditor(null);
      }
    } catch (error) {
      console.error(error);
      setGearError(error.message || "용품을 삭제하지 못했습니다.");
    }
  }

  /**
   * 특정 페이지로 이동합니다.
   */
  async function goToPage(nextPage) {
    if (nextPage < 0 || nextPage >= totalPages || nextPage === page) {
      return;
    }

    await fetchGears({ nextPage });
  }

  return {
    categoryOptions,
    makerOptions,
    categoriesLoading,
    makersLoading,
    gears,
    gearsLoading,
    gearSubmitting,
    gearError,
    gearEditor,
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
    startCreateGear,
    startGearEdit,
    cancelGearEdit,
    updateGearEditor,
    updateGearEditorSelect,
    handleGearEditorKeyDown,
    handleGearEditorKeyUp,
    saveGear,
    deleteGear,
    goToPage
  };
}
