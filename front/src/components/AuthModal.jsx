import { API_BASE_URL } from "../constants/admin";

/**
 * 소셜 로그인 시작을 담당하는 공용 인증 모달입니다.
 */
export function AuthModal({ onClose }) {
  /**
   * 선택한 제공자의 소셜 로그인 인증 페이지로 이동합니다.
   */
  function handleSocialLogin(provider) {
    window.location.href = `${API_BASE_URL}/oauth2/authorization/${provider}`;
  }

  return (
    <div className="modal-backdrop" role="presentation" onClick={onClose}>
      <section
        className="auth-modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="auth-modal-title"
        onClick={(event) => event.stopPropagation()}
      >
        <button type="button" className="close-button" onClick={onClose} aria-label="닫기">
          ×
        </button>

        <div className="modal-icon-badge" aria-hidden="true">
          <span className="tent-icon">△</span>
        </div>

        <h2 id="auth-modal-title">간편 시작하기</h2>
        <p className="modal-description">
          이미 가입한 계정이면 바로 로그인되고,
          <br />
          처음인 계정이면 자동으로 가입돼요.
        </p>

        <div className="button-stack auth-provider-stack">
          <button type="button" className="provider-button naver-button" onClick={() => handleSocialLogin("naver")}>
            <span className="provider-icon naver-icon">N</span>
            <span>네이버로 계속하기</span>
          </button>

          <button type="button" className="provider-button kakao-button" onClick={() => handleSocialLogin("kakao")}>
            <span className="provider-icon kakao-icon">◼</span>
            <span>카카오로 계속하기</span>
          </button>

          <button type="button" className="provider-button google-button" onClick={() => handleSocialLogin("google")}>
            <span className="provider-icon google-icon">G</span>
            <span>구글로 계속하기</span>
          </button>
        </div>

        <div className="modal-divider" />
        <p className="modal-footnote">
          계속 진행하면 점핑의 이용약관 및 개인정보처리방침에 동의하는 것으로 간주됩니다.
        </p>
      </section>
    </div>
  );
}
