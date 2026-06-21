import { formatDate } from "../../utils/date";
import cancelIcon from "../../assets/action-icons/cancel.svg";
import deleteIcon from "../../assets/action-icons/delete.svg";
import editIcon from "../../assets/action-icons/edit.svg";
import saveIcon from "../../assets/action-icons/save.svg";
import { ActionIconButton } from "../common/ActionIconButton";

/**
 * 관리자 메이커 관리 테이블과 검색, 페이지, 인라인 CRUD UI를 렌더링합니다.
 */
export function MakerManagement({
  makers,
  totalElements,
  makersLoading,
  makerSubmitting,
  makerError,
  makerEditor,
  makerSearchInput,
  page,
  totalPages,
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
}) {
  /**
   * 메이커 인라인 편집 행을 렌더링합니다.
   */
  function renderEditorRow(maker = null) {
    return (
      <tr key={maker ? maker.id : "new"} className="admin-edit-row">
        <td>{maker ? maker.id : "new"}</td>
        <td>
          <input
            className="admin-inline-input"
            name="name"
            value={makerEditor?.name ?? ""}
            onChange={updateMakerEditor}
            onKeyDown={handleMakerEditorKeyDown}
            onKeyUp={handleMakerEditorKeyUp}
            placeholder="메이커명"
            maxLength={100}
          />
        </td>
        <td>
          <input
            className="admin-inline-input"
            name="nameEng"
            value={makerEditor?.nameEng ?? ""}
            onChange={updateMakerEditor}
            onKeyDown={handleMakerEditorKeyDown}
            onKeyUp={handleMakerEditorKeyUp}
            placeholder="영문명"
            maxLength={100}
          />
        </td>
        <td>
          <input
            className="admin-inline-input"
            name="homepageUrl"
            value={makerEditor?.homepageUrl ?? ""}
            onChange={updateMakerEditor}
            onKeyDown={handleMakerEditorKeyDown}
            onKeyUp={handleMakerEditorKeyUp}
            placeholder="홈페이지 주소"
            maxLength={500}
          />
        </td>
        <td>
          <div className="admin-cell-text">{maker?.createdBy || "-"}</div>
        </td>
        <td>
          <div className="admin-cell-text">{formatDate(maker?.createdAt)}</div>
        </td>
        <td>
          <div className="admin-cell-text">{maker?.updatedBy || "-"}</div>
        </td>
        <td>
          <div className="admin-cell-text">{formatDate(maker?.updatedAt)}</div>
        </td>
        <td className="admin-action-column">
          <div className="admin-row-actions">
            <ActionIconButton icon={saveIcon} label="저장" onClick={saveMaker} disabled={makerSubmitting} />
            <ActionIconButton icon={cancelIcon} label="취소" onClick={cancelMakerEdit} disabled={makerSubmitting} />
          </div>
        </td>
        <td className="admin-action-column">
          {maker && (
            <div className="admin-row-actions">
              <ActionIconButton
                icon={deleteIcon}
                label="삭제"
                onClick={() => deleteMaker(maker.id)}
                disabled={makerSubmitting}
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
          <h2>메이커 관리</h2>
        </div>
      </div>

      <form
        className="admin-toolbar"
        onSubmit={(event) => {
          event.preventDefault();
          searchMakers();
        }}
      >
        <div className="admin-search-field">
          <input
            type="text"
            value={makerSearchInput}
            onChange={updateMakerSearchInput}
            onKeyDown={handleMakerSearchKeyDown}
            placeholder="메이커명 또는 영문명 검색"
            className="admin-search-input"
          />
        </div>
        <button type="submit" className="admin-secondary-button">
          검색
        </button>
      </form>

      {makerError && <p className="admin-error table-error">{makerError}</p>}

      {makersLoading ? (
        <div className="admin-empty-inline">메이커 목록을 불러오는 중입니다.</div>
      ) : (
        <>
          <div className="admin-table-meta">총 {totalElements}건</div>

          <div className="admin-table-wrap">
            <table className="admin-table">
              <thead>
                <tr>
                  <th width={50}>ID</th>
                  <th width={180}>메이커명</th>
                  <th width={180}>영문명</th>
                  <th width={100} className="admin-action-column">홈페이지</th>
                  <th width={120}>등록자</th>
                  <th width={140}>등록일</th>
                  <th width={120}>수정자</th>
                  <th width={140}>수정일</th>
                  <th width={100} className="admin-action-column">수정</th>
                  <th width={100} className="admin-action-column">삭제</th>
                </tr>
              </thead>
              <tbody>
                {makers.length === 0 && !makerEditor?.isNew && (
                  <tr>
                    <td colSpan={10} className="admin-empty-inline">
                      검색 조건에 맞는 메이커가 없습니다.
                    </td>
                  </tr>
                )}

                {makers.map((maker) => {
                  const isEditing = makerEditor && !makerEditor.isNew && makerEditor.id === maker.id;

                  if (isEditing) {
                    return renderEditorRow(maker);
                  }

                  return (
                    <tr key={maker.id}>
                      <td>{maker.id}</td>
                      <td>
                        <div className="admin-cell-text">{maker.name}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{maker.nameEng || "-"}</div>
                      </td>
                      <td className="admin-action-column">
                        <div className="admin-row-actions">
                          {maker.homepageUrl ? (
                            <a
                              href={maker.homepageUrl}
                              target="_blank"
                              rel="noopener noreferrer"
                              className="gear-link-icon"
                              title={maker.homepageUrl}
                            >
                              ↗
                            </a>
                          ) : (
                            "-"
                          )}
                        </div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{maker.createdBy || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{formatDate(maker.createdAt)}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{maker.updatedBy || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{formatDate(maker.updatedAt)}</div>
                      </td>
                      <td className="admin-action-column">
                        <div className="admin-row-actions">
                          <ActionIconButton
                            icon={editIcon}
                            label="수정"
                            onClick={() => startMakerEdit(maker)}
                            disabled={Boolean(makerEditor)}
                          />
                        </div>
                      </td>
                      <td className="admin-action-column">
                        <div className="admin-row-actions">
                          <ActionIconButton
                            icon={deleteIcon}
                            label="삭제"
                            onClick={() => deleteMaker(maker.id)}
                            disabled={Boolean(makerEditor)}
                          />
                        </div>
                      </td>
                    </tr>
                  );
                })}

                {makerEditor?.isNew && renderEditorRow()}

                <tr className="admin-add-row">
                  <td colSpan={10}>
                    <button
                      type="button"
                      className="admin-add-row-button"
                      onClick={startCreateMaker}
                      disabled={Boolean(makerEditor)}
                    >
                      + 메이커 추가
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
