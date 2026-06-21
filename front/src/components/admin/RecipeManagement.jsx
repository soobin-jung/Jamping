import { useState } from "react";
import { formatDate } from "../../utils/date";
import addIcon from "../../assets/action-icons/add.svg";
import deleteIcon from "../../assets/action-icons/delete.svg";
import editIcon from "../../assets/action-icons/edit.svg";
import { ActionIconButton } from "../common/ActionIconButton";
import { RecipeDetailModal } from "./RecipeDetailModal";
import { RecipeEditModal } from "./RecipeEditModal";

export function RecipeManagement({
  recipes,
  recipesLoading,
  recipeSubmitting,
  recipeError,
  recipeModal,
  recipeCategoryOptions,
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
}) {
  const [detailRecipe, setDetailRecipe] = useState(null);

  return (
    <section className="admin-card admin-table-card">
      <div className="admin-card-header">
        <div>
          <h2>레시피 관리</h2>
        </div>
      </div>

      <form
        className="admin-toolbar"
        onSubmit={(e) => { e.preventDefault(); applyRecipeFilters(); }}
      >
        <div className="admin-search-field">
          <select
            className="admin-search-input"
            value={selectedCategoryId}
            onChange={(e) => changeSelectedCategoryId(e.target.value)}
          >
            <option value="">전체 카테고리</option>
            {recipeCategoryOptions.map((opt) => (
              <option key={opt.value} value={opt.value}>{opt.label}</option>
            ))}
          </select>
        </div>
        <button type="submit" className="admin-secondary-button">검색</button>
        <button
          type="button"
          className="recipe-add-button"
          onClick={openCreateRecipeModal}
        >
          <img src={addIcon} alt="" className="recipe-add-icon" />
          레시피 추가
        </button>
      </form>

      {recipeError && !recipeModal && <p className="admin-error table-error">{recipeError}</p>}

      {recipesLoading ? (
        <div className="admin-empty-inline">레시피 목록을 불러오는 중입니다.</div>
      ) : (
        <>
          <div className="admin-table-meta">총 {totalElements}건</div>

          <div className="admin-table-wrap">
            <table className="admin-table">
              <thead>
                <tr>
                  <th width={50}>ID</th>
                  <th width={140}>카테고리</th>
                  <th>레시피명</th>
                  <th width={120}>등록자</th>
                  <th width={140}>등록일</th>
                  <th width={100} className="admin-action-column">수정</th>
                  <th width={100} className="admin-action-column">삭제</th>
                </tr>
              </thead>
              <tbody>
                {recipes.length === 0 && (
                  <tr>
                    <td colSpan={7} className="admin-empty-inline">등록된 레시피가 없습니다.</td>
                  </tr>
                )}

                {recipes.map((recipe) => (
                  <tr key={recipe.id}>
                    <td>{recipe.id}</td>
                    <td>
                      <div className="admin-cell-text">{recipe.recipeCategoryName}</div>
                    </td>
                    <td>
                      <div className="admin-cell-text">
                        <button
                          type="button"
                          className="recipe-name-link"
                          onClick={() => setDetailRecipe(recipe)}
                        >
                          {recipe.name}
                        </button>
                      </div>
                    </td>
                    <td>
                      <div className="admin-cell-text">{recipe.createdBy || "-"}</div>
                    </td>
                    <td>
                      <div className="admin-cell-text">{formatDate(recipe.createdAt)}</div>
                    </td>
                    <td className="admin-action-column">
                      <div className="admin-row-actions">
                        <ActionIconButton
                          icon={editIcon}
                          label="수정"
                          onClick={() => openEditRecipeModal(recipe)}
                        />
                      </div>
                    </td>
                    <td className="admin-action-column">
                      <div className="admin-row-actions">
                        <ActionIconButton
                          icon={deleteIcon}
                          label="삭제"
                          onClick={() => deleteRecipe(recipe.id)}
                        />
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="admin-pagination">
            <button type="button" className="admin-page-button" onClick={() => goToPage(page - 1)} disabled={!hasPrevious}>
              이전
            </button>
            <span className="admin-page-indicator">{totalPages === 0 ? 0 : page + 1} / {totalPages}</span>
            <button type="button" className="admin-page-button" onClick={() => goToPage(page + 1)} disabled={!hasNext}>
              다음
            </button>
          </div>
        </>
      )}

      {detailRecipe && (
        <RecipeDetailModal
          recipe={detailRecipe}
          onClose={() => setDetailRecipe(null)}
          onEdit={openEditRecipeModal}
        />
      )}

      {recipeModal && (
        <RecipeEditModal
          recipe={recipeModal.isNew ? null : recipeModal}
          recipeCategoryOptions={recipeCategoryOptions}
          submitting={recipeSubmitting}
          error={recipeError}
          onSave={saveRecipe}
          onClose={closeRecipeModal}
        />
      )}
    </section>
  );
}
