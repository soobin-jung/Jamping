import { AuthModal } from "./AuthModal";
import { TopProfileBar } from "./common/TopProfileBar";

/**
 * 비로그인 사용자도 접근 가능한 메인 홈 화면입니다.
 */
export function PublicHome({
  me,
  loading,
  isAuthModalOpen,
  onOpenAuthModal,
  onCloseAuthModal,
  onNavigate,
  onLogout
}) {
  const isAuthenticated = !loading && Boolean(me);

  return (
    <main className="app-shell">
      <header className="top-bar">
        <div className="brand-mark">Jamping</div>

        <div className="top-bar-actions">
          {loading ? (
            <span className="user-chip ghost-chip">확인 중...</span>
          ) : isAuthenticated ? (
            <TopProfileBar
              links={me.role === "ADMIN" ? [{ label: "관리", onClick: () => onNavigate("/admin") }] : []}
              showAppsButton
              onAppsClick={() => onNavigate("/")}
              onLogout={onLogout}
              nickname={me.nickname}
            />
          ) : (
            <button type="button" className="primary-button" onClick={onOpenAuthModal}>
              간편 시작하기
            </button>
          )}
        </div>
      </header>

      <section className="hero-card">
        <p className="eyebrow">Camping Crew Platform</p>
        <h1>Jamping</h1>
        <p className="description">캠핑 일정, 준비물, 식단, 추억까지 한 번에 관리하는 우리만의 캠핑 플랫폼</p>

        <div className="status-panel">
          <span className="status-label">현재 상태</span>
          {loading ? (
            <p className="status-value">로그인 상태를 확인하고 있어요.</p>
          ) : isAuthenticated ? (
            <div className="profile-box">
              <p className="status-value">{me.nickname}님, 반가워요.</p>
              <p className="status-subtle">{me.email || "이메일 정보 없음"}</p>
              <p className="status-subtle">provider: {me.provider}</p>
              <p className="status-subtle">role: {me.role}</p>
            </div>
          ) : (
            <p className="status-value">로그인 없이도 자유롭게 둘러볼 수 있어요.</p>
          )}
        </div>
      </section>

      {isAuthModalOpen && <AuthModal onClose={onCloseAuthModal} />}
    </main>
  );
}
