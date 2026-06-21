import { formatDate } from "../../utils/date";

export function RecipeDetailModal({ recipe, onClose, onEdit }) {
  return (
    <div className="modal-backdrop" onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className="recipe-detail-modal">
        <div className="recipe-detail-header">
          <div>
            <span className="recipe-detail-category">{recipe.recipeCategoryName}</span>
            <h2 className="recipe-detail-title">{recipe.name}</h2>
          </div>
          <button className="close-button" type="button" onClick={onClose}>×</button>
        </div>

        <div className="recipe-detail-body">
          <section className="recipe-detail-section">
            <h3 className="recipe-detail-section-title">재료</h3>
            <p className="recipe-detail-text">
              {recipe.ingredients || <span className="recipe-detail-empty">재료 정보가 없습니다.</span>}
            </p>
          </section>

          <section className="recipe-detail-section">
            <h3 className="recipe-detail-section-title">조리법</h3>
            <p className="recipe-detail-text">
              {recipe.instructions || <span className="recipe-detail-empty">조리법 정보가 없습니다.</span>}
            </p>
          </section>
        </div>

        <div className="recipe-detail-meta">
          <span>등록: {recipe.createdBy || "-"} · {formatDate(recipe.createdAt)}</span>
          {recipe.updatedBy && (
            <span>수정: {recipe.updatedBy} · {formatDate(recipe.updatedAt)}</span>
          )}
        </div>

        <div className="recipe-detail-footer">
          <button type="button" className="ghost-button" onClick={onClose}>닫기</button>
          <button type="button" className="primary-button" onClick={() => { onClose(); onEdit(recipe); }}>
            수정하기
          </button>
        </div>
      </div>
    </div>
  );
}
