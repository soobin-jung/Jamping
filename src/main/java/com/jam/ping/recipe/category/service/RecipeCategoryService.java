package com.jam.ping.recipe.category.service;

import com.jam.ping.recipe.category.domain.RecipeCategory;
import com.jam.ping.recipe.category.repository.RecipeCategoryRepository;
import com.jam.ping.user.main.domain.User;
import com.jam.ping.user.main.service.UserService;
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
    private final UserService userService;

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
    public RecipeCategory createRecipeCategory(String name, Long actorUserId) {
        validateDuplicateName(name, null);
        User actorUser = userService.getActorUser(actorUserId);

        RecipeCategory recipeCategory = RecipeCategory.builder()
                .name(name)
                .createdBy(actorUser)
                .updatedBy(actorUser)
                .build();

        return recipeCategoryRepository.save(recipeCategory);
    }

    @Transactional
    public RecipeCategory updateRecipeCategory(Long id, String name, Long actorUserId) {
        validateDuplicateName(name, id);
        RecipeCategory recipeCategory = findRecipeCategory(id);
        User actorUser = userService.getActorUser(actorUserId);

        recipeCategory.update(name, actorUser);
        return recipeCategory;
    }

    @Transactional
    public void deleteRecipeCategory(Long id) {
        RecipeCategory recipeCategory = findRecipeCategory(id);
        recipeCategoryRepository.delete(recipeCategory);
    }

    private RecipeCategory findRecipeCategory(Long id) {
        return recipeCategoryRepository.findWithUsersById(id)
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
