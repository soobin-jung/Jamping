package com.jam.ping.category.controller;

import com.jam.ping.category.controller.request.CategoryRequest;
import com.jam.ping.category.controller.response.CategoryPageResponse;
import com.jam.ping.category.controller.response.CategoryResponse;
import com.jam.ping.category.domain.Category;
import com.jam.ping.category.service.CategoryService;
import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.user.oauth.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminOnly
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 관리자 화면에서 사용하는 카테고리 목록을 검색 조건과 페이지 정보로 조회합니다.
     */
    @GetMapping
    public ApiRes<CategoryPageResponse> getCategories(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ApiRes<CategoryPageResponse>()
                .successData(CategoryPageResponse.from(
                        categoryService.getCategories(keyword, page, size),
                        keyword == null ? "" : keyword.trim()
                ));
    }

    /**
     * 단건 카테고리 정보를 조회합니다.
     */
    @GetMapping("/{categoryId}")
    public ApiRes<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        return new ApiRes<CategoryResponse>()
                .successData(CategoryResponse.from(categoryService.getCategory(categoryId)));
    }

    /**
     * 새로운 카테고리를 생성합니다.
     */
    @PostMapping
    public ResponseEntity<ApiRes<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Category category = categoryService.createCategory(
                request.name(),
                request.memo(),
                resolveActorUserId(oauth2User)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<CategoryResponse>()
                        .successData(CategoryResponse.from(category))
                        .manipulationOne(category.getId())
                        .responseMsg("카테고리가 등록되었습니다."));
    }

    /**
     * 기존 카테고리 정보를 수정합니다.
     */
    @PutMapping("/{categoryId}")
    public ApiRes<CategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Category category = categoryService.updateCategory(
                categoryId,
                request.name(),
                request.memo(),
                resolveActorUserId(oauth2User)
        );

        return new ApiRes<CategoryResponse>()
                .successData(CategoryResponse.from(category))
                .manipulationOne(category.getId())
                .responseMsg("카테고리가 수정되었습니다.");
    }

    /**
     * 선택한 카테고리를 삭제합니다.
     */
    @DeleteMapping("/{categoryId}")
    public ApiRes<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);

        return new ApiRes<Void>()
                .manipulationOne(categoryId)
                .responseMsg("카테고리가 삭제되었습니다.");
    }

    /**
     * 생성자와 수정자 기록에 사용할 사용자 ID를 반환합니다.
     */
    private Long resolveActorUserId(CustomOAuth2User oauth2User) {
        if (oauth2User == null) {
            return null;
        }

        return oauth2User.getUserId();
    }
}
