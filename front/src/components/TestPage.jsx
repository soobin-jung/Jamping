import { useEffect, useState } from "react";
import { API_BASE_URL } from "../constants/admin";

const S = {
  page: { maxWidth: 780, margin: "0 auto", padding: "24px 16px", fontFamily: "-apple-system, sans-serif" },
  topbar: { background: "#1a1a2e", color: "white", padding: "14px 24px", display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 0 },
  card: { background: "white", borderRadius: 10, boxShadow: "0 1px 4px rgba(0,0,0,.08)", padding: 20, marginBottom: 20 },
  cardTitle: { fontSize: 15, fontWeight: 700, color: "#1a1a2e", marginBottom: 16, paddingBottom: 10, borderBottom: "2px solid #f0f2f5", display: "flex", justifyContent: "space-between", alignItems: "center" },
  label: { display: "block", fontSize: 13, color: "#555", marginBottom: 4, marginTop: 12 },
  input: { width: "100%", padding: "9px 12px", border: "1px solid #ddd", borderRadius: 6, fontSize: 14, outline: "none", boxSizing: "border-box" },
  row: { display: "flex", gap: 8, alignItems: "flex-end" },
  btn: (color) => ({ padding: "9px 18px", border: "none", borderRadius: 6, cursor: "pointer", fontSize: 14, fontWeight: 600, background: color, color: "white" }),
  btnSm: (color) => ({ padding: "4px 10px", border: "none", borderRadius: 4, cursor: "pointer", fontSize: 12, fontWeight: 600, background: color, color: "white" }),
  msgOk: { marginTop: 10, padding: "8px 12px", borderRadius: 6, fontSize: 13, background: "#d4edda", color: "#155724" },
  msgErr: { marginTop: 10, padding: "8px 12px", borderRadius: 6, fontSize: 13, background: "#f8d7da", color: "#721c24" },
  table: { width: "100%", borderCollapse: "collapse", fontSize: 13 },
  th: { background: "#f8f9fa", padding: "8px 10px", textAlign: "left", color: "#555", fontWeight: 600, borderBottom: "2px solid #eee" },
  td: { padding: "9px 10px", borderBottom: "1px solid #f0f0f0", verticalAlign: "middle" },
  chip: { display: "inline-flex", alignItems: "center", gap: 4, background: "#e8f0fe", color: "#1a56db", borderRadius: 14, padding: "3px 10px", fontSize: 13, margin: 2 },
  infoBox: { background: "#f8f9fa", borderRadius: 6, padding: 12, fontSize: 13, color: "#555" },
  empty: { textAlign: "center", color: "#aaa", padding: 20, fontSize: 13 },
};

const BADGE = {
  PENDING: { background: "#fff3cd", color: "#856404" },
  ACCEPTED: { background: "#d4edda", color: "#155724" },
  REJECTED: { background: "#f8d7da", color: "#721c24" },
  EXPIRED: { background: "#e2e3e5", color: "#383d41" },
};

async function api(method, url, body) {
  const opts = { method, headers: { "Content-Type": "application/json" }, credentials: "include" };
  if (body) opts.body = JSON.stringify(body);
  try {
    const res = await fetch(`${API_BASE_URL}${url}`, opts);
    const json = await res.json().catch(() => ({}));
    return { ok: res.ok, data: json };
  } catch {
    return { ok: false, data: {} };
  }
}

function fmt(dt) {
  if (!dt) return "-";
  return new Date(dt).toLocaleString("ko-KR", { month: "2-digit", day: "2-digit", hour: "2-digit", minute: "2-digit" });
}

export function TestPage({ me, loading }) {
  const [campsites, setCampsites] = useState([]);
  const [manualCampSiteId, setManualCampSiteId] = useState("");
  const [famName, setFamName] = useState("");
  const [reservationSites, setReservationSites] = useState("");
  const [currentFam, setCurrentFam] = useState(null);

  const [emailInput, setEmailInput] = useState("");
  const [pendingEmails, setPendingEmails] = useState([]);

  const [invitations, setInvitations] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [tokenInput, setTokenInput] = useState("");

  const [msgs, setMsgs] = useState({});

  useEffect(() => {
    if (me) {
      loadCampsites();
      loadNotifications();
    }
  }, [me]);

  function showMsg(key, text, ok) {
    setMsgs(m => ({ ...m, [key]: { text, ok } }));
    setTimeout(() => setMsgs(m => { const n = { ...m }; delete n[key]; return n; }), 5000);
  }

  function Msg({ k }) {
    const m = msgs[k];
    if (!m) return null;
    return <div style={m.ok ? S.msgOk : S.msgErr}>{m.text}</div>;
  }

  async function loadCampsites() {
    const { ok, data } = await api("GET", "/api/campsites?size=100");
    setCampsites(ok ? (data?.data?.content || []) : []);
  }

  async function createFam() {
    const campSiteId = campsites.length > 0
      ? Number(document.getElementById("campsite-sel").value)
      : Number(manualCampSiteId);
    if (!famName || !campSiteId || !reservationSites) {
      showMsg("fam", "모든 항목을 입력해주세요.", false); return;
    }
    const { ok, data } = await api("POST", "/camping-fams", { name: famName, campSiteId, reservationSites });
    if (!ok) { showMsg("fam", data.message || "생성 실패", false); return; }
    setCurrentFam({ id: data.resultId, name: famName, reservationSites });
    showMsg("fam", `생성 완료! (ID: ${data.resultId})`, true);
    loadInvitations(data.resultId);
  }

  function addEmail() {
    const email = emailInput.trim();
    if (!email || !/\S+@\S+\.\S+/.test(email)) { alert("올바른 이메일을 입력해주세요."); return; }
    if (pendingEmails.includes(email)) { alert("이미 추가된 이메일입니다."); return; }
    setPendingEmails(p => [...p, email]);
    setEmailInput("");
  }

  async function sendInvites() {
    if (!pendingEmails.length) { showMsg("invite", "이메일을 추가해주세요.", false); return; }
    const { ok, data } = await api("POST", `/camping-fams/${currentFam.id}/invitations`, { emails: pendingEmails });
    if (!ok) { showMsg("invite", data.message || "초대 실패", false); return; }
    showMsg("invite", `${pendingEmails.length}명 초대 완료!`, true);
    setPendingEmails([]);
    loadInvitations(currentFam.id);
  }

  async function loadInvitations(famId) {
    const id = famId ?? currentFam?.id;
    if (!id) return;
    const { ok, data } = await api("GET", `/camping-fams/${id}/invitations`);
    setInvitations(ok ? (data?.data || []) : []);
  }

  async function loadNotifications() {
    const [notiRes, countRes] = await Promise.all([
      api("GET", "/notifications"),
      api("GET", "/notifications/unread-count"),
    ]);
    setNotifications(notiRes.data?.data || []);
    setUnreadCount(countRes.data?.data ?? 0);
  }

  async function markRead(id) {
    await api("PATCH", `/notifications/${id}/read`);
    loadNotifications();
  }

  async function doAccept(token) {
    const { ok, data } = await api("POST", `/camping-fams/invitations/${token}/accept`);
    alert(ok ? "✅ 수락 완료!" : "❌ " + (data.message || "실패"));
    if (ok) { loadInvitations(); loadNotifications(); }
  }

  async function doReject(token) {
    const { ok, data } = await api("POST", `/camping-fams/invitations/${token}/reject`);
    alert(ok ? "거절 완료" : "❌ " + (data.message || "실패"));
    if (ok) loadInvitations();
  }

  async function acceptByToken() {
    if (!tokenInput) { showMsg("token", "토큰을 입력해주세요.", false); return; }
    const { ok, data } = await api("POST", `/camping-fams/invitations/${tokenInput}/accept`);
    showMsg("token", ok ? "✅ 캠핑팸에 참여했습니다!" : (data.message || "실패"), ok);
    if (ok) { loadNotifications(); loadInvitations(); }
  }

  async function rejectByToken() {
    if (!tokenInput) { showMsg("token", "토큰을 입력해주세요.", false); return; }
    const { ok, data } = await api("POST", `/camping-fams/invitations/${tokenInput}/reject`);
    showMsg("token", ok ? "거절했습니다." : (data.message || "실패"), ok);
    if (ok) loadInvitations();
  }

  function copyToken(token) {
    navigator.clipboard.writeText(token).then(() => alert("토큰 복사 완료!\n\n" + token));
  }

  if (loading) return <div style={{ textAlign: "center", padding: 60, color: "#888" }}>로딩 중...</div>;

  if (!me) {
    return (
      <>
        <div style={S.topbar}><span style={{ fontWeight: 700, fontSize: 18 }}>🏕️ Jamping Demo</span></div>
        <div style={{ ...S.page, textAlign: "center", paddingTop: 60 }}>
          <div style={S.card}>
            <div style={{ fontSize: 22, fontWeight: 700, marginBottom: 8 }}>로그인이 필요합니다</div>
            <p style={{ color: "#888", marginBottom: 28, fontSize: 14 }}>소셜 계정으로 로그인 후 데모를 사용할 수 있습니다.</p>
            <div style={{ display: "flex", gap: 12, justifyContent: "center", flexWrap: "wrap" }}>
              <a href={`${API_BASE_URL}/oauth2/authorization/google`}><button style={{ ...S.btn("#fff"), color: "#333", border: "1px solid #ddd", minWidth: 140 }}>Google 로그인</button></a>
              <a href={`${API_BASE_URL}/oauth2/authorization/naver`}><button style={{ ...S.btn("#03c75a"), minWidth: 140 }}>Naver 로그인</button></a>
              <a href={`${API_BASE_URL}/oauth2/authorization/kakao`}><button style={{ ...S.btn("#fee500"), color: "#333", minWidth: 140 }}>Kakao 로그인</button></a>
            </div>
          </div>
        </div>
      </>
    );
  }

  return (
    <>
      <div style={S.topbar}>
        <span style={{ fontWeight: 700, fontSize: 18 }}>🏕️ Jamping Demo</span>
        <div style={{ display: "flex", alignItems: "center", gap: 12, fontSize: 14 }}>
          <span>{me.nickname}</span>
          <form method="POST" action={`${API_BASE_URL}/logout`} style={{ display: "inline" }}>
            <button type="submit" style={{ background: "transparent", border: "1px solid #666", color: "#ccc", padding: "4px 12px", borderRadius: 4, cursor: "pointer", fontSize: 13 }}>
              로그아웃
            </button>
          </form>
        </div>
      </div>

      <div style={{ background: "#f0f2f5", minHeight: "100vh", paddingBottom: 40 }}>
        <div style={S.page}>

          {/* 내 정보 */}
          <div style={S.card}>
            <div style={S.cardTitle}>내 정보</div>
            <div style={S.infoBox}>
              <strong>닉네임:</strong> {me.nickname} &nbsp;|&nbsp;
              <strong>이메일:</strong> {me.email || "없음"} &nbsp;|&nbsp;
              <strong>역할:</strong> {me.role} &nbsp;|&nbsp;
              <strong>userId:</strong> {me.userId}
            </div>
          </div>

          {/* 캠핑팸 만들기 or 현재 팸 */}
          {!currentFam ? (
            <div style={S.card}>
              <div style={S.cardTitle}>캠핑팸 만들기</div>
              <label style={S.label}>팸 이름</label>
              <input style={S.input} value={famName} onChange={e => setFamName(e.target.value)} placeholder="ex. 여름 캠핑팸" />
              <label style={S.label}>캠핑장</label>
              {campsites.length > 0 ? (
                <select id="campsite-sel" style={S.input}>
                  {campsites.map(cs => <option key={cs.id} value={cs.id}>[{cs.id}] {cs.name}</option>)}
                </select>
              ) : (
                <input style={S.input} value={manualCampSiteId} onChange={e => setManualCampSiteId(e.target.value)} placeholder="campSiteId 직접 입력" />
              )}
              <label style={S.label}>예약 사이트</label>
              <input style={S.input} value={reservationSites} onChange={e => setReservationSites(e.target.value)} placeholder="ex. 네이버 예약 A-3구역" />
              <div style={{ marginTop: 14 }}>
                <button style={S.btn("#4a90d9")} onClick={createFam}>캠핑팸 생성 →</button>
              </div>
              <Msg k="fam" />
            </div>
          ) : (
            <div style={S.card}>
              <div style={S.cardTitle}>현재 캠핑팸 <span style={{ fontSize: 12, fontWeight: 400, color: "#888" }}>ID: {currentFam.id}</span></div>
              <div style={S.infoBox}>
                <strong>이름:</strong> {currentFam.name} &nbsp;|&nbsp;
                <strong>예약사이트:</strong> {currentFam.reservationSites}
              </div>
            </div>
          )}

          {/* 멤버 초대 */}
          {currentFam && (
            <div style={S.card}>
              <div style={S.cardTitle}>멤버 초대</div>
              <div style={S.row}>
                <div style={{ flex: 1 }}>
                  <label style={{ ...S.label, marginTop: 0 }}>이메일</label>
                  <input
                    style={S.input}
                    value={emailInput}
                    onChange={e => setEmailInput(e.target.value)}
                    onKeyDown={e => e.key === "Enter" && addEmail()}
                    placeholder="초대할 이메일"
                  />
                </div>
                <button style={{ ...S.btn("#6c757d"), alignSelf: "flex-end" }} onClick={addEmail}>+ 추가</button>
              </div>
              <div style={{ marginTop: 10, minHeight: 28 }}>
                {pendingEmails.map(email => (
                  <span key={email} style={S.chip}>
                    {email}
                    <button onClick={() => setPendingEmails(p => p.filter(e => e !== email))}
                      style={{ background: "none", border: "none", color: "#888", cursor: "pointer", fontSize: 14, lineHeight: 1, padding: 0 }}>×</button>
                  </span>
                ))}
              </div>
              <div style={{ marginTop: 12 }}>
                <button style={S.btn("#4a90d9")} onClick={sendInvites}>초대 발송</button>
              </div>
              <Msg k="invite" />
            </div>
          )}

          {/* 초대 현황 */}
          {currentFam && (
            <div style={S.card}>
              <div style={S.cardTitle}>
                초대 현황
                <button style={S.btnSm("#6c757d")} onClick={() => loadInvitations()}>새로고침</button>
              </div>
              <table style={S.table}>
                <thead>
                  <tr>
                    <th style={S.th}>이메일</th>
                    <th style={S.th}>상태</th>
                    <th style={S.th}>토큰</th>
                    <th style={S.th}>만료일</th>
                    <th style={S.th}>액션</th>
                  </tr>
                </thead>
                <tbody>
                  {invitations.length === 0
                    ? <tr><td colSpan={5} style={S.empty}>초대 내역 없음</td></tr>
                    : invitations.map(inv => (
                      <tr key={inv.id}>
                        <td style={S.td}>{inv.email}</td>
                        <td style={S.td}>
                          <span style={{ ...BADGE[inv.status], padding: "2px 8px", borderRadius: 10, fontSize: 11, fontWeight: 600 }}>
                            {inv.status}
                          </span>
                        </td>
                        <td style={S.td}>
                          <span style={{ fontFamily: "monospace", fontSize: 11, color: "#888", maxWidth: 120, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap", display: "inline-block" }} title={inv.token}>
                            {inv.token?.slice(0, 8)}...
                          </span>
                          <button onClick={() => copyToken(inv.token)} style={{ background: "none", border: "none", color: "#4a90d9", cursor: "pointer", fontSize: 12, marginLeft: 4 }}>복사</button>
                        </td>
                        <td style={S.td}>{fmt(inv.expiredAt)}</td>
                        <td style={S.td}>
                          {inv.status === "PENDING" ? (
                            <div style={{ display: "flex", gap: 4 }}>
                              <button style={S.btnSm("#28a745")} onClick={() => doAccept(inv.token)}>수락</button>
                              <button style={S.btnSm("#dc3545")} onClick={() => doReject(inv.token)}>거절</button>
                            </div>
                          ) : "-"}
                        </td>
                      </tr>
                    ))
                  }
                </tbody>
              </table>
            </div>
          )}

          {/* 내 알림 */}
          <div style={S.card}>
            <div style={S.cardTitle}>
              내 알림 {unreadCount > 0 && (
                <span style={{ background: "#dc3545", color: "white", fontSize: 11, fontWeight: 700, padding: "2px 7px", borderRadius: 10, marginLeft: 6 }}>
                  {unreadCount}
                </span>
              )}
              <button style={S.btnSm("#6c757d")} onClick={loadNotifications}>새로고침</button>
            </div>
            <table style={S.table}>
              <thead>
                <tr>
                  <th style={S.th}>메시지</th>
                  <th style={S.th}>시간</th>
                  <th style={S.th}>읽음</th>
                </tr>
              </thead>
              <tbody>
                {notifications.length === 0
                  ? <tr><td colSpan={3} style={S.empty}>알림 없음</td></tr>
                  : notifications.map(n => (
                    <tr key={n.id} style={n.isRead ? {} : { background: "#f0f7ff" }}>
                      <td style={S.td}>{n.message}</td>
                      <td style={S.td}>{fmt(n.createdAt)}</td>
                      <td style={S.td}>
                        {!n.isRead
                          ? <button style={S.btnSm("#6c757d")} onClick={() => markRead(n.id)}>읽음 처리</button>
                          : <span style={{ color: "#aaa", fontSize: 12 }}>읽음</span>}
                      </td>
                    </tr>
                  ))
                }
              </tbody>
            </table>
          </div>

          {/* 토큰 직접 입력 */}
          <div style={S.card}>
            <div style={S.cardTitle}>초대 수락 / 거절 <span style={{ fontSize: 12, fontWeight: 400, color: "#888" }}>토큰 직접 입력</span></div>
            <div style={S.row}>
              <div style={{ flex: 1 }}>
                <label style={{ ...S.label, marginTop: 0 }}>초대 토큰</label>
                <input style={S.input} value={tokenInput} onChange={e => setTokenInput(e.target.value)} placeholder="토큰을 붙여넣으세요" />
              </div>
              <button style={{ ...S.btn("#28a745"), alignSelf: "flex-end" }} onClick={acceptByToken}>수락</button>
              <button style={{ ...S.btn("#dc3545"), alignSelf: "flex-end" }} onClick={rejectByToken}>거절</button>
            </div>
            <Msg k="token" />
          </div>

        </div>
      </div>
    </>
  );
}
