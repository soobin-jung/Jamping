import { DISTRICT_MAP, REGION_OPTIONS, getDistrictLabel, getRegionLabel } from "../../constants/region";
import deleteIcon from "../../assets/action-icons/delete.svg";
import editIcon from "../../assets/action-icons/edit.svg";
import { ActionIconButton } from "../common/ActionIconButton";
import { CampSiteEditModal } from "./CampSiteEditModal";

export function CampSiteManagement({
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
}) {
  const filterDistrictOptions = selectedRegionCode ? (DISTRICT_MAP[selectedRegionCode] ?? []) : [];

  return (
    <section className="admin-card admin-table-card">
      {campSiteModal && (
        <CampSiteEditModal
          campSite={campSiteModal.isNew ? null : campSiteModal}
          submitting={campSiteSubmitting}
          error={campSiteError}
          onSave={saveCampSite}
          onClose={closeCampSiteModal}
        />
      )}

      <div className="admin-card-header">
        <div>
          <h2>사이트 관리</h2>
        </div>
      </div>

      <form
        className="admin-toolbar"
        onSubmit={(e) => { e.preventDefault(); applyCampSiteFilters(); }}
      >
        <div className="admin-search-field">
          <select
            className="admin-search-input"
            value={selectedRegionCode}
            onChange={(e) => changeSelectedRegionCode(e.target.value)}
          >
            <option value="">전체 지역구</option>
            {REGION_OPTIONS.map((r) => (
              <option key={r.value} value={r.value}>{r.label}</option>
            ))}
          </select>
        </div>
        <div className="admin-search-field">
          <select
            className="admin-search-input"
            value={selectedDistrictCode}
            onChange={(e) => changeSelectedDistrictCode(e.target.value)}
            disabled={!selectedRegionCode}
          >
            <option value="">전체 자치구</option>
            {filterDistrictOptions.map((d) => (
              <option key={d.value} value={d.value}>{d.label}</option>
            ))}
          </select>
        </div>
        <div className="admin-search-field">
          <input
            className="admin-search-input"
            type="text"
            value={selectedKeyword}
            onChange={(e) => changeSelectedKeyword(e.target.value)}
            placeholder="캠핑장명 검색"
            maxLength={100}
          />
        </div>
        <button type="submit" className="admin-secondary-button">검색</button>
        <button type="button" className="admin-primary-button" onClick={openCreateCampSiteModal}>
          + 캠핑장 추가
        </button>
      </form>

      {campSiteError && !campSiteModal && <p className="admin-error table-error">{campSiteError}</p>}

      {campSitesLoading ? (
        <div className="admin-empty-inline">캠핑장 목록을 불러오는 중입니다.</div>
      ) : (
        <>
          <div className="admin-table-meta">총 {totalElements}건</div>

          <div className="admin-table-wrap">
            <table className="admin-table">
              <thead>
                <tr>
                  <th width={50}>ID</th>
                  <th>캠핑장명</th>
                  <th width={130}>지역구</th>
                  <th width={110}>자치구</th>
                  <th width={80} className="admin-action-column">입실</th>
                  <th width={80} className="admin-action-column">퇴실</th>
                  <th width={60} className="admin-action-column">링크</th>
                  <th width={80} className="admin-action-column">수정</th>
                  <th width={80} className="admin-action-column">삭제</th>
                </tr>
              </thead>
              <tbody>
                {campSites.length === 0 && (
                  <tr>
                    <td colSpan={9} className="admin-empty-inline">조회된 캠핑장이 없습니다.</td>
                  </tr>
                )}

                {campSites.map((campSite) => (
                  <tr key={campSite.id}>
                    <td>{campSite.id}</td>
                    <td>
                      <div className="admin-cell-text">{campSite.name}</div>
                    </td>
                    <td>
                      <div className="admin-cell-text">{getRegionLabel(campSite.regionCode)}</div>
                    </td>
                    <td>
                      <div className="admin-cell-text">{getDistrictLabel(campSite.regionCode, campSite.districtCode)}</div>
                    </td>
                    <td className="admin-action-column">
                      <div className="admin-cell-text">{campSite.checkInTime}</div>
                    </td>
                    <td className="admin-action-column">
                      <div className="admin-cell-text">{campSite.checkOutTime}</div>
                    </td>
                    <td className="admin-action-column">
                      <div className="admin-row-actions">
                        {campSite.link ? (
                          <a
                            href={campSite.link}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="gear-link-icon"
                            title={campSite.link}
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
                        <ActionIconButton
                          icon={editIcon}
                          label="수정"
                          onClick={() => openEditCampSiteModal(campSite)}
                        />
                      </div>
                    </td>
                    <td className="admin-action-column">
                      <div className="admin-row-actions">
                        <ActionIconButton
                          icon={deleteIcon}
                          label="삭제"
                          onClick={() => deleteCampSite(campSite.id)}
                        />
                      </div>
                    </td>
                  </tr>
                ))}

                <tr className="admin-add-row">
                  <td colSpan={9}>
                    <button type="button" className="admin-add-row-button" onClick={openCreateCampSiteModal}>
                      + 캠핑장 추가
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          {totalPages > 1 && (
            <div className="admin-pagination">
              <button
                type="button"
                className="admin-page-button"
                onClick={() => goToPage(page - 1)}
                disabled={!hasPrevious}
              >
                이전
              </button>
              <span className="admin-page-info">{page + 1} / {totalPages}</span>
              <button
                type="button"
                className="admin-page-button"
                onClick={() => goToPage(page + 1)}
                disabled={!hasNext}
              >
                다음
              </button>
            </div>
          )}
        </>
      )}
    </section>
  );
}
