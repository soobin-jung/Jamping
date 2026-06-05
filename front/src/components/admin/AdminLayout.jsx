import { ADMIN_MENU } from "../../constants/admin";
import { TopProfileBar } from "../common/TopProfileBar";

/**
 * 관리자 공통 사이드바와 상단 바를 묶는 레이아웃입니다.
 */
export function AdminLayout({ loading, me, activeMenu, activeTitle, onNavigate, onLogout, children }) {
  if (loading) {
    return (
      <main className="admin-shell">
        <section className="admin-content-shell">
          <section className="admin-content">
            <section className="admin-empty-state">
              <h2>관리자 화면을 준비하고 있습니다</h2>
              <p>로그인과 권한 상태를 확인하는 중입니다.</p>
            </section>
          </section>
        </section>
      </main>
    );
  }

  if (!me || me.role !== "ADMIN") {
    return null;
  }

  return (
    <main className="admin-shell">
      <aside className="admin-sidebar">
        <button type="button" className="admin-brand" onClick={() => onNavigate("/admin")}>
          <span className="admin-brand-mark"></span>
          <div>
            <strong>Jamping Admin</strong>
            <span>운영자만 접근 가능합니다</span>
          </div>
        </button>

        <nav className="admin-nav">
          <button
            type="button"
            className={`admin-nav-item ${activeMenu === ADMIN_MENU.dashboard ? "active" : ""}`}
            onClick={() => onNavigate("/admin")}
          >
            대시보드
          </button>

          <section className="admin-nav-group">
            <p className="admin-nav-group-title">장비 관리</p>
            <button
              type="button"
              className={`admin-nav-subitem ${activeMenu === ADMIN_MENU.categories ? "active" : ""}`}
              onClick={() => onNavigate("/admin/categories")}
            >
              카테고리 관리
            </button>
            <button
              type="button"
              className={`admin-nav-subitem ${activeMenu === ADMIN_MENU.makers ? "active" : ""}`}
              onClick={() => onNavigate("/admin/makers")}
            >
              메이커 관리
            </button>
            <button
              type="button"
              className={`admin-nav-subitem ${activeMenu === ADMIN_MENU.gears ? "active" : ""}`}
              onClick={() => onNavigate("/admin/gears")}
            >
              용품 관리
            </button>
          </section>
        </nav>
      </aside>

      <section className="admin-content-shell">
        <header className="admin-topbar">
          <div>
            <strong>{activeTitle}</strong>
          </div>

          <div className="admin-topbar-actions">
            <TopProfileBar
              links={[{ label: "메인", onClick: () => onNavigate("/") }]}
              showAppsButton
              onAppsClick={() => onNavigate("/admin")}
              onLogout={onLogout}
              nickname={me.nickname}
            />
          </div>
        </header>

        <section className="admin-content">{children}</section>
      </section>
    </main>
  );
}
