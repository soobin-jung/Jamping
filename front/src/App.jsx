import { useEffect, useState } from "react";

const API_BASE_URL = "http://localhost:8080";

function App() {
  const [me, setMe] = useState(null);
  const [loading, setLoading] = useState(true);
  const [modalMode, setModalMode] = useState(null);

  /**
   * 현재 세션의 로그인 사용자 정보를 조회합니다.
   */
  async function loadMe() {
    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/me`, {
        credentials: "include"
      });

      if (!response.ok) {
        setMe(null);
        return;
      }

      const data = await response.json();
      setMe(data);
    } catch (error) {
      console.error("Failed to fetch current user", error);
      setMe(null);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadMe();
  }, []);

  /**
   * 로그인 또는 회원가입 모달을 엽니다.
   */
  function openModal(mode) {
    setModalMode(mode);
  }

  /**
   * 인증 모달을 닫습니다.
   */
  function closeModal() {
    setModalMode(null);
  }

  /**
   * 선택한 소셜 로그인 제공자 인증 페이지로 이동합니다.
   */
  function handleSocialLogin(provider) {
    window.location.href = `${API_BASE_URL}/oauth2/authorization/${provider}`;
  }

  /**
   * 현재 로그인 세션을 종료하고 화면 상태를 초기화합니다.
   */
  async function handleLogout() {
    try {
      await fetch(`${API_BASE_URL}/logout`, {
        method: "POST",
        credentials: "include"
      });
    } catch (error) {
      console.error("Failed to logout", error);
    } finally {
      setMe(null);
      setModalMode(null);
      setLoading(false);
    }
  }

  const isAuthenticated = !loading && Boolean(me);
  const modalTitle = modalMode === "signup" ? "회원가입" : "로그인";
  const modalDescription =
    modalMode === "signup"
      ? "원하는 소셜 계정으로 Jamping에 처음 가입해보세요."
      : "소셜 계정으로 간편하게 로그인하고 캠핑 팸을 시작해보세요.";

  return (
    <main className="app-shell">
      <header className="top-bar">
        <div className="brand-mark">Jamping</div>
        <div className="top-bar-actions">
          {loading ? (
            <span className="user-chip ghost-chip">확인 중...</span>
          ) : isAuthenticated ? (
            <div className="authenticated-actions">
              <span className="user-chip">{me.nickname}</span>
              <button type="button" className="ghost-button" onClick={handleLogout}>
                로그아웃
              </button>
            </div>
          ) : (
            <>
              <button type="button" className="ghost-button" onClick={() => openModal("login")}>
                로그인
              </button>
              <button type="button" className="primary-button" onClick={() => openModal("signup")}>
                회원가입
              </button>
            </>
          )}
        </div>
      </header>

      <section className="hero-card">
        <p className="eyebrow">Camping Crew Platform</p>
        <h1>Jamping</h1>
        <p className="description">
          캠핑 일정, 준비물, 식단, 추억까지 한 번에 관리하는 우리만의 캠핑 플랫폼
        </p>

        <div className="status-panel">
          <span className="status-label">현재 상태</span>
          {loading ? (
            <p className="status-value">로그인 상태를 확인하고 있어요.</p>
          ) : isAuthenticated ? (
            <div className="profile-box">
              <p className="status-value">{me.nickname}님, 반가워요.</p>
              <p className="status-subtle">{me.email || "이메일 정보 없음"}</p>
              <p className="status-subtle">provider: {me.provider}</p>
            </div>
          ) : (
            <p className="status-value">로그인 없이도 둘러볼 수 있어요.</p>
          )}
        </div>
      </section>

      {modalMode && (
        <div className="modal-backdrop" role="presentation" onClick={closeModal}>
          <section
            className="auth-modal"
            role="dialog"
            aria-modal="true"
            aria-labelledby="auth-modal-title"
            onClick={(event) => event.stopPropagation()}
          >
            <button type="button" className="close-button" onClick={closeModal} aria-label="닫기">
              ×
            </button>
            <p className="modal-kicker">Jamping Access</p>
            <h2 id="auth-modal-title">{modalTitle}</h2>
            <p className="modal-description">{modalDescription}</p>

            <div className="button-stack">
              <button
                type="button"
                className="naver-button"
                onClick={() => handleSocialLogin("naver")}
              >
                네이버로 계속하기
              </button>
              <button
                type="button"
                className="kakao-button"
                onClick={() => handleSocialLogin("kakao")}
              >
                카카오로 계속하기
              </button>
              <button
                type="button"
                className="google-button"
                onClick={() => handleSocialLogin("google")}
              >
                구글로 계속하기
              </button>
            </div>
          </section>
        </div>
      )}
    </main>
  );
}

export default App;
