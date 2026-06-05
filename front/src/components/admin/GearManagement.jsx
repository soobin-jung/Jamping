import { formatDate } from "../../utils/date";
import cancelIcon from "../../assets/action-icons/cancel.svg";
import deleteIcon from "../../assets/action-icons/delete.svg";
import editIcon from "../../assets/action-icons/edit.svg";
import saveIcon from "../../assets/action-icons/save.svg";
import { SearchableSelect } from "../common/SearchableSelect";
import { ActionIconButton } from "../common/ActionIconButton";

/**
 * 관리자 용품 관리 테이블과 필터, 인라인 편집 UI를 렌더링합니다.
 */
export function GearManagement({
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
}) {
  const categoryFilterOptions = [{ value: "", label: "전체 카테고리" }, ...categoryOptions];
  const makerFilterOptions = [{ value: "", label: "전체 메이커" }, ...makerOptions];
  const filtersLoading = categoriesLoading || makersLoading;

  /**
   * 편집 중인 용품 행을 공통 구조로 렌더링합니다.
   */
  function renderEditorRow(gear = null) {
    return (
      <tr key={gear ? gear.id : "new"} className="admin-edit-row">
        <td>{gear ? gear.id : "new"}</td>
        <td>
          <input
            className="admin-inline-input"
            name="name"
            value={gearEditor?.name ?? ""}
            onChange={updateGearEditor}
            onKeyDown={handleGearEditorKeyDown}
            onKeyUp={handleGearEditorKeyUp}
            placeholder="용품명"
            maxLength={100}
          />
        </td>
        <td>
          <SearchableSelect
            value={gearEditor?.categoryId ?? ""}
            options={categoryOptions}
            placeholder="카테고리 선택"
            searchPlaceholder="카테고리 검색"
            emptyMessage="카테고리가 없습니다."
            onChange={(value) => updateGearEditorSelect("categoryId", value)}
          />
        </td>
        <td>
          <SearchableSelect
            value={gearEditor?.makerId ?? ""}
            options={makerOptions}
            placeholder="메이커 선택"
            searchPlaceholder="메이커 검색"
            emptyMessage="메이커가 없습니다."
            onChange={(value) => updateGearEditorSelect("makerId", value)}
          />
        </td>
        <td>
          <input
            className="admin-inline-input"
            name="link"
            value={gearEditor?.link ?? ""}
            onChange={updateGearEditor}
            onKeyDown={handleGearEditorKeyDown}
            onKeyUp={handleGearEditorKeyUp}
            placeholder="링크"
            maxLength={500}
          />
        </td>
        <td>
          <input
            className="admin-inline-input"
            name="imageUrl"
            value={gearEditor?.imageUrl ?? ""}
            onChange={updateGearEditor}
            onKeyDown={handleGearEditorKeyDown}
            onKeyUp={handleGearEditorKeyUp}
            placeholder="이미지 주소"
            maxLength={1000}
          />
        </td>
        <td>
          <input
            className="admin-inline-input"
            name="memo"
            value={gearEditor?.memo ?? ""}
            onChange={updateGearEditor}
            onKeyDown={handleGearEditorKeyDown}
            onKeyUp={handleGearEditorKeyUp}
            placeholder="메모"
            maxLength={1000}
          />
        </td>
        <td>
          <div className="admin-cell-text">{gear?.createdBy || "-"}</div>
        </td>
        <td>
          <div className="admin-cell-text">{formatDate(gear?.createdAt)}</div>
        </td>
        <td>
          <div className="admin-cell-text">{gear?.updatedBy || "-"}</div>
        </td>
        <td>
          <div className="admin-cell-text">{formatDate(gear?.updatedAt)}</div>
        </td>
        <td className="admin-action-column">
          <div className="admin-row-actions">
            <ActionIconButton icon={saveIcon} label="저장" onClick={saveGear} disabled={gearSubmitting} />
            <ActionIconButton icon={cancelIcon} label="취소" onClick={cancelGearEdit} disabled={gearSubmitting} />
          </div>
        </td>
        <td className="admin-action-column">
          {gear && (
            <div className="admin-row-actions">
              <ActionIconButton
                icon={deleteIcon}
                label="삭제"
                onClick={() => deleteGear(gear.id)}
                disabled={gearSubmitting}
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
          <h2>용품 관리</h2>
        </div>
      </div>

      <div className="admin-toolbar">
        <div className="admin-filter-field">
          <SearchableSelect
            value={selectedCategoryId}
            options={categoryFilterOptions}
            placeholder="카테고리 선택"
            searchPlaceholder="카테고리 검색"
            emptyMessage="카테고리가 없습니다."
            disabled={filtersLoading}
            onChange={changeSelectedCategoryId}
          />
        </div>

        <div className="admin-filter-field">
          <SearchableSelect
            value={selectedMakerId}
            options={makerFilterOptions}
            placeholder="메이커 선택"
            searchPlaceholder="메이커 검색"
            emptyMessage="메이커가 없습니다."
            disabled={filtersLoading}
            onChange={changeSelectedMakerId}
          />
        </div>

        <button
          type="button"
          className="admin-secondary-button"
          onClick={applyGearFilters}
          disabled={filtersLoading}
        >
          검색
        </button>
      </div>

      {gearError && <p className="admin-error table-error">{gearError}</p>}

      {gearsLoading ? (
        <div className="admin-empty-inline">용품 목록을 불러오는 중입니다.</div>
      ) : (
        <>
          <div className="admin-table-meta">총 {totalElements}건</div>

          <div className="admin-table-wrap">
            <table className="admin-table gear-table">
              <thead>
                <tr>
                  <th width={50}>ID</th>
                  <th width={180}>용품명</th>
                  <th width={200}>카테고리</th>
                  <th width={220}>메이커</th>
                  <th width={220}>링크</th>
                  <th width={220}>이미지</th>
                  <th width={220}>메모</th>
                  <th width={110}>등록자</th>
                  <th width={140}>등록일</th>
                  <th width={110}>수정자</th>
                  <th width={140}>수정일</th>
                  <th width={100} className="admin-action-column">수정</th>
                  <th width={100} className="admin-action-column">삭제</th>
                </tr>
              </thead>
              <tbody>
                {gears.length === 0 && !gearEditor?.isNew && (
                  <tr>
                    <td colSpan={13} className="admin-empty-inline">
                      조회된 용품이 없습니다.
                    </td>
                  </tr>
                )}

                {gears.map((gear) => {
                  const isEditing = gearEditor && !gearEditor.isNew && gearEditor.id === gear.id;

                  if (isEditing) {
                    return renderEditorRow(gear);
                  }

                  return (
                    <tr key={gear.id}>
                      <td>{gear.id}</td>
                      <td>
                        <div className="admin-cell-text">{gear.name}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">
                          {gear.categoryId ? `${gear.categoryId}. ${gear.categoryName}` : "-"}
                        </div>
                      </td>
                      <td>
                        <div className="admin-cell-text">
                          {gear.makerId ? `${gear.makerId}. ${gear.makerName}` : "-"}
                        </div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{gear.link || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{gear.imageUrl || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{gear.memo || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{gear.createdBy || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{formatDate(gear.createdAt)}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{gear.updatedBy || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{formatDate(gear.updatedAt)}</div>
                      </td>
                      <td className="admin-action-column">
                        <div className="admin-row-actions">
                          <ActionIconButton
                            icon={editIcon}
                            label="수정"
                            onClick={() => startGearEdit(gear)}
                            disabled={Boolean(gearEditor)}
                          />
                        </div>
                      </td>
                      <td className="admin-action-column">
                        <div className="admin-row-actions">
                          <ActionIconButton
                            icon={deleteIcon}
                            label="삭제"
                            onClick={() => deleteGear(gear.id)}
                            disabled={Boolean(gearEditor)}
                          />
                        </div>
                      </td>
                    </tr>
                  );
                })}

                {gearEditor?.isNew && renderEditorRow()}

                <tr className="admin-add-row">
                  <td colSpan={13}>
                    <button
                      type="button"
                      className="admin-add-row-button"
                      onClick={startCreateGear}
                      disabled={Boolean(gearEditor)}
                    >
                      + 용품 추가
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
