import { formatDate } from "../../utils/date";
import cancelIcon from "../../assets/action-icons/cancel.svg";
import deleteIcon from "../../assets/action-icons/delete.svg";
import editIcon from "../../assets/action-icons/edit.svg";
import saveIcon from "../../assets/action-icons/save.svg";
import { ActionIconButton } from "../common/ActionIconButton";

/**
 * 관리자 카테고리 관리 테이블과 검색, 페이지 UI를 렌더링합니다.
 */
export function CategoryManagement({
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
}) {
  /**
   * 인라인 편집 행을 공통 구조로 렌더링합니다.
   */
  function renderEditorRow(category = null) {
    return (
      <tr key={category ? category.id : "new"} className="admin-edit-row">
        <td>{category ? category.id : "new"}</td>
        <td>
          <input
            className="admin-inline-input"
            name="name"
            value={categoryEditor?.name ?? ""}
            onChange={updateCategoryEditor}
            onKeyDown={handleCategoryEditorKeyDown}
            onKeyUp={handleCategoryEditorKeyUp}
            placeholder="카테고리명"
            maxLength={100}
          />
        </td>
        <td>
          <input
            className="admin-inline-input"
            name="memo"
            value={categoryEditor?.memo ?? ""}
            onChange={updateCategoryEditor}
            onKeyDown={handleCategoryEditorKeyDown}
            onKeyUp={handleCategoryEditorKeyUp}
            placeholder="메모"
            maxLength={1000}
          />
        </td>
        <td>
          <div className="admin-cell-text">{category?.createdBy || "-"}</div>
        </td>
        <td>
          <div className="admin-cell-text">{formatDate(category?.createdAt)}</div>
        </td>
        <td>
          <div className="admin-cell-text">{category?.updatedBy || "-"}</div>
        </td>
        <td>
          <div className="admin-cell-text">{formatDate(category?.updatedAt)}</div>
        </td>
        <td className="admin-action-column">
          <div className="admin-row-actions">
            <ActionIconButton icon={saveIcon} label="저장" onClick={saveCategory} disabled={categorySubmitting} />
            <ActionIconButton
              icon={cancelIcon}
              label="취소"
              onClick={cancelCategoryEdit}
              disabled={categorySubmitting}
            />
          </div>
        </td>
        <td className="admin-action-column">
          {category && (
            <div className="admin-row-actions">
              <ActionIconButton
                icon={deleteIcon}
                label="삭제"
                onClick={() => deleteCategory(category.id)}
                disabled={categorySubmitting}
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
          <h2>카테고리 관리</h2>
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
        <button type="submit" className="admin-secondary-button">
          검색
        </button>
      </form>

      {categoryError && <p className="admin-error table-error">{categoryError}</p>}

      {categoriesLoading ? (
        <div className="admin-empty-inline">카테고리 목록을 불러오는 중입니다.</div>
      ) : (
        <>
          <div className="admin-table-meta">총 {totalElements}건</div>

          <div className="admin-table-wrap">
            <table className="admin-table">
              <thead>
                <tr>
                  <th width={50}>ID</th>
                  <th width={180}>카테고리명</th>
                  <th>메모</th>
                  <th width={110}>등록자</th>
                  <th width={140}>등록일</th>
                  <th width={110}>수정자</th>
                  <th width={140}>수정일</th>
                  <th width={100} className="admin-action-column">수정</th>
                  <th width={100} className="admin-action-column">삭제</th>
                </tr>
              </thead>
              <tbody>
                {categories.length === 0 && !categoryEditor?.isNew && (
                  <tr>
                    <td colSpan={9} className="admin-empty-inline">
                      검색 결과가 없습니다.
                    </td>
                  </tr>
                )}

                {categories.map((category) => {
                  const isEditing = categoryEditor && !categoryEditor.isNew && categoryEditor.id === category.id;

                  if (isEditing) {
                    return renderEditorRow(category);
                  }

                  return (
                    <tr key={category.id}>
                      <td>{category.id}</td>
                      <td>
                        <div className="admin-cell-text">{category.name}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{category.memo || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{category.createdBy || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{formatDate(category.createdAt)}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{category.updatedBy || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{formatDate(category.updatedAt)}</div>
                      </td>
                      <td className="admin-action-column">
                        <div className="admin-row-actions">
                          <ActionIconButton
                            icon={editIcon}
                            label="수정"
                            onClick={() => startCategoryEdit(category)}
                            disabled={Boolean(categoryEditor)}
                          />
                        </div>
                      </td>
                      <td className="admin-action-column">
                        <div className="admin-row-actions">
                          <ActionIconButton
                            icon={deleteIcon}
                            label="삭제"
                            onClick={() => deleteCategory(category.id)}
                            disabled={Boolean(categoryEditor)}
                          />
                        </div>
                      </td>
                    </tr>
                  );
                })}

                {categoryEditor?.isNew && renderEditorRow()}

                <tr className="admin-add-row">
                  <td colSpan={9}>
                    <button
                      type="button"
                      className="admin-add-row-button"
                      onClick={startCreateCategory}
                      disabled={Boolean(categoryEditor)}
                    >
                      + 카테고리 추가
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <div className="admin-pagination">
            <button
              type="button"
              className="admin-page-button"
              onClick={() => goToPage(page - 1)}
              disabled={!hasPrevious}
            >
              이전
            </button>
            <span className="admin-page-indicator">
              {totalPages === 0 ? 0 : page + 1} / {totalPages}
            </span>
            <button
              type="button"
              className="admin-page-button"
              onClick={() => goToPage(page + 1)}
              disabled={!hasNext}
            >
              다음
            </button>
          </div>
        </>
      )}
    </section>
  );
}
