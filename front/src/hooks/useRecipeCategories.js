import { useEffect, useState } from "react";
import { API_BASE_URL } from "../constants/admin";
import { extractApiData, extractErrorMessage } from "../utils/http";

const DEFAULT_PAGE_SIZE = 10;

export function useRecipeCategories({ enabled }) {
  const [recipeCategories, setRecipeCategories] = useState([]);
  const [recipeCategoriesLoading, setRecipeCategoriesLoading] = useState(false);
  const [recipeCategorySubmitting, setRecipeCategorySubmitting] = useState(false);
  const [recipeCategoryError, setRecipeCategoryError] = useState("");
  const [recipeCategoryEditor, setRecipeCategoryEditor] = useState(null);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [searchInput, setSearchInput] = useState("");
  const [page, setPage] = useState(0);
  const [size] = useState(DEFAULT_PAGE_SIZE);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  async function fetchRecipeCategories({ nextPage = page, nextKeyword = searchKeyword } = {}) {
    setRecipeCategoriesLoading(true);
    setRecipeCategoryError("");

    const params = new URLSearchParams({ keyword: nextKeyword, page: String(nextPage), size: String(size) });

    try {
      const response = await fetch(`${API_BASE_URL}/admin/recipe-categories?${params}`, { credentials: "include" });
      if (!response.ok) throw new Error("레시피 카테고리 목록을 불러오지 못했습니다.");

      const data = await extractApiData(response);
      setRecipeCategories(data.content ?? []);
      setPage(data.page ?? 0);
      setTotalPages(data.totalPages ?? 0);
      setTotalElements(data.totalElements ?? 0);
      setHasNext(Boolean(data.hasNext));
      setHasPrevious(Boolean(data.hasPrevious));
      setSearchKeyword(data.keyword ?? "");
      setSearchInput(data.keyword ?? "");
    } catch (error) {
      console.error(error);
      setRecipeCategoryError("레시피 카테고리 목록을 불러오지 못했습니다.");
    } finally {
      setRecipeCategoriesLoading(false);
    }
  }

  useEffect(() => {
    if (!enabled) {
      setRecipeCategoryEditor(null);
      setRecipeCategoryError("");
      return;
    }
    fetchRecipeCategories({ nextPage: 0, nextKeyword: "" });
  }, [enabled]);

  function updateSearchInput(event) {
    setSearchInput(event.target.value);
  }

  async function submitSearch(event) {
    event.preventDefault();
    await fetchRecipeCategories({ nextPage: 0, nextKeyword: searchInput.trim() });
  }

  async function goToPage(nextPage) {
    if (nextPage < 0 || nextPage >= totalPages || nextPage === page) return;
    await fetchRecipeCategories({ nextPage, nextKeyword: searchKeyword });
  }

  function startCreateRecipeCategory() {
    setRecipeCategoryEditor({ id: null, name: "", isNew: true });
    setRecipeCategoryError("");
  }

  function startRecipeCategoryEdit(recipeCategory) {
    setRecipeCategoryEditor({ id: recipeCategory.id, name: recipeCategory.name, isNew: false });
    setRecipeCategoryError("");
  }

  function cancelRecipeCategoryEdit() {
    setRecipeCategoryEditor(null);
    setRecipeCategoryError("");
  }

  function updateRecipeCategoryEditor(event) {
    const { name, value } = event.target;
    setRecipeCategoryEditor((prev) => ({ ...prev, [name]: value }));
  }

  function handleRecipeCategoryEditorKeyDown(event) {
    if (event.nativeEvent.isComposing || event.key !== "Enter" || recipeCategorySubmitting) return;
    event.preventDefault();
  }

  function handleRecipeCategoryEditorKeyUp(event) {
    if (event.nativeEvent.isComposing || event.key !== "Enter" || recipeCategorySubmitting) return;
    event.preventDefault();
    saveRecipeCategory();
  }

  async function saveRecipeCategory() {
    if (!recipeCategoryEditor) return;

    const trimmedName = recipeCategoryEditor.name.trim();
    if (!trimmedName) {
      setRecipeCategoryError("카테고리명은 비워둘 수 없습니다.");
      return;
    }

    setRecipeCategorySubmitting(true);
    setRecipeCategoryError("");

    const isEditMode = !recipeCategoryEditor.isNew && Boolean(recipeCategoryEditor.id);
    const url = isEditMode
      ? `${API_BASE_URL}/admin/recipe-categories/${recipeCategoryEditor.id}`
      : `${API_BASE_URL}/admin/recipe-categories`;
    const method = isEditMode ? "PUT" : "POST";

    try {
      const response = await fetch(url, {
        method,
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name: trimmedName })
      });

      if (!response.ok) {
        const message = await extractErrorMessage(response, "카테고리를 저장하지 못했습니다.");
        throw new Error(message);
      }

      await fetchRecipeCategories({ nextPage: page, nextKeyword: searchKeyword });
      setRecipeCategoryEditor(null);
    } catch (error) {
      console.error(error);
      setRecipeCategoryError(error.message || "카테고리를 저장하지 못했습니다.");
    } finally {
      setRecipeCategorySubmitting(false);
    }
  }

  async function deleteRecipeCategory(id) {
    if (!window.confirm("이 카테고리를 삭제할까요?")) return;

    setRecipeCategoryError("");

    try {
      const response = await fetch(`${API_BASE_URL}/admin/recipe-categories/${id}`, {
        method: "DELETE",
        credentials: "include"
      });

      if (!response.ok) {
        const message = await extractErrorMessage(response, "카테고리를 삭제하지 못했습니다.");
        throw new Error(message);
      }

      const fallbackPage = recipeCategories.length === 1 && page > 0 ? page - 1 : page;
      await fetchRecipeCategories({ nextPage: fallbackPage, nextKeyword: searchKeyword });

      if (recipeCategoryEditor?.id === id) setRecipeCategoryEditor(null);
    } catch (error) {
      console.error(error);
      setRecipeCategoryError(error.message || "카테고리를 삭제하지 못했습니다.");
    }
  }

  return {
    recipeCategories,
    recipeCategoriesLoading,
    recipeCategorySubmitting,
    recipeCategoryError,
    recipeCategoryEditor,
    searchInput,
    page,
    totalPages,
    totalElements,
    hasNext,
    hasPrevious,
    updateSearchInput,
    submitSearch,
    goToPage,
    startCreateRecipeCategory,
    startRecipeCategoryEdit,
    cancelRecipeCategoryEdit,
    updateRecipeCategoryEditor,
    handleRecipeCategoryEditorKeyDown,
    handleRecipeCategoryEditorKeyUp,
    saveRecipeCategory,
    deleteRecipeCategory
  };
}
