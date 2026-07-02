package com.jam.ping.api.gear.category.service;

import com.jam.ping.api.gear.category.domain.Category;
import com.jam.ping.api.gear.category.dto.CategoryDto;
import com.jam.ping.api.gear.category.repository.CategoryRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Page<CategoryDto> getCategories(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        if (normalizedKeyword.isBlank()) {
            return categoryRepository.findAll(pageable).map(CategoryDto::from);
        }

        return categoryRepository.findByNameContainingIgnoreCase(normalizedKeyword, pageable).map(CategoryDto::from);
    }

    public CategoryDto getCategory(Long categoryId) {
        return CategoryDto.from(findCategory(categoryId));
    }

    @Transactional
    public CategoryDto createCategory(String name, String memo) {
        validateDuplicateName(name, null);
        return CategoryDto.from(categoryRepository.save(Category.create(name, memo)));
    }

    @Transactional
    public CategoryDto updateCategory(Long categoryId, String name, String memo) {
        validateDuplicateName(name, categoryId);
        Category category = findCategory(categoryId);
        category.update(name, memo);
        return CategoryDto.from(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.delete(findCategory(categoryId));
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."));
    }

    private void validateDuplicateName(String name, Long categoryId) {
        String normalizedName = name == null ? "" : name.trim();

        boolean duplicated = categoryId == null
                ? categoryRepository.existsByNameIgnoreCase(normalizedName)
                : categoryRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, categoryId);

        if (duplicated) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 등록된 카테고리명입니다.");
        }
    }
}
