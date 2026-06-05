/**
 * 실패 응답에서 사용자에게 보여줄 메시지를 추출합니다.
 */
export async function extractErrorMessage(response, fallbackMessage) {
  try {
    const data = await response.json();
    return data.detail || data.message || fallbackMessage;
  } catch (error) {
    return fallbackMessage;
  }
}

/**
 * ApiRes<T> 형식 응답에서 실제 data 필드를 꺼냅니다.
 */
export async function extractApiData(response) {
  const payload = await response.json();
  return payload?.data ?? null;
}
