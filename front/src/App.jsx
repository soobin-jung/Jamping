import { useEffect, useMemo, useState } from "react";
import { PublicHome } from "./components/PublicHome";
import { AdminLayout } from "./components/admin/AdminLayout";
import { AdminDashboard } from "./components/admin/AdminDashboard";
import { CategoryManagement } from "./components/admin/CategoryManagement";
import { GearManagement } from "./components/admin/GearManagement";
import { MakerManagement } from "./components/admin/MakerManagement";
import { RecipeCategoryManagement } from "./components/admin/RecipeCategoryManagement";
import { RecipeManagement } from "./components/admin/RecipeManagement";
import { UserManagement } from "./components/admin/UserManagement";
import { CampSiteManagement } from "./components/admin/CampSiteManagement";
import { ADMIN_MENU, API_BASE_URL } from "./constants/admin";
import { useCategories } from "./hooks/useCategories";
import { useCurrentUser } from "./hooks/useCurrentUser";
import { useGears } from "./hooks/useGears";
import { useMakers } from "./hooks/useMakers";
import { useRecipeCategories } from "./hooks/useRecipeCategories";
import { useRecipes } from "./hooks/useRecipes";
import { useUsers } from "./hooks/useUsers";
import { useCampSites } from "./hooks/useCampSites";

function App() {
  const [pathname, setPathname] = useState(window.location.pathname);
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
  const { me, loading, setMe } = useCurrentUser();

  const isAdminPage = pathname.startsWith("/admin");

  const activeAdminMenu = useMemo(() => {
    if (pathname === "/admin" || pathname === "/admin/") {
      return ADMIN_MENU.dashboard;
    }

    if (pathname.startsWith("/admin/categories")) {
      return ADMIN_MENU.categories;
    }

    if (pathname.startsWith("/admin/users")) {
      return ADMIN_MENU.users;
    }

    if (pathname.startsWith("/admin/makers")) {
      return ADMIN_MENU.makers;
    }

    if (pathname.startsWith("/admin/gears")) {
      return ADMIN_MENU.gears;
    }

    if (pathname.startsWith("/admin/recipe-categories")) {
      return ADMIN_MENU.recipeCategories;
    }

    if (pathname.startsWith("/admin/recipes")) {
      return ADMIN_MENU.recipes;
    }

    if (pathname.startsWith("/admin/campsites")) {
      return ADMIN_MENU.campSites;
    }

    return ADMIN_MENU.dashboard;
  }, [pathname]);

  const activeAdminTitle = useMemo(() => {
    if (activeAdminMenu === ADMIN_MENU.users) {
      return "유저 관리 > 유저 관리";
    }

    if (activeAdminMenu === ADMIN_MENU.categories) {
      return "장비 관리 > 카테고리 관리";
    }

    if (activeAdminMenu === ADMIN_MENU.makers) {
      return "장비 관리 > 메이커 관리";
    }

    if (activeAdminMenu === ADMIN_MENU.gears) {
      return "장비 관리 > 용품 관리";
    }

    if (activeAdminMenu === ADMIN_MENU.recipeCategories) {
      return "레시피 관리 > 카테고리 관리";
    }

    if (activeAdminMenu === ADMIN_MENU.recipes) {
      return "레시피 관리 > 레시피 관리";
    }

    if (activeAdminMenu === ADMIN_MENU.campSites) {
      return "사이트 관리 > 캠핑장 관리";
    }

    return "대시보드";
  }, [activeAdminMenu]);

  const categoryState = useCategories({
    enabled: isAdminPage && activeAdminMenu === ADMIN_MENU.categories && me?.role === "ADMIN"
  });

  const userState = useUsers({
    enabled: isAdminPage && activeAdminMenu === ADMIN_MENU.users && me?.role === "ADMIN"
  });

  const makerState = useMakers({
    enabled: isAdminPage && activeAdminMenu === ADMIN_MENU.makers && me?.role === "ADMIN"
  });

  const gearState = useGears({
    enabled: isAdminPage && activeAdminMenu === ADMIN_MENU.gears && me?.role === "ADMIN"
  });

  const recipeCategoryState = useRecipeCategories({
    enabled: isAdminPage && activeAdminMenu === ADMIN_MENU.recipeCategories && me?.role === "ADMIN"
  });

  const recipeState = useRecipes({
    enabled: isAdminPage && activeAdminMenu === ADMIN_MENU.recipes && me?.role === "ADMIN"
  });

  const campSiteState = useCampSites({
    enabled: isAdminPage && activeAdminMenu === ADMIN_MENU.campSites && me?.role === "ADMIN"
  });

  /**
   * 브라우저 이동과 맞춰 현재 경로 상태를 동기화합니다.
   */
  useEffect(() => {
    const handlePopState = () => setPathname(window.location.pathname);
    window.addEventListener("popstate", handlePopState);

    return () => window.removeEventListener("popstate", handlePopState);
  }, []);

  /**
   * 관리자 페이지 접근 시 로그인과 권한 상태를 함께 확인합니다.
   */
  useEffect(() => {
    if (!isAdminPage || loading) {
      return;
    }

    if (!me) {
      navigateTo("/");
      setIsAuthModalOpen(true);
      return;
    }

    if (me.role !== "ADMIN") {
      navigateTo("/");
    }
  }, [isAdminPage, loading, me]);

  /**
   * 인증 모달을 엽니다.
   */
  function openAuthModal() {
    setIsAuthModalOpen(true);
  }

  /**
   * 인증 모달을 닫습니다.
   */
  function closeAuthModal() {
    setIsAuthModalOpen(false);
  }

  /**
   * 앱 내부 페이지 이동 시 현재 경로를 갱신합니다.
   */
  function navigateTo(path) {
    window.history.pushState({}, "", path);
    setPathname(path);
  }

  /**
   * 현재 세션을 종료하고 홈으로 이동합니다.
   */
  async function handleLogout() {
    try {
      await fetch(`${API_BASE_URL}/logout`, {
        method: "POST",
        credentials: "include"
      });
    } catch (error) {
      console.error("Failed to logout", error);
    } finally {
      setMe(null);
      setIsAuthModalOpen(false);
      navigateTo("/");
    }
  }

  /**
   * 현재 관리자 메뉴에 맞는 본문 컴포넌트를 반환합니다.
   */
  function renderAdminContent() {
    if (activeAdminMenu === ADMIN_MENU.users) {
      return <UserManagement currentUserId={me?.id ?? null} {...userState} />;
    }

    if (activeAdminMenu === ADMIN_MENU.categories) {
      return <CategoryManagement {...categoryState} />;
    }

    if (activeAdminMenu === ADMIN_MENU.makers) {
      return <MakerManagement {...makerState} />;
    }

    if (activeAdminMenu === ADMIN_MENU.gears) {
      return <GearManagement {...gearState} />;
    }

    if (activeAdminMenu === ADMIN_MENU.recipeCategories) {
      return <RecipeCategoryManagement {...recipeCategoryState} />;
    }

    if (activeAdminMenu === ADMIN_MENU.recipes) {
      return <RecipeManagement {...recipeState} />;
    }

    if (activeAdminMenu === ADMIN_MENU.campSites) {
      return <CampSiteManagement {...campSiteState} />;
    }

    return <AdminDashboard nickname={me?.nickname ?? ""} role={me?.role ?? ""} />;
  }

  if (isAdminPage) {
    return (
      <AdminLayout
        loading={loading}
        me={me}
        activeMenu={activeAdminMenu}
        activeTitle={activeAdminTitle}
        onNavigate={navigateTo}
        onLogout={handleLogout}
      >
        {renderAdminContent()}
      </AdminLayout>
    );
  }

  return (
    <PublicHome
      me={me}
      loading={loading}
      isAuthModalOpen={isAuthModalOpen}
      onOpenAuthModal={openAuthModal}
      onCloseAuthModal={closeAuthModal}
      onNavigate={navigateTo}
      onLogout={handleLogout}
    />
  );
}

export default App;
