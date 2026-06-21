import { formatDate } from "../../utils/date";
import cancelIcon from "../../assets/action-icons/cancel.svg";
import deleteIcon from "../../assets/action-icons/delete.svg";
import editIcon from "../../assets/action-icons/edit.svg";
import saveIcon from "../../assets/action-icons/save.svg";
import { ActionIconButton } from "../common/ActionIconButton";

export function RecipeCategoryManagement({
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
}) {
  function renderEditorRow(recipeCategory = null) {
    return (
      <tr key={recipeCategory ? recipeCategory.id : "new"} className="admin-edit-row">
        <td>{recipeCategory ? recipeCategory.id : "new"}</td>
        <td>
          <input
            className="admin-inline-input"
            name="name"
            value={recipeCategoryEditor?.name ?? ""}
            onChange={updateRecipeCategoryEditor}
            onKeyDown={handleRecipeCategoryEditorKeyDown}
            onKeyUp={handleRecipeCategoryEditorKeyUp}
            placeholder="카테고리명"
            maxLength={100}
          />
        </td>
        <td>
          <div className="admin-cell-text">{recipeCategory?.createdBy || "-"}</div>
        </td>
        <td>
          <div className="admin-cell-text">{formatDate(recipeCategory?.createdAt)}</div>
        </td>
        <td>
          <div className="admin-cell-text">{recipeCategory?.updatedBy || "-"}</div>
        </td>
        <td>
          <div className="admin-cell-text">{formatDate(recipeCategory?.updatedAt)}</div>
        </td>
        <td className="admin-action-column">
          <div className="admin-row-actions">
            <ActionIconButton icon={saveIcon} label="저장" onClick={saveRecipeCategory} disabled={recipeCategorySubmitting} />
            <ActionIconButton icon={cancelIcon} label="취소" onClick={cancelRecipeCategoryEdit} disabled={recipeCategorySubmitting} />
          </div>
        </td>
        <td className="admin-action-column">
          {recipeCategory && (
            <div className="admin-row-actions">
              <ActionIconButton
                icon={deleteIcon}
                label="삭제"
                onClick={() => deleteRecipeCategory(recipeCategory.id)}
                disabled={recipeCategorySubmitting}
              />
            </div>
          )}
        </td>
      </tr>
    );
  }

  return (
    <section className="admin-card admin-table-card">
      <div className="admin-card-header">
        <div>
          <h2>레시피 카테고리 관리</h2>
        </div>
      </div>

      <form className="admin-toolbar" onSubmit={submitSearch}>
        <div className="admin-search-field">
          <input
            type="text"
            value={searchInput}
            onChange={updateSearchInput}
            placeholder="카테고리명 검색"
            className="admin-search-input"
          />
        </div>
        <button type="submit" className="admin-secondary-button">검색</button>
      </form>

      {recipeCategoryError && <p className="admin-error table-error">{recipeCategoryError}</p>}

      {recipeCategoriesLoading ? (
        <div className="admin-empty-inline">카테고리 목록을 불러오는 중입니다.</div>
      ) : (
        <>
          <div className="admin-table-meta">총 {totalElements}건</div>

          <div className="admin-table-wrap">
            <table className="admin-table">
              <thead>
                <tr>
                  <th width={50}>ID</th>
                  <th width={200}>카테고리명</th>
                  <th width={120}>등록자</th>
                  <th width={140}>등록일</th>
                  <th width={120}>수정자</th>
                  <th width={140}>수정일</th>
                  <th width={100} className="admin-action-column">수정</th>
                  <th width={100} className="admin-action-column">삭제</th>
                </tr>
              </thead>
              <tbody>
                {recipeCategories.length === 0 && !recipeCategoryEditor?.isNew && (
                  <tr>
                    <td colSpan={8} className="admin-empty-inline">검색 결과가 없습니다.</td>
                  </tr>
                )}

                {recipeCategories.map((recipeCategory) => {
                  const isEditing =
                    recipeCategoryEditor && !recipeCategoryEditor.isNew && recipeCategoryEditor.id === recipeCategory.id;
                  if (isEditing) return renderEditorRow(recipeCategory);

                  return (
                    <tr key={recipeCategory.id}>
                      <td>{recipeCategory.id}</td>
                      <td>
                        <div className="admin-cell-text">{recipeCategory.name}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{recipeCategory.createdBy || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{formatDate(recipeCategory.createdAt)}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{recipeCategory.updatedBy || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{formatDate(recipeCategory.updatedAt)}</div>
                      </td>
                      <td className="admin-action-column">
                        <div className="admin-row-actions">
                          <ActionIconButton
                            icon={editIcon}
                            label="수정"
                            onClick={() => startRecipeCategoryEdit(recipeCategory)}
                            disabled={Boolean(recipeCategoryEditor)}
                          />
                        </div>
                      </td>
                      <td className="admin-action-column">
                        <div className="admin-row-actions">
                          <ActionIconButton
                            icon={deleteIcon}
                            label="삭제"
                            onClick={() => deleteRecipeCategory(recipeCategory.id)}
                            disabled={Boolean(recipeCategoryEditor)}
                          />
                        </div>
                      </td>
                    </tr>
                  );
                })}

                {recipeCategoryEditor?.isNew && renderEditorRow()}

                <tr className="admin-add-row">
                  <td colSpan={8}>
                    <button
                      type="button"
                      className="admin-add-row-button"
                      onClick={startCreateRecipeCategory}
                      disabled={Boolean(recipeCategoryEditor)}
                    >
                      + 카테고리 추가
                    </button>
                  </td>
                </tr>
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
    </section>
  );
}
