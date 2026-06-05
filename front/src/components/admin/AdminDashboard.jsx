/**
 * 관리자 메인 대시보드의 기본 플레이스홀더입니다.
 */
export function AdminDashboard({ nickname, role }) {
  return (
    <section className="admin-dashboard-grid">
      <article className="admin-card dashboard-placeholder">
        <div className="admin-card-header">
          <div>
            <p className="admin-section-kicker">Dashboard</p>
            <h2>관리자 대시보드</h2>
          </div>
        </div>
      </article>

      <article className="admin-card dashboard-mini-card">
        <span className="mini-card-label">장비 관리</span>
        <strong>카테고리 / 용품</strong>
        <p>좌측 메뉴에서 장비 관리 화면으로 바로 이동할 수 있습니다.</p>
      </article>

      <article className="admin-card dashboard-mini-card">
        <span className="mini-card-label">권한 상태</span>
        <strong>{nickname}</strong>
        <p>{role} 권한으로 로그인되어 있습니다.</p>
      </article>
    </section>
  );
}
