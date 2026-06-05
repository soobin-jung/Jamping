import { useEffect, useMemo, useRef, useState } from "react";

/**
 * 검색 가능한 단일 선택 드롭다운입니다.
 */
export function SearchableSelect({
  value,
  options,
  placeholder,
  searchPlaceholder,
  emptyMessage,
  disabled = false,
  onChange
}) {
  const containerRef = useRef(null);
  const [isOpen, setIsOpen] = useState(false);
  const [keyword, setKeyword] = useState("");

  /**
   * 현재 선택된 옵션을 계산합니다.
   */
  const selectedOption = useMemo(
    () => options.find((option) => String(option.value) === String(value)) ?? null,
    [options, value]
  );

  /**
   * 검색어 기준으로 옵션 목록을 필터링합니다.
   */
  const filteredOptions = useMemo(() => {
    const normalizedKeyword = keyword.trim().toLowerCase();

    if (!normalizedKeyword) {
      return options;
    }

    return options.filter((option) => option.label.toLowerCase().includes(normalizedKeyword));
  }, [keyword, options]);

  useEffect(() => {
    function handleClickOutside(event) {
      if (!containerRef.current?.contains(event.target)) {
        setIsOpen(false);
      }
    }

    window.addEventListener("mousedown", handleClickOutside);
    return () => window.removeEventListener("mousedown", handleClickOutside);
  }, []);

  useEffect(() => {
    if (!isOpen) {
      setKeyword("");
    }
  }, [isOpen]);

  /**
   * 옵션 선택 시 값을 상위로 전달하고 패널을 닫습니다.
   */
  function handleSelect(optionValue) {
    onChange(optionValue);
    setIsOpen(false);
  }

  return (
    <div ref={containerRef} className={`searchable-select ${disabled ? "disabled" : ""}`}>
      <button
        type="button"
        className="searchable-select-trigger"
        disabled={disabled}
        onClick={() => setIsOpen((previous) => !previous)}
      >
        <span>{selectedOption ? selectedOption.label : placeholder}</span>
        <span className="searchable-select-arrow">{isOpen ? "^" : "v"}</span>
      </button>

      {isOpen && !disabled && (
        <div className="searchable-select-panel">
          <input
            className="searchable-select-input"
            type="text"
            value={keyword}
            onChange={(event) => setKeyword(event.target.value)}
            placeholder={searchPlaceholder}
          />

          <div className="searchable-select-options">
            {filteredOptions.length === 0 ? (
              <div className="searchable-select-empty">{emptyMessage}</div>
            ) : (
              filteredOptions.map((option) => (
                <button
                  key={option.value}
                  type="button"
                  className={`searchable-select-option ${
                    String(option.value) === String(value) ? "selected" : ""
                  }`}
                  onClick={() => handleSelect(option.value)}
                >
                  {option.label}
                </button>
              ))
            )}
          </div>
        </div>
      )}
    </div>
  );
}
