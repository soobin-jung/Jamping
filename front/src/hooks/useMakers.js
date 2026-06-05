import { useEffect, useState } from "react";
import { API_BASE_URL } from "../constants/admin";
import { extractApiData, extractErrorMessage } from "../utils/http";

const DEFAULT_PAGE_SIZE = 10;

/**
 * 관리자 메이커 목록, 검색, 페이징, 인라인 편집 상태를 함께 관리합니다.
 */
export function useMakers({ enabled }) {
  const [makers, setMakers] = useState([]);
  const [makersLoading, setMakersLoading] = useState(false);
  const [makerSubmitting, setMakerSubmitting] = useState(false);
  const [makerError, setMakerError] = useState("");
  const [makerEditor, setMakerEditor] = useState(null);
  const [makerKeyword, setMakerKeyword] = useState("");
  const [makerSearchInput, setMakerSearchInput] = useState("");
  const [page, setPage] = useState(0);
  const [size] = useState(DEFAULT_PAGE_SIZE);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  /**
   * 서버에서 메이커 목록을 검색 조건과 페이지 정보로 다시 조회합니다.
   */
  async function fetchMakers({ nextPage = page, nextKeyword = makerKeyword } = {}) {
    setMakersLoading(true);
    setMakerError("");

    const params = new URLSearchParams({
      keyword: nextKeyword,
      page: String(nextPage),
      size: String(size)
    });

    try {
      const response = await fetch(`${API_BASE_URL}/admin/makers?${params.toString()}`, {
        credentials: "include"
      });

      if (!response.ok) {
        throw new Error("메이커 목록을 불러오지 못했습니다.");
      }

      const data = await extractApiData(response);
      setMakers(data.content ?? []);
      setPage(data.page ?? 0);
      setTotalPages(data.totalPages ?? 0);
      setTotalElements(data.totalElements ?? 0);
      setHasNext(Boolean(data.hasNext));
      setHasPrevious(Boolean(data.hasPrevious));
      setMakerKeyword(data.keyword ?? "");
      setMakerSearchInput(data.keyword ?? "");
    } catch (error) {
      console.error(error);
      setMakerError("메이커 목록을 불러오지 못했습니다.");
    } finally {
      setMakersLoading(false);
    }
  }

  useEffect(() => {
    if (!enabled) {
      return;
    }

    fetchMakers({ nextPage: 0, nextKeyword: "" });
  }, [enabled]);

  /**
   * 검색 입력 값을 갱신합니다.
   */
  function updateMakerSearchInput(event) {
    setMakerSearchInput(event.target.value);
  }

  /**
   * 현재 입력값 기준으로 첫 페이지부터 다시 조회합니다.
   */
  async function searchMakers() {
    setMakerEditor(null);
    await fetchMakers({
      nextPage: 0,
      nextKeyword: makerSearchInput.trim()
    });
  }

  /**
   * 검색 입력창에서 Enter를 누르면 메이커 검색을 수행합니다.
   */
  function handleMakerSearchKeyDown(event) {
    if (event.nativeEvent.isComposing || event.key !== "Enter") {
      return;
    }

    event.preventDefault();
    searchMakers();
  }

  /**
   * 특정 페이지로 이동합니다.
   */
  async function goToPage(nextPage) {
    if (nextPage < 0 || nextPage >= totalPages || nextPage === page) {
      return;
    }

    await fetchMakers({
      nextPage,
      nextKeyword: makerKeyword
    });
  }

  /**
   * 새 메이커를 추가할 수 있도록 빈 편집 상태를 엽니다.
   */
  function startCreateMaker() {
    setMakerEditor({
      id: null,
      name: "",
      nameEng: "",
      homepageUrl: "",
      isNew: true
    });
    setMakerError("");
  }

  /**
   * 선택한 메이커를 인라인 수정 모드로 전환합니다.
   */
  function startMakerEdit(maker) {
    setMakerEditor({
      id: maker.id,
      name: maker.name,
      nameEng: maker.nameEng ?? "",
      homepageUrl: maker.homepageUrl ?? "",
      isNew: false
    });
    setMakerError("");
  }

  /**
   * 현재 메이커 편집 상태를 종료합니다.
   */
  function cancelMakerEdit() {
    setMakerEditor(null);
    setMakerError("");
  }

  /**
   * 편집 중인 메이커 입력 값을 갱신합니다.
   */
  function updateMakerEditor(event) {
    const { name, value } = event.target;
    setMakerEditor((previous) => ({
      ...previous,
      [name]: value
    }));
  }

  /**
   * 한글 조합 입력 중 Enter 기본 동작만 막고 실제 저장은 keyup에서 처리합니다.
   */
  function handleMakerEditorKeyDown(event) {
    if (event.nativeEvent.isComposing || event.key !== "Enter" || makerSubmitting) {
      return;
    }

    event.preventDefault();
  }

  /**
   * Enter 입력이 끝나면 현재 편집 중인 메이커를 저장합니다.
   */
  function handleMakerEditorKeyUp(event) {
    if (event.nativeEvent.isComposing || event.key !== "Enter" || makerSubmitting) {
      return;
    }

    event.preventDefault();
    saveMaker();
  }

  /**
   * 현재 편집 상태를 생성 또는 수정 요청으로 저장합니다.
   */
  async function saveMaker() {
    if (!makerEditor) {
      return;
    }

    const trimmedName = makerEditor.name.trim();
    const trimmedNameEng = makerEditor.nameEng.trim();
    const trimmedHomepageUrl = makerEditor.homepageUrl.trim();

    if (!trimmedName) {
      setMakerError("메이커명은 비워둘 수 없습니다.");
      return;
    }

    setMakerSubmitting(true);
    setMakerError("");

    const isEditMode = !makerEditor.isNew && Boolean(makerEditor.id);
    const requestUrl = isEditMode
      ? `${API_BASE_URL}/admin/makers/${makerEditor.id}`
      : `${API_BASE_URL}/admin/makers`;
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
          nameEng: trimmedNameEng,
          homepageUrl: trimmedHomepageUrl
        })
      });

      if (!response.ok) {
        const message = await extractErrorMessage(response, "메이커를 저장하지 못했습니다.");
        throw new Error(message);
      }

      await fetchMakers({ nextPage: page, nextKeyword: makerKeyword });
      setMakerEditor(null);
    } catch (error) {
      console.error(error);
      setMakerError(error.message || "메이커를 저장하지 못했습니다.");
    } finally {
      setMakerSubmitting(false);
    }
  }

  /**
   * 선택한 메이커를 삭제하고 현재 페이지를 다시 조회합니다.
   */
  async function deleteMaker(makerId) {
    const shouldDelete = window.confirm("이 메이커를 삭제할까요?");
    if (!shouldDelete) {
      return;
    }

    setMakerError("");

    try {
      const response = await fetch(`${API_BASE_URL}/admin/makers/${makerId}`, {
        method: "DELETE",
        credentials: "include"
      });

      if (!response.ok) {
        const message = await extractErrorMessage(response, "메이커를 삭제하지 못했습니다.");
        throw new Error(message);
      }

      const fallbackPage = makers.length === 1 && page > 0 ? page - 1 : page;
      await fetchMakers({
        nextPage: fallbackPage,
        nextKeyword: makerKeyword
      });

      if (makerEditor?.id === makerId) {
        setMakerEditor(null);
      }
    } catch (error) {
      console.error(error);
      setMakerError(error.message || "메이커를 삭제하지 못했습니다.");
    }
  }

  return {
    makers,
    makersLoading,
    makerSubmitting,
    makerError,
    makerEditor,
    makerSearchInput,
    page,
    totalPages,
    totalElements,
    hasNext,
    hasPrevious,
    updateMakerSearchInput,
    handleMakerSearchKeyDown,
    searchMakers,
    goToPage,
    startCreateMaker,
    startMakerEdit,
    cancelMakerEdit,
    updateMakerEditor,
    handleMakerEditorKeyDown,
    handleMakerEditorKeyUp,
    saveMaker,
    deleteMaker
  };
}
