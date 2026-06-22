package com.jam.ping.api.recipe.category.service;

import com.jam.ping.api.recipe.category.domain.RecipeCategory;
import com.jam.ping.api.recipe.category.repository.RecipeCategoryRepository;
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
public class RecipeCategoryService {

    private final RecipeCategoryRepository recipeCategoryRepository;

    public Page<RecipeCategory> getRecipeCategories(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        if (normalizedKeyword.isBlank()) {
            return recipeCategoryRepository.findAll(pageable);
        }

        return recipeCategoryRepository.findByNameContainingIgnoreCase(normalizedKeyword, pageable);
    }

    public RecipeCategory getRecipeCategory(Long id) {
        return findRecipeCategory(id);
    }

    @Transactional
    public RecipeCategory createRecipeCategory(String name) {
        validateDuplicateName(name, null);

        RecipeCategory recipeCategory = RecipeCategory.builder()
                .name(name)
                .build();

        return recipeCategoryRepository.save(recipeCategory);
    }

    @Transactional
    public RecipeCategory updateRecipeCategory(Long id, String name) {
        validateDuplicateName(name, id);
        RecipeCategory recipeCategory = findRecipeCategory(id);
        recipeCategory.update(name);
        return recipeCategory;
    }

    @Transactional
    public void deleteRecipeCategory(Long id) {
        RecipeCategory recipeCategory = findRecipeCategory(id);
        recipeCategoryRepository.delete(recipeCategory);
    }

    private RecipeCategory findRecipeCategory(Long id) {
        return recipeCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "레시피 카테고리를 찾을 수 없습니다."));
    }

    private void validateDuplicateName(String name, Long id) {
        String normalizedName = name == null ? "" : name.trim();

        boolean duplicated = id == null
                ? recipeCategoryRepository.existsByNameIgnoreCase(normalizedName)
                : recipeCategoryRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id);

        if (duplicated) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 등록된 카테고리명입니다.");
        }
    }
}
