function GridIcon() {
  return (
    <svg viewBox="0 0 24 24" aria-hidden="true" className="profile-bar-icon-svg">
      {[5, 12, 19].flatMap((x) =>
        [5, 12, 19].map((y) => <circle key={`${x}-${y}`} cx={x} cy={y} r="1.6" fill="currentColor" />)
      )}
    </svg>
  );
}

function LogoutIcon() {
  return (
    <svg viewBox="0 0 24 24" aria-hidden="true" className="profile-bar-icon-svg">
      <path
        d="M14 7L19 12L14 17M19 12H9M11 5H7.5C6.7 5 6 5.7 6 6.5V17.5C6 18.3 6.7 19 7.5 19H11"
        fill="none"
        stroke="currentColor"
        strokeWidth="1.9"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}

export function TopProfileBar({ links = [], showAppsButton = false, onAppsClick, onLogout, nickname = "" }) {
  const avatarLabel = nickname.trim().slice(0, 1).toUpperCase() || "J";

  return (
    <div className="profile-bar">
      {links.map((link) => (
        <button key={link.label} type="button" className="profile-bar-link" onClick={link.onClick}>
          {link.label}
        </button>
      ))}

      {showAppsButton && (
        <button type="button" className="profile-bar-icon-button" onClick={onAppsClick} aria-label="메뉴">
          <GridIcon />
        </button>
      )}

      {onLogout && (
        <button type="button" className="profile-bar-icon-button" onClick={onLogout} aria-label="로그아웃">
          <LogoutIcon />
        </button>
      )}

      <button type="button" className="profile-avatar-button" aria-label={`${nickname || "사용자"} 프로필`}>
        <span className="profile-avatar-core">{avatarLabel}</span>
      </button>
    </div>
  );
}
