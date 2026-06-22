package com.jam.ping.api.gear.category.controller;

import com.jam.ping.api.gear.category.controller.request.CategoryRequest;
import com.jam.ping.api.gear.category.controller.response.CategoryPageResponse;
import com.jam.ping.api.gear.category.controller.response.CategoryResponse;
import com.jam.ping.api.gear.category.domain.Category;
import com.jam.ping.api.gear.category.service.CategoryService;
import com.jam.ping.global.request.KeywordPageRequest;
import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminOnly
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiRes<CategoryPageResponse> getCategories(@Valid @ModelAttribute KeywordPageRequest request) {
        return new ApiRes<CategoryPageResponse>()
                .successData(CategoryPageResponse.from(
                        categoryService.getCategories(request.keyword(), request.page(), request.size()),
                        request.keyword().trim()
                ));
    }

    @GetMapping("/{categoryId}")
    public ApiRes<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        return new ApiRes<CategoryResponse>()
                .successData(CategoryResponse.from(categoryService.getCategory(categoryId)));
    }

    @PostMapping
    public ResponseEntity<ApiRes<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request
    ) {
        Category category = categoryService.createCategory(
                request.name(),
                request.memo()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<CategoryResponse>()
                        .successData(CategoryResponse.from(category))
                        .manipulationOne(category.getId())
                        .responseMsg("카테고리가 등록되었습니다."));
    }

    @PutMapping("/{categoryId}")
    public ApiRes<CategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest request
    ) {
        Category category = categoryService.updateCategory(
                categoryId,
                request.name(),
                request.memo()
        );

        return new ApiRes<CategoryResponse>()
                .successData(CategoryResponse.from(category))
                .manipulationOne(category.getId())
                .responseMsg("카테고리가 수정되었습니다.");
    }

    @DeleteMapping("/{categoryId}")
    public ApiRes<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);

        return new ApiRes<Void>()
                .manipulationOne(categoryId)
                .responseMsg("카테고리가 삭제되었습니다.");
    }

}
