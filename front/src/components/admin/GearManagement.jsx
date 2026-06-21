import { formatDate } from "../../utils/date";
import deleteIcon from "../../assets/action-icons/delete.svg";
import editIcon from "../../assets/action-icons/edit.svg";
import { SearchableSelect } from "../common/SearchableSelect";
import { ActionIconButton } from "../common/ActionIconButton";
import { GearReviewModal } from "./GearReviewModal";
import { GearEditModal } from "./GearEditModal";

export function GearManagement({
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
}) {
  const categoryFilterOptions = [{ value: "", label: "전체 카테고리" }, ...categoryOptions];
  const makerFilterOptions = [{ value: "", label: "전체 메이커" }, ...makerOptions];
  const filtersLoading = categoriesLoading || makersLoading;

  return (
    <section className="admin-card admin-table-card">
      {reviewGear && <GearReviewModal gear={reviewGear} onClose={closeGearReviews} />}

      {gearModal && (
        <GearEditModal
          gear={gearModal.isNew ? null : gearModal}
          categoryOptions={categoryOptions}
          makerOptions={makerOptions}
          submitting={gearSubmitting}
          error={gearError}
          onSave={saveGear}
          onClose={closeGearModal}
        />
      )}

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

      {gearError && !gearModal && <p className="admin-error table-error">{gearError}</p>}

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
                  <th width={300}>용품명</th>
                  <th width={160}>카테고리</th>
                  <th width={200}>메이커</th>
                  <th width={80} className="admin-action-column">링크</th>
                  <th width={60} className="admin-action-column">이미지</th>
                  <th width={200}>메모</th>
                  <th width={110}>등록자</th>
                  <th width={130}>등록일</th>
                  <th width={110}>수정자</th>
                  <th width={130}>수정일</th>
                  <th width={80} className="admin-action-column">수정</th>
                  <th width={80} className="admin-action-column">삭제</th>
                </tr>
              </thead>
              <tbody>
                {gears.length === 0 && (
                  <tr>
                    <td colSpan={13} className="admin-empty-inline">
                      조회된 용품이 없습니다.
                    </td>
                  </tr>
                )}

                {gears.map((gear) => (
                  <tr key={gear.id}>
                    <td>{gear.id}</td>
                    <td>
                      <div className="admin-cell-text">
                        <button
                          type="button"
                          className="gear-name-link"
                          onClick={() => openGearReviews(gear)}
                        >
                          {gear.name}
                        </button>
                      </div>
                    </td>
                    <td>
                      <div className="admin-cell-text">{gear.categoryName || "-"}</div>
                    </td>
                    <td>
                      <div className="admin-cell-text">{gear.makerName || "-"}</div>
                    </td>
                    <td className="admin-action-column">
                      <div className="admin-row-actions">
                        {gear.link ? (
                          <a
                            href={gear.link}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="gear-link-icon"
                            title={gear.link}
                          >
                            ↗
                          </a>
                        ) : (
                          "-"
                        )}
                      </div>
                    </td>
                    <td className="admin-action-column">
                      <div className="admin-row-actions">
                        {gear.imageUrl ? (
                          <img
                            src={gear.imageUrl}
                            alt={gear.name}
                            className="gear-thumbnail"
                            onError={(e) => (e.target.style.display = "none")}
                          />
                        ) : (
                          "-"
                        )}
                      </div>
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
                          onClick={() => openEditGearModal(gear)}
                        />
                      </div>
                    </td>
                    <td className="admin-action-column">
                      <div className="admin-row-actions">
                        <ActionIconButton
                          icon={deleteIcon}
                          label="삭제"
                          onClick={() => deleteGear(gear.id)}
                        />
                      </div>
                    </td>
                  </tr>
                ))}

                <tr className="admin-add-row">
                  <td colSpan={13}>
                    <button
                      type="button"
                      className="admin-add-row-button"
                      onClick={openCreateGearModal}
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
