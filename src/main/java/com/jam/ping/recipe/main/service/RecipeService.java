package com.jam.ping.recipe.main.service;

import com.jam.ping.recipe.category.domain.RecipeCategory;
import com.jam.ping.recipe.category.repository.RecipeCategoryRepository;
import com.jam.ping.recipe.main.domain.Recipe;
import com.jam.ping.recipe.main.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCategoryRepository recipeCategoryRepository;

    public Page<Recipe> getRecipes(Long recipeCategoryId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        boolean hasCategory = recipeCategoryId != null;
        boolean hasKeyword = !normalizedKeyword.isBlank();

        if (!hasCategory && !hasKeyword) {
            return recipeRepository.findAll(pageable);
        }
        if (hasCategory && !hasKeyword) {
            return recipeRepository.findByRecipeCategoryId(recipeCategoryId, pageable);
        }
        if (!hasCategory) {
            return recipeRepository.findByNameContainingIgnoreCase(normalizedKeyword, pageable);
        }
        return recipeRepository.findByRecipeCategoryIdAndNameContainingIgnoreCase(recipeCategoryId, normalizedKeyword, pageable);
    }

    public Recipe getRecipe(Long id) {
        return findRecipe(id);
    }

    @Transactional
    public Recipe createRecipe(String name, String ingredients, String instructions, Long recipeCategoryId) {
        RecipeCategory recipeCategory = findRecipeCategory(recipeCategoryId);
        validateDuplicate(recipeCategory.getId(), name, null);

        Recipe recipe = Recipe.builder()
                .name(name)
                .ingredients(ingredients)
                .instructions(instructions)
                .recipeCategory(recipeCategory)
                .build();

        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe updateRecipe(Long id, String name, String ingredients, String instructions, Long recipeCategoryId) {
        Recipe recipe = findRecipe(id);
        RecipeCategory recipeCategory = findRecipeCategory(recipeCategoryId);
        validateDuplicate(recipeCategory.getId(), name, id);

        recipe.update(name, ingredients, instructions, recipeCategory);
        return recipe;
    }

    @Transactional
    public void deleteRecipe(Long id) {
        Recipe recipe = findRecipe(id);
        recipeRepository.delete(recipe);
    }

    private Recipe findRecipe(Long id) {
        return recipeRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "레시피를 찾을 수 없습니다."));
    }

    private RecipeCategory findRecipeCategory(Long recipeCategoryId) {
        if (recipeCategoryId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리는 필수입니다.");
        }
        return recipeCategoryRepository.findById(recipeCategoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "레시피 카테고리를 찾을 수 없습니다."));
    }

    private void validateDuplicate(Long recipeCategoryId, String name, Long recipeId) {
        String normalizedName = name == null ? "" : name.trim();

        boolean duplicated = recipeId == null
                ? recipeRepository.existsByRecipeCategoryIdAndNameIgnoreCase(recipeCategoryId, normalizedName)
                : recipeRepository.existsByRecipeCategoryIdAndNameIgnoreCaseAndIdNot(recipeCategoryId, normalizedName, recipeId);

        if (duplicated) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "같은 카테고리에 동일한 레시피명이 이미 존재합니다.");
        }
    }
}
