export function ActionIconButton({ icon, label, onClick, disabled = false, type = "button" }) {
  return (
    <button
      type={type}
      className="admin-icon-button"
      onClick={onClick}
      disabled={disabled}
      aria-label={label}
      title={label}
    >
      <img src={icon} alt="" className="admin-icon-button-image" />
    </button>
  );
}
