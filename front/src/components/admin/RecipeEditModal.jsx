import { useState } from "react";
import { SearchableSelect } from "../common/SearchableSelect";

export function RecipeEditModal({
  recipe,
  recipeCategoryOptions,
  submitting,
  error,
  onSave,
  onClose
}) {
  const isNew = !recipe?.id;

  const [form, setForm] = useState({
    name: recipe?.name ?? "",
    ingredients: recipe?.ingredients ?? "",
    instructions: recipe?.instructions ?? "",
    recipeCategoryId: recipe?.recipeCategoryId ? String(recipe.recipeCategoryId) : ""
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
          <h2>{isNew ? "레시피 추가" : "레시피 수정"}</h2>
          <button className="close-button" type="button" onClick={onClose}>×</button>
        </div>

        <div className="gear-edit-form">
          {error && <p className="admin-error">{error}</p>}

          <div className="gear-edit-field">
            <label className="gear-edit-label">카테고리 *</label>
            <SearchableSelect
              value={form.recipeCategoryId}
              options={recipeCategoryOptions}
              placeholder="카테고리 선택"
              searchPlaceholder="카테고리 검색"
              emptyMessage="카테고리가 없습니다."
              onChange={(v) => handleSelect("recipeCategoryId", v)}
            />
          </div>

          <div className="gear-edit-field">
            <label className="gear-edit-label">레시피명 *</label>
            <input
              name="name"
              value={form.name}
              onChange={handleChange}
              maxLength={100}
              placeholder="레시피명을 입력하세요"
            />
          </div>

          <div className="gear-edit-field">
            <label className="gear-edit-label">재료</label>
            <textarea
              name="ingredients"
              value={form.ingredients}
              onChange={handleChange}
              placeholder={"김치 200g, 돼지고기 150g, 두부 1/2모 ..."}
              rows={4}
            />
          </div>

          <div className="gear-edit-field">
            <label className="gear-edit-label">조리법</label>
            <textarea
              name="instructions"
              value={form.instructions}
              onChange={handleChange}
              placeholder={"1. 김치를 적당히 썰어 ...\n2. 냄비에 기름을 두르고 ..."}
              rows={6}
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
