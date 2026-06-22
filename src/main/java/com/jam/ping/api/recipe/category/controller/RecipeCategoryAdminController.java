package com.jam.ping.api.recipe.category.controller;

import com.jam.ping.global.request.KeywordPageRequest;
import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.api.recipe.category.controller.request.RecipeCategoryRequest;
import com.jam.ping.api.recipe.category.controller.response.RecipeCategoryPageResponse;
import com.jam.ping.api.recipe.category.controller.response.RecipeCategoryResponse;
import com.jam.ping.api.recipe.category.domain.RecipeCategory;
import com.jam.ping.api.recipe.category.service.RecipeCategoryService;
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
@RequestMapping("/admin/recipe-categories")
public class RecipeCategoryAdminController {

    private final RecipeCategoryService recipeCategoryService;

    @GetMapping
    public ApiRes<RecipeCategoryPageResponse> getRecipeCategories(@Valid @ModelAttribute KeywordPageRequest request) {
        return new ApiRes<RecipeCategoryPageResponse>()
                .successData(RecipeCategoryPageResponse.from(
                        recipeCategoryService.getRecipeCategories(request.keyword(), request.page(), request.size()),
                        request.keyword().trim()
                ));
    }

    @GetMapping("/{id}")
    public ApiRes<RecipeCategoryResponse> getRecipeCategory(@PathVariable Long id) {
        return new ApiRes<RecipeCategoryResponse>()
                .successData(RecipeCategoryResponse.from(recipeCategoryService.getRecipeCategory(id)));
    }

    @PostMapping
    public ResponseEntity<ApiRes<RecipeCategoryResponse>> createRecipeCategory(
            @Valid @RequestBody RecipeCategoryRequest request
    ) {
        RecipeCategory recipeCategory = recipeCategoryService.createRecipeCategory(
                request.name()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<RecipeCategoryResponse>()
                        .successData(RecipeCategoryResponse.from(recipeCategory))
                        .manipulationOne(recipeCategory.getId())
                        .responseMsg("레시피 카테고리가 등록되었습니다."));
    }

    @PutMapping("/{id}")
    public ApiRes<RecipeCategoryResponse> updateRecipeCategory(
            @PathVariable Long id,
            @Valid @RequestBody RecipeCategoryRequest request
    ) {
        RecipeCategory recipeCategory = recipeCategoryService.updateRecipeCategory(
                id,
                request.name()
        );

        return new ApiRes<RecipeCategoryResponse>()
                .successData(RecipeCategoryResponse.from(recipeCategory))
                .manipulationOne(recipeCategory.getId())
                .responseMsg("레시피 카테고리가 수정되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ApiRes<Void> deleteRecipeCategory(@PathVariable Long id) {
        recipeCategoryService.deleteRecipeCategory(id);

        return new ApiRes<Void>()
                .manipulationOne(id)
                .responseMsg("레시피 카테고리가 삭제되었습니다.");
    }

}
