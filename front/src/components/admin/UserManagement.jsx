import { formatDate } from "../../utils/date";
import cancelIcon from "../../assets/action-icons/cancel.svg";
import editIcon from "../../assets/action-icons/edit.svg";
import saveIcon from "../../assets/action-icons/save.svg";
import { ActionIconButton } from "../common/ActionIconButton";

const ROLE_OPTIONS = [
  { value: "", label: "전체 권한" },
  { value: "ADMIN", label: "관리자" },
  { value: "USER", label: "일반 유저" }
];

function getRoleLabel(role) {
  if (role === "ADMIN") {
    return "관리자";
  }

  if (role === "USER") {
    return "일반 유저";
  }

  return role || "-";
}

export function UserManagement({
  currentUserId,
  users,
  usersLoading,
  userSubmitting,
  userError,
  userEditor,
  searchInput,
  roleFilter,
  page,
  totalPages,
  totalElements,
  hasNext,
  hasPrevious,
  updateSearchInput,
  updateRoleFilter,
  submitSearch,
  goToPage,
  startUserEdit,
  cancelUserEdit,
  updateUserEditor,
  saveUserRole
}) {
  function renderEditorRow(user) {
    return (
      <tr key={user.id} className="admin-edit-row">
        <td>{user.id}</td>
        <td>
          <div className="admin-cell-text">{user.nickname || "-"}</div>
        </td>
        <td>
          <div className="admin-cell-text">{user.email || "-"}</div>
        </td>
        <td>
          <div className="admin-cell-text">{user.provider}</div>
        </td>
        <td>
          <select
            className="admin-inline-select"
            value={userEditor?.role ?? user.role}
            onChange={updateUserEditor}
            disabled={userSubmitting}
          >
            {ROLE_OPTIONS.filter((option) => option.value).map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </td>
        <td>
          <div className="admin-cell-text">{formatDate(user.createdAt)}</div>
        </td>
        <td>
          <div className="admin-cell-text">{formatDate(user.updatedAt)}</div>
        </td>
        <td className="admin-action-column">
          <div className="admin-row-actions">
            <ActionIconButton icon={saveIcon} label="저장" onClick={saveUserRole} disabled={userSubmitting} />
            <ActionIconButton icon={cancelIcon} label="취소" onClick={cancelUserEdit} disabled={userSubmitting} />
          </div>
        </td>
      </tr>
    );
  }

  return (
    <section className="admin-card admin-table-card">
      <div className="admin-card-header">
        <div>
          <h2>유저 관리</h2>
        </div>
      </div>

      <form className="admin-toolbar" onSubmit={submitSearch}>
        <div className="admin-search-field">
          <input
            type="text"
            value={searchInput}
            onChange={updateSearchInput}
            placeholder="닉네임 또는 이메일 검색"
            className="admin-search-input"
          />
        </div>

        <div className="admin-filter-field">
          <select className="admin-filter-select" value={roleFilter} onChange={updateRoleFilter}>
            {ROLE_OPTIONS.map((option) => (
              <option key={option.value || "all"} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </div>

        <button type="submit" className="admin-secondary-button">
          검색
        </button>
      </form>

      {userError && <p className="admin-error table-error">{userError}</p>}

      {usersLoading ? (
        <div className="admin-empty-inline">유저 목록을 불러오는 중입니다.</div>
      ) : (
        <>
          <div className="admin-table-meta">총 {totalElements}건</div>

          <div className="admin-table-wrap">
            <table className="admin-table user-table">
              <thead>
                <tr>
                  <th width={60}>ID</th>
                  <th width={160}>닉네임</th>
                  <th width={220}>이메일</th>
                  <th width={120}>로그인 경로</th>
                  <th width={120}>권한</th>
                  <th width={130}>가입일</th>
                  <th width={130}>수정일</th>
                  <th width={100} className="admin-action-column">
                    수정
                  </th>
                </tr>
              </thead>
              <tbody>
                {users.length === 0 && (
                  <tr>
                    <td colSpan={9} className="admin-empty-inline">
                      검색 조건에 맞는 유저가 없습니다.
                    </td>
                  </tr>
                )}

                {users.map((user) => {
                  const isEditing = userEditor?.id === user.id;
                  const isCurrentUser = currentUserId === user.id;

                  if (isEditing) {
                    return renderEditorRow(user);
                  }

                  return (
                    <tr key={user.id}>
                    <td>{user.id}</td>
                    <td>
                      <div className="admin-cell-text">{user.nickname || "-"}</div>
                    </td>
                      <td>
                        <div className="admin-cell-text">{user.email || "-"}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{user.provider}</div>
                      </td>
                      <td>
                        <span className={`admin-role-badge role-${String(user.role).toLowerCase()}`}>
                          {getRoleLabel(user.role)}
                        </span>
                      </td>
                      <td>
                        <div className="admin-cell-text">{formatDate(user.createdAt)}</div>
                      </td>
                      <td>
                        <div className="admin-cell-text">{formatDate(user.updatedAt)}</div>
                      </td>
                      <td className="admin-action-column">
                        <div className="admin-row-actions">
                          <ActionIconButton
                            icon={editIcon}
                            label={isCurrentUser ? "본인 계정" : "수정"}
                            onClick={() => startUserEdit(user)}
                            disabled={Boolean(userEditor) || isCurrentUser}
                          />
                        </div>
                      </td>
                    </tr>
                  );
                })}
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
