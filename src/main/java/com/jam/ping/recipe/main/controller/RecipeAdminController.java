package com.jam.ping.recipe.main.controller;

import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.recipe.main.controller.request.RecipeRequest;
import com.jam.ping.recipe.main.controller.response.RecipePageResponse;
import com.jam.ping.recipe.main.controller.response.RecipeResponse;
import com.jam.ping.recipe.main.domain.Recipe;
import com.jam.ping.recipe.main.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/admin/recipes")
public class RecipeAdminController {

    private final RecipeService recipeService;

    @GetMapping
    public ApiRes<RecipePageResponse> getRecipes(
            @RequestParam(required = false) Long recipeCategoryId,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ApiRes<RecipePageResponse>()
                .successData(RecipePageResponse.from(
                        recipeService.getRecipes(recipeCategoryId, keyword, page, size),
                        recipeCategoryId,
                        keyword == null ? "" : keyword.trim()
                ));
    }

    @GetMapping("/{id}")
    public ApiRes<RecipeResponse> getRecipe(@PathVariable Long id) {
        return new ApiRes<RecipeResponse>()
                .successData(RecipeResponse.from(recipeService.getRecipe(id)));
    }

    @PostMapping
    public ResponseEntity<ApiRes<RecipeResponse>> createRecipe(
            @Valid @RequestBody RecipeRequest request
    ) {
        Recipe recipe = recipeService.createRecipe(
                request.name(),
                request.ingredients(),
                request.instructions(),
                request.recipeCategoryId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<RecipeResponse>()
                        .successData(RecipeResponse.from(recipe))
                        .manipulationOne(recipe.getId())
                        .responseMsg("레시피가 등록되었습니다."));
    }

    @PutMapping("/{id}")
    public ApiRes<RecipeResponse> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeRequest request
    ) {
        Recipe recipe = recipeService.updateRecipe(
                id,
                request.name(),
                request.ingredients(),
                request.instructions(),
                request.recipeCategoryId()
        );

        return new ApiRes<RecipeResponse>()
                .successData(RecipeResponse.from(recipe))
                .manipulationOne(recipe.getId())
                .responseMsg("레시피가 수정되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ApiRes<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);

        return new ApiRes<Void>()
                .manipulationOne(id)
                .responseMsg("레시피가 삭제되었습니다.");
    }

}
