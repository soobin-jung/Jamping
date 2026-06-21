import { useEffect, useState } from "react";
import { API_BASE_URL } from "../constants/admin";
import { extractApiData, extractErrorMessage } from "../utils/http";

const DEFAULT_PAGE_SIZE = 10;

export function useCampSites({ enabled }) {
  const [campSites, setCampSites] = useState([]);
  const [campSitesLoading, setCampSitesLoading] = useState(false);
  const [campSiteSubmitting, setCampSiteSubmitting] = useState(false);
  const [campSiteError, setCampSiteError] = useState("");
  const [campSiteModal, setCampSiteModal] = useState(null);
  const [selectedRegionCode, setSelectedRegionCode] = useState("");
  const [selectedDistrictCode, setSelectedDistrictCode] = useState("");
  const [selectedKeyword, setSelectedKeyword] = useState("");
  const [appliedRegionCode, setAppliedRegionCode] = useState("");
  const [appliedDistrictCode, setAppliedDistrictCode] = useState("");
  const [page, setPage] = useState(0);
  const [size] = useState(DEFAULT_PAGE_SIZE);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  async function fetchCampSites({
    nextPage = page,
    nextRegionCode = appliedRegionCode,
    nextDistrictCode = appliedDistrictCode,
    nextKeyword = selectedKeyword
  } = {}) {
    setCampSitesLoading(true);
    setCampSiteError("");

    const params = new URLSearchParams({ page: String(nextPage), size: String(size), keyword: nextKeyword });
    if (nextDistrictCode) {
      params.set("districtCode", nextDistrictCode);
    } else if (nextRegionCode) {
      params.set("regionCode", nextRegionCode);
    }

    try {
      const response = await fetch(`${API_BASE_URL}/admin/campsites?${params}`, { credentials: "include" });
      if (!response.ok) throw new Error("캠핑장 목록을 불러오지 못했습니다.");
      const data = await extractApiData(response);
      setCampSites(data.content ?? []);
      setPage(data.page ?? 0);
      setTotalPages(data.totalPages ?? 0);
      setTotalElements(data.totalElements ?? 0);
      setHasNext(Boolean(data.hasNext));
      setHasPrevious(Boolean(data.hasPrevious));
      setAppliedRegionCode(data.regionCode ?? "");
      setAppliedDistrictCode(data.districtCode ?? "");
    } catch (error) {
      console.error(error);
      setCampSiteError("캠핑장 목록을 불러오지 못했습니다.");
    } finally {
      setCampSitesLoading(false);
    }
  }

  useEffect(() => {
    if (!enabled) {
      setCampSiteModal(null);
      setCampSiteError("");
      return;
    }
    fetchCampSites({ nextPage: 0, nextRegionCode: "", nextDistrictCode: "", nextKeyword: "" });
  }, [enabled]);

  function openCreateCampSiteModal() {
    setCampSiteModal({ isNew: true });
    setCampSiteError("");
  }

  function openEditCampSiteModal(campSite) {
    setCampSiteModal(campSite);
    setCampSiteError("");
  }

  function closeCampSiteModal() {
    setCampSiteModal(null);
    setCampSiteError("");
  }

  function changeSelectedRegionCode(value) {
    setSelectedRegionCode(value);
    setSelectedDistrictCode("");
  }

  function changeSelectedDistrictCode(value) {
    setSelectedDistrictCode(value);
  }

  function changeSelectedKeyword(value) {
    setSelectedKeyword(value);
  }

  async function applyCampSiteFilters() {
    await fetchCampSites({
      nextPage: 0,
      nextRegionCode: selectedRegionCode,
      nextDistrictCode: selectedDistrictCode,
      nextKeyword: selectedKeyword
    });
  }

  async function saveCampSite(formData) {
    const trimmedName = formData.name.trim();
    if (!trimmedName) {
      setCampSiteError("캠핑장명은 비워둘 수 없습니다.");
      return;
    }
    if (!formData.regionCode) {
      setCampSiteError("지역구는 필수입니다.");
      return;
    }
    if (!formData.districtCode) {
      setCampSiteError("자치구는 필수입니다.");
      return;
    }
    if (!formData.checkInTime) {
      setCampSiteError("입실 시간은 필수입니다.");
      return;
    }
    if (!formData.checkOutTime) {
      setCampSiteError("퇴실 시간은 필수입니다.");
      return;
    }

    setCampSiteSubmitting(true);
    setCampSiteError("");

    const isEditMode = campSiteModal && !campSiteModal.isNew && Boolean(campSiteModal.id);
    const url = isEditMode
      ? `${API_BASE_URL}/admin/campsites/${campSiteModal.id}`
      : `${API_BASE_URL}/admin/campsites`;
    const method = isEditMode ? "PUT" : "POST";

    try {
      const response = await fetch(url, {
        method,
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          name: trimmedName,
          link: formData.link.trim() || null,
          regionCode: formData.regionCode,
          districtCode: formData.districtCode,
          checkInTime: formData.checkInTime,
          checkOutTime: formData.checkOutTime
        })
      });

      if (!response.ok) {
        const message = await extractErrorMessage(response, "캠핑장을 저장하지 못했습니다.");
        throw new Error(message);
      }

      await fetchCampSites();
      setCampSiteModal(null);
    } catch (error) {
      console.error(error);
      setCampSiteError(error.message || "캠핑장을 저장하지 못했습니다.");
    } finally {
      setCampSiteSubmitting(false);
    }
  }

  async function deleteCampSite(id) {
    if (!window.confirm("이 캠핑장을 삭제할까요?")) return;

    setCampSiteError("");
    try {
      const response = await fetch(`${API_BASE_URL}/admin/campsites/${id}`, {
        method: "DELETE",
        credentials: "include"
      });
      if (!response.ok) {
        const message = await extractErrorMessage(response, "캠핑장을 삭제하지 못했습니다.");
        throw new Error(message);
      }
      const fallbackPage = campSites.length === 1 && page > 0 ? page - 1 : page;
      await fetchCampSites({ nextPage: fallbackPage });
      if (campSiteModal?.id === id) setCampSiteModal(null);
    } catch (error) {
      console.error(error);
      setCampSiteError(error.message || "캠핑장을 삭제하지 못했습니다.");
    }
  }

  async function goToPage(nextPage) {
    if (nextPage < 0 || nextPage >= totalPages || nextPage === page) return;
    await fetchCampSites({ nextPage });
  }

  return {
    campSites,
    campSitesLoading,
    campSiteSubmitting,
    campSiteError,
    campSiteModal,
    selectedRegionCode,
    selectedDistrictCode,
    selectedKeyword,
    page,
    totalPages,
    totalElements,
    hasNext,
    hasPrevious,
    openCreateCampSiteModal,
    openEditCampSiteModal,
    closeCampSiteModal,
    changeSelectedRegionCode,
    changeSelectedDistrictCode,
    changeSelectedKeyword,
    applyCampSiteFilters,
    saveCampSite,
    deleteCampSite,
    goToPage
  };
}
