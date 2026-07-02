package com.jam.ping.api.recipe.category.service;

import com.jam.ping.api.recipe.category.domain.RecipeCategory;
import com.jam.ping.api.recipe.category.dto.RecipeCategoryDto;
import com.jam.ping.api.recipe.category.repository.RecipeCategoryRepository;
import com.jam.ping.global.exception.ConflictException;
import com.jam.ping.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeCategoryService {

    private final RecipeCategoryRepository recipeCategoryRepository;

    public Page<RecipeCategoryDto> getRecipeCategories(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        if (normalizedKeyword.isBlank()) {
            return recipeCategoryRepository.findAll(pageable).map(RecipeCategoryDto::from);
        }

        return recipeCategoryRepository.findByNameContainingIgnoreCase(normalizedKeyword, pageable).map(RecipeCategoryDto::from);
    }

    public RecipeCategoryDto getRecipeCategory(Long id) {
        return RecipeCategoryDto.from(findRecipeCategory(id));
    }

    @Transactional
    public RecipeCategoryDto createRecipeCategory(String name) {
        validateDuplicateName(name, null);
        return RecipeCategoryDto.from(recipeCategoryRepository.save(RecipeCategory.create(name)));
    }

    @Transactional
    public RecipeCategoryDto updateRecipeCategory(Long id, String name) {
        validateDuplicateName(name, id);
        RecipeCategory recipeCategory = findRecipeCategory(id);
        recipeCategory.update(name);
        return RecipeCategoryDto.from(recipeCategory);
    }

    @Transactional
    public void deleteRecipeCategory(Long id) {
        recipeCategoryRepository.delete(findRecipeCategory(id));
    }

    RecipeCategory findRecipeCategory(Long id) {
        return recipeCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("레시피 카테고리를 찾을 수 없습니다."));
    }

    private void validateDuplicateName(String name, Long id) {
        String normalizedName = name == null ? "" : name.trim();

        boolean duplicated = id == null
                ? recipeCategoryRepository.existsByNameIgnoreCase(normalizedName)
                : recipeCategoryRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id);

        if (duplicated) {
            throw new ConflictException("이미 등록된 카테고리명입니다.");
        }
    }
}
