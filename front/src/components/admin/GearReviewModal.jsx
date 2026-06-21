import { useEffect, useState } from "react";
import { API_BASE_URL } from "../../constants/admin";
import { extractApiData, extractErrorMessage } from "../../utils/http";
import { formatDate } from "../../utils/date";

const STATUS_OPTIONS = [
  { value: "", label: "전체" },
  { value: "ACTIVE", label: "활성" },
  { value: "INACTIVE", label: "비활성" }
];

export function GearReviewModal({ gear, onClose }) {
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  const [sort, setSort] = useState("createdAt");
  const [direction, setDirection] = useState("desc");
  const [statusFilter, setStatusFilter] = useState("");
  const [tooltip, setTooltip] = useState(null);

  useEffect(() => {
    fetchReviews({ nextPage: 0, nextSort: "createdAt", nextDirection: "desc", nextStatus: "" });
  }, [gear.id]);

  async function fetchReviews({
    nextPage = page,
    nextSort = sort,
    nextDirection = direction,
    nextStatus = statusFilter
  } = {}) {
    setLoading(true);
    setError("");
    try {
      const params = new URLSearchParams({ page: String(nextPage), size: "10", sort: nextSort, direction: nextDirection });
      if (nextStatus) params.set("status", nextStatus);

      const response = await fetch(`${API_BASE_URL}/admin/gears/${gear.id}/reviews?${params}`, {
        credentials: "include"
      });
      if (!response.ok) throw new Error("리뷰 목록을 불러오지 못했습니다.");
      const data = await extractApiData(response);
      setReviews(data.content ?? []);
      setPage(data.page ?? 0);
      setTotalPages(data.totalPages ?? 0);
      setTotalElements(data.totalElements ?? 0);
      setHasNext(Boolean(data.hasNext));
      setHasPrevious(Boolean(data.hasPrevious));
    } catch (err) {
      setError(err.message || "리뷰 목록을 불러오지 못했습니다.");
    } finally {
      setLoading(false);
    }
  }

  function handleSort(field) {
    const nextDirection = sort === field && direction === "desc" ? "asc" : "desc";
    setSort(field);
    setDirection(nextDirection);
    fetchReviews({ nextPage: 0, nextSort: field, nextDirection });
  }

  function handleStatusFilter(value) {
    setStatusFilter(value);
    fetchReviews({ nextPage: 0, nextStatus: value });
  }

  async function deactivateReview(reviewId) {
    const reason = window.prompt("비활성화 사유를 입력하세요. (최대 1000자)");
    if (reason === null) return;
    if (!reason.trim()) {
      alert("비활성화 사유를 입력해야 합니다.");
      return;
    }

    setSubmitting(true);
    setError("");
    try {
      const response = await fetch(
        `${API_BASE_URL}/admin/gears/${gear.id}/reviews/${reviewId}/deactivate`,
        {
          method: "PUT",
          credentials: "include",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ moderationReason: reason.trim() })
        }
      );
      if (!response.ok) {
        const msg = await extractErrorMessage(response, "비활성화하지 못했습니다.");
        throw new Error(msg);
      }
      await fetchReviews();
    } catch (err) {
      setError(err.message || "비활성화하지 못했습니다.");
    } finally {
      setSubmitting(false);
    }
  }

  async function activateReview(reviewId) {
    if (!window.confirm("이 리뷰를 다시 활성화할까요?")) return;

    setSubmitting(true);
    setError("");
    try {
      const response = await fetch(
        `${API_BASE_URL}/admin/gears/${gear.id}/reviews/${reviewId}/activate`,
        { method: "PUT", credentials: "include" }
      );
      if (!response.ok) {
        const msg = await extractErrorMessage(response, "활성화하지 못했습니다.");
        throw new Error(msg);
      }
      await fetchReviews();
    } catch (err) {
      setError(err.message || "활성화하지 못했습니다.");
    } finally {
      setSubmitting(false);
    }
  }

  async function goToPage(nextPage) {
    if (nextPage < 0 || nextPage >= totalPages || nextPage === page) return;
    await fetchReviews({ nextPage });
  }

  function SortIcon({ field }) {
    if (sort !== field) return <span className="sort-icon inactive">↕</span>;
    return <span className="sort-icon active">{direction === "asc" ? "↑" : "↓"}</span>;
  }

  return (
    <div className="modal-backdrop" onClick={(e) => e.target === e.currentTarget && onClose()} onMouseLeave={() => setTooltip(null)}>
      <div className="gear-review-modal">
        <div className="gear-review-modal-header">
          <div>
            <p className="admin-section-kicker">리뷰 관리</p>
            <h2 className="gear-review-modal-title">{gear.name}</h2>
          </div>
          <button className="close-button" type="button" onClick={onClose}>×</button>
        </div>

        <div className="review-toolbar">
          <div className="review-status-filter">
            {STATUS_OPTIONS.map((opt) => (
              <button
                key={opt.value}
                type="button"
                className={`review-filter-tab ${statusFilter === opt.value ? "active" : ""}`}
                onClick={() => handleStatusFilter(opt.value)}
              >
                {opt.label}
              </button>
            ))}
          </div>
          <span className="admin-table-meta">{totalElements}건</span>
        </div>

        {error && <p className="admin-error gear-review-error">{error}</p>}

        {loading ? (
          <div className="admin-empty-inline">리뷰 목록을 불러오는 중입니다.</div>
        ) : (
          <>
            <div className="gear-review-modal-body">
              <div className="admin-table-wrap">
                <table className="admin-table review-table">
                  <thead>
                    <tr>
                      <th width={50}>ID</th>
                      <th width={120}>작성자</th>
                      <th width={110}>
                        <button
                          type="button"
                          className="sort-header-button"
                          onClick={() => handleSort("rating")}
                        >
                          별점 <SortIcon field="rating" />
                        </button>
                      </th>
                      <th>내용</th>
                      <th width={90}>상태</th>
                      <th width={200}>비활성화 사유</th>
                      <th width={100}>처리자</th>
                      <th width={130}>
                        <button
                          type="button"
                          className="sort-header-button"
                          onClick={() => handleSort("createdAt")}
                        >
                          작성일 <SortIcon field="createdAt" />
                        </button>
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {reviews.length === 0 ? (
                      <tr>
                        <td colSpan={8} className="admin-empty-inline">
                          등록된 리뷰가 없습니다.
                        </td>
                      </tr>
                    ) : (
                      reviews.map((review) => (
                        <tr key={review.id}>
                          <td>{review.id}</td>
                          <td>
                            <div className="admin-cell-text">{review.nickname}</div>
                          </td>
                          <td>
                            <div className="admin-cell-text review-rating">
                              {"★".repeat(review.rating)}{"☆".repeat(5 - review.rating)}
                            </div>
                          </td>
                          <td>
                            <div className="admin-cell-text">
                              <span
                                className="review-content"
                                onMouseEnter={(e) => {
                                  const rect = e.currentTarget.getBoundingClientRect();
                                  setTooltip({ content: review.content, top: rect.bottom + 8, left: rect.left });
                                }}
                                onMouseLeave={() => setTooltip(null)}
                              >
                                {review.content}
                              </span>
                            </div>
                          </td>
                          <td>
                            <div className="admin-cell-text">
                              <button
                                type="button"
                                className={`admin-role-badge review-status-toggle ${
                                  review.status === "ACTIVE" ? "status-active" : "status-inactive"
                                }`}
                                onClick={() =>
                                  review.status === "ACTIVE"
                                    ? deactivateReview(review.id)
                                    : activateReview(review.id)
                                }
                                disabled={submitting}
                                title={review.status === "ACTIVE" ? "클릭하여 비활성화" : "클릭하여 활성화"}
                              >
                                {review.status === "ACTIVE" ? "활성" : "비활성"}
                              </button>
                            </div>
                          </td>
                          <td>
                            <div className="admin-cell-text">{review.moderationReason || "-"}</div>
                          </td>
                          <td>
                            <div className="admin-cell-text">{review.moderatedBy || "-"}</div>
                          </td>
                          <td>
                            <div className="admin-cell-text">{formatDate(review.createdAt)}</div>
                          </td>
                        </tr>
                      ))
                    )}
                  </tbody>
                </table>
              </div>
            </div>

            <div className="admin-pagination">
              <button
                type="button"
                className="admin-page-button"
                onClick={() => goToPage(page - 1)}
                disabled={!hasPrevious}
              >
                이전
              </button>
              <span className="admin-page-indicator">
                {totalPages === 0 ? 0 : page + 1} / {totalPages}
              </span>
              <button
                type="button"
                className="admin-page-button"
                onClick={() => goToPage(page + 1)}
                disabled={!hasNext}
              >
                다음
              </button>
            </div>
          </>
        )}
      </div>
      {tooltip && (
        <div
          className="review-content-tooltip"
          style={{ top: tooltip.top, left: tooltip.left }}
        >
          {tooltip.content}
        </div>
      )}
    </div>
  );
}
