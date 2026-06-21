import { useState } from "react";
import { DISTRICT_MAP, REGION_OPTIONS } from "../../constants/region";

export function CampSiteEditModal({ campSite, submitting, error, onSave, onClose }) {
  const isNew = !campSite?.id;

  const [form, setForm] = useState({
    name: campSite?.name ?? "",
    link: campSite?.link ?? "",
    regionCode: campSite?.regionCode ?? "",
    districtCode: campSite?.districtCode ?? "",
    checkInTime: campSite?.checkInTime ?? "14:00",
    checkOutTime: campSite?.checkOutTime ?? "11:00"
  });

  const districtOptions = form.regionCode ? (DISTRICT_MAP[form.regionCode] ?? []) : [];

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  function handleRegionChange(e) {
    setForm((prev) => ({ ...prev, regionCode: e.target.value, districtCode: "" }));
  }

  return (
    <div className="modal-backdrop" onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className="gear-edit-modal">
        <div className="gear-edit-modal-header">
          <h2>{isNew ? "캠핑장 추가" : "캠핑장 수정"}</h2>
          <button className="close-button" type="button" onClick={onClose}>×</button>
        </div>

        <div className="gear-edit-form">
          {error && <p className="admin-error">{error}</p>}

          <div className="gear-edit-field">
            <label className="gear-edit-label">캠핑장명 *</label>
            <input
              name="name"
              value={form.name}
              onChange={handleChange}
              maxLength={100}
              placeholder="캠핑장명을 입력하세요"
            />
          </div>

          <div className="gear-edit-row">
            <div className="gear-edit-field">
              <label className="gear-edit-label">지역구 *</label>
              <select name="regionCode" value={form.regionCode} onChange={handleRegionChange}>
                <option value="">지역구 선택</option>
                {REGION_OPTIONS.map((r) => (
                  <option key={r.value} value={r.value}>{r.label}</option>
                ))}
              </select>
            </div>
            <div className="gear-edit-field">
              <label className="gear-edit-label">자치구 *</label>
              <select
                name="districtCode"
                value={form.districtCode}
                onChange={handleChange}
                disabled={!form.regionCode}
              >
                <option value="">자치구 선택</option>
                {districtOptions.map((d) => (
                  <option key={d.value} value={d.value}>{d.label}</option>
                ))}
              </select>
            </div>
          </div>

          <div className="gear-edit-row">
            <div className="gear-edit-field">
              <label className="gear-edit-label">입실 시간 *</label>
              <input
                type="time"
                name="checkInTime"
                value={form.checkInTime}
                onChange={handleChange}
              />
            </div>
            <div className="gear-edit-field">
              <label className="gear-edit-label">퇴실 시간 *</label>
              <input
                type="time"
                name="checkOutTime"
                value={form.checkOutTime}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="gear-edit-field">
            <label className="gear-edit-label">링크</label>
            <input
              name="link"
              value={form.link}
              onChange={handleChange}
              maxLength={1000}
              placeholder="https://"
            />
          </div>
        </div>

        <div className="gear-edit-modal-footer">
          <button type="button" className="ghost-button" onClick={onClose} disabled={submitting}>
            취소
          </button>
          <button type="button" className="primary-button" onClick={() => onSave(form)} disabled={submitting}>
            {submitting ? "저장 중..." : "저장"}
          </button>
        </div>
      </div>
    </div>
  );
}
