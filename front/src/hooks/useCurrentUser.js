import { useEffect, useState } from "react";
import { API_BASE_URL } from "../constants/admin";
import { extractApiData } from "../utils/http";

/**
 * 현재 로그인한 사용자의 정보를 조회하고 보관합니다.
 */
export function useCurrentUser() {
  const [me, setMe] = useState(null);
  const [loading, setLoading] = useState(true);

  /**
   * 서버에서 현재 사용자 정보를 다시 읽어옵니다.
   */
  async function loadMe() {
    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/me`, {
        credentials: "include"
      });

      if (!response.ok) {
        setMe(null);
        return;
      }

      const data = await extractApiData(response);
      setMe(data);
    } catch (error) {
      console.error("Failed to fetch current user", error);
      setMe(null);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadMe();
  }, []);

  return {
    me,
    loading,
    setMe,
    reloadMe: loadMe
  };
}
