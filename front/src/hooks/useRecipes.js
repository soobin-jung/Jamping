import { useEffect, useMemo, useState } from "react";
import { API_BASE_URL } from "../constants/admin";
import { extractApiData, extractErrorMessage } from "../utils/http";

const DEFAULT_PAGE_SIZE = 10;
const OPTION_PAGE_SIZE = 100;

export function useRecipes({ enabled }) {
  const [recipes, setRecipes] = useState([]);
  const [recipesLoading, setRecipesLoading] = useState(false);
  const [recipeSubmitting, setRecipeSubmitting] = useState(false);
  const [recipeError, setRecipeError] = useState("");
  const [recipeModal, setRecipeModal] = useState(null);
  const [recipeCategories, setRecipeCategories] = useState([]);
  const [recipeCategoriesLoading, setRecipeCategoriesLoading] = useState(false);
  const [selectedCategoryId, setSelectedCategoryId] = useState("");
  const [appliedCategoryId, setAppliedCategoryId] = useState("");
  const [page, setPage] = useState(0);
  const [size] = useState(DEFAULT_PAGE_SIZE);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  const recipeCategoryOptions = useMemo(
    () => recipeCategories.map((c) => ({ value: String(c.id), label: `${c.id}. ${c.name}` })),
    [recipeCategories]
  );

  async function fetchRecipeCategoryOptions() {
    setRecipeCategoriesLoading(true);
    try {
      const params = new URLSearchParams({ keyword: "", page: "0", size: String(OPTION_PAGE_SIZE) });
      const response = await fetch(`${API_BASE_URL}/admin/recipe-categories?${params}`, { credentials: "include" });
      if (!response.ok) throw new Error();
      const data = await extractApiData(response);
      setRecipeCategories(data.content ?? []);
    } catch {
      console.error("레시피 카테고리 목록을 불러오지 못했습니다.");
    } finally {
      setRecipeCategoriesLoading(false);
    }
  }

  async function fetchRecipes({ nextPage = page, nextCategoryId = appliedCategoryId } = {}) {
    setRecipesLoading(true);
    setRecipeError("");

    const params = new URLSearchParams({ page: String(nextPage), size: String(size), keyword: "" });
    if (nextCategoryId) params.set("recipeCategoryId", nextCategoryId);

    try {
      const response = await fetch(`${API_BASE_URL}/admin/recipes?${params}`, { credentials: "include" });
      if (!response.ok) throw new Error("레시피 목록을 불러오지 못했습니다.");
      const data = await extractApiData(response);
      setRecipes(data.content ?? []);
      setPage(data.page ?? 0);
      setTotalPages(data.totalPages ?? 0);
      setTotalElements(data.totalElements ?? 0);
      setHasNext(Boolean(data.hasNext));
      setHasPrevious(Boolean(data.hasPrevious));
      setAppliedCategoryId(data.recipeCategoryId ? String(data.recipeCategoryId) : "");
      setSelectedCategoryId(data.recipeCategoryId ? String(data.recipeCategoryId) : "");
    } catch (error) {
      console.error(error);
      setRecipeError("레시피 목록을 불러오지 못했습니다.");
    } finally {
      setRecipesLoading(false);
    }
  }

  useEffect(() => {
    if (!enabled) return;
    fetchRecipeCategoryOptions();
    fetchRecipes({ nextPage: 0, nextCategoryId: "" });
  }, [enabled]);

  function openCreateRecipeModal() {
    setRecipeModal({ isNew: true });
    setRecipeError("");
  }

  function openEditRecipeModal(recipe) {
    setRecipeModal(recipe);
    setRecipeError("");
  }

  function closeRecipeModal() {
    setRecipeModal(null);
    setRecipeError("");
  }

  function changeSelectedCategoryId(value) {
    setSelectedCategoryId(value);
  }

  async function applyRecipeFilters() {
    await fetchRecipes({ nextPage: 0, nextCategoryId: selectedCategoryId });
  }

  async function saveRecipe(formData) {
    const trimmedName = formData.name.trim();
    if (!trimmedName) {
      setRecipeError("레시피명은 비워둘 수 없습니다.");
      return;
    }
    if (!formData.recipeCategoryId) {
      setRecipeError("카테고리는 필수입니다.");
      return;
    }

    setRecipeSubmitting(true);
    setRecipeError("");

    const isEditMode = recipeModal && !recipeModal.isNew && Boolean(recipeModal.id);
    const url = isEditMode ? `${API_BASE_URL}/admin/recipes/${recipeModal.id}` : `${API_BASE_URL}/admin/recipes`;
    const method = isEditMode ? "PUT" : "POST";

    try {
      const response = await fetch(url, {
        method,
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          name: trimmedName,
          ingredients: formData.ingredients.trim(),
          instructions: formData.instructions.trim(),
          recipeCategoryId: Number(formData.recipeCategoryId)
        })
      });

      if (!response.ok) {
        const message = await extractErrorMessage(response, "레시피를 저장하지 못했습니다.");
        throw new Error(message);
      }

      await fetchRecipes();
      setRecipeModal(null);
    } catch (error) {
      console.error(error);
      setRecipeError(error.message || "레시피를 저장하지 못했습니다.");
    } finally {
      setRecipeSubmitting(false);
    }
  }

  async function deleteRecipe(id) {
    if (!window.confirm("이 레시피를 삭제할까요?")) return;

    setRecipeError("");
    try {
      const response = await fetch(`${API_BASE_URL}/admin/recipes/${id}`, {
        method: "DELETE",
        credentials: "include"
      });
      if (!response.ok) {
        const message = await extractErrorMessage(response, "레시피를 삭제하지 못했습니다.");
        throw new Error(message);
      }
      const fallbackPage = recipes.length === 1 && page > 0 ? page - 1 : page;
      await fetchRecipes({ nextPage: fallbackPage });
      if (recipeModal?.id === id) setRecipeModal(null);
    } catch (error) {
      console.error(error);
      setRecipeError(error.message || "레시피를 삭제하지 못했습니다.");
    }
  }

  async function goToPage(nextPage) {
    if (nextPage < 0 || nextPage >= totalPages || nextPage === page) return;
    await fetchRecipes({ nextPage });
  }

  return {
    recipes,
    recipesLoading,
    recipeSubmitting,
    recipeError,
    recipeModal,
    recipeCategoryOptions,
    recipeCategoriesLoading,
    selectedCategoryId,
    page,
    totalPages,
    totalElements,
    hasNext,
    hasPrevious,
    openCreateRecipeModal,
    openEditRecipeModal,
    closeRecipeModal,
    changeSelectedCategoryId,
    applyRecipeFilters,
    saveRecipe,
    deleteRecipe,
    goToPage
  };
}
