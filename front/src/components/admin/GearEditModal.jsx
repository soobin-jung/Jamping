import { useState } from "react";
import { SearchableSelect } from "../common/SearchableSelect";

export function GearEditModal({
  gear,
  categoryOptions,
  makerOptions,
  submitting,
  error,
  onSave,
  onClose
}) {
  const isNew = !gear?.id;

  const [form, setForm] = useState({
    name: gear?.name ?? "",
    link: gear?.link ?? "",
    imageUrl: gear?.imageUrl ?? "",
    categoryId: gear?.categoryId ? String(gear.categoryId) : "",
    makerId: gear?.makerId ? String(gear.makerId) : "",
    memo: gear?.memo ?? ""
  });

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  function handleSelect(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }));
  }

  return (
    <div className="modal-backdrop" onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className="gear-edit-modal">
        <div className="gear-edit-modal-header">
          <h2>{isNew ? "용품 추가" : "용품 수정"}</h2>
          <button className="close-button" type="button" onClick={onClose}>
            ×
          </button>
        </div>

        <div className="gear-edit-form">
          {form.imageUrl && (
            <div className="gear-edit-image-preview">
              <img src={form.imageUrl} alt="이미지 미리보기" onError={(e) => (e.target.style.display = "none")} />
            </div>
          )}

          {error && <p className="admin-error">{error}</p>}

          <div className="gear-edit-field">
            <label className="gear-edit-label">용품명 *</label>
            <input
              name="name"
              value={form.name}
              onChange={handleChange}
              maxLength={100}
              placeholder="용품명을 입력하세요"
            />
          </div>

          <div className="gear-edit-row">
            <div className="gear-edit-field">
              <label className="gear-edit-label">카테고리 *</label>
              <SearchableSelect
                value={form.categoryId}
                options={categoryOptions}
                placeholder="카테고리 선택"
                searchPlaceholder="카테고리 검색"
                emptyMessage="카테고리가 없습니다."
                onChange={(v) => handleSelect("categoryId", v)}
              />
            </div>
            <div className="gear-edit-field">
              <label className="gear-edit-label">메이커 *</label>
              <SearchableSelect
                value={form.makerId}
                options={makerOptions}
                placeholder="메이커 선택"
                searchPlaceholder="메이커 검색"
                emptyMessage="메이커가 없습니다."
                onChange={(v) => handleSelect("makerId", v)}
              />
            </div>
          </div>

          <div className="gear-edit-field">
            <label className="gear-edit-label">링크</label>
            <input
              name="link"
              value={form.link}
              onChange={handleChange}
              maxLength={500}
              placeholder="https://"
            />
          </div>

          <div className="gear-edit-field">
            <label className="gear-edit-label">이미지 URL</label>
            <input
              name="imageUrl"
              value={form.imageUrl}
              onChange={handleChange}
              maxLength={1000}
              placeholder="https://"
            />
          </div>

          <div className="gear-edit-field">
            <label className="gear-edit-label">메모</label>
            <textarea
              name="memo"
              value={form.memo}
              onChange={handleChange}
              maxLength={1000}
              placeholder="메모를 입력하세요"
              rows={3}
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
