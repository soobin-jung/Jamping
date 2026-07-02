package com.jam.ping.api.recipe.main.service;

import com.jam.ping.api.recipe.category.domain.RecipeCategory;
import com.jam.ping.api.recipe.category.repository.RecipeCategoryRepository;
import com.jam.ping.api.recipe.main.domain.Recipe;
import com.jam.ping.api.recipe.main.dto.RecipeDto;
import com.jam.ping.api.recipe.main.repository.RecipeRepository;
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

    public Page<RecipeDto> getRecipes(Long recipeCategoryId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        boolean hasCategory = recipeCategoryId != null;
        boolean hasKeyword = !normalizedKeyword.isBlank();

        if (!hasCategory && !hasKeyword) {
            return recipeRepository.findAll(pageable).map(RecipeDto::from);
        }
        if (hasCategory && !hasKeyword) {
            return recipeRepository.findByRecipeCategoryId(recipeCategoryId, pageable).map(RecipeDto::from);
        }
        if (!hasCategory) {
            return recipeRepository.findByNameContainingIgnoreCase(normalizedKeyword, pageable).map(RecipeDto::from);
        }
        return recipeRepository.findByRecipeCategoryIdAndNameContainingIgnoreCase(recipeCategoryId, normalizedKeyword, pageable).map(RecipeDto::from);
    }

    public RecipeDto getRecipe(Long id) {
        return RecipeDto.from(findRecipe(id));
    }

    @Transactional
    public RecipeDto createRecipe(String name, String ingredients, String instructions, Long recipeCategoryId) {
        RecipeCategory recipeCategory = findRecipeCategory(recipeCategoryId);
        validateDuplicate(recipeCategory.getId(), name, null);

        return RecipeDto.from(recipeRepository.save(Recipe.create(name, ingredients, instructions, recipeCategory)));
    }

    @Transactional
    public RecipeDto updateRecipe(Long id, String name, String ingredients, String instructions, Long recipeCategoryId) {
        Recipe recipe = findRecipe(id);
        RecipeCategory recipeCategory = findRecipeCategory(recipeCategoryId);
        validateDuplicate(recipeCategory.getId(), name, id);

        recipe.update(name, ingredients, instructions, recipeCategory);
        return RecipeDto.from(recipe);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        recipeRepository.delete(findRecipe(id));
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
