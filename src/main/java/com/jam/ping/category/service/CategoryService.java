package com.jam.ping.category.service;

import com.jam.ping.category.domain.Category;
import com.jam.ping.category.repository.CategoryRepository;
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

    /**
     * 관리자 화면에서 사용하는 카테고리 목록을 검색 조건과 페이지 범위로 조회합니다.
     */
    public Page<Category> getCategories(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        if (normalizedKeyword.isBlank()) {
            return categoryRepository.findAll(pageable);
        }

        return categoryRepository.findByNameContainingIgnoreCase(normalizedKeyword, pageable);
    }

    /**
     * 요청한 ID의 카테고리를 조회합니다.
     */
    public Category getCategory(Long categoryId) {
        return findCategory(categoryId);
    }

    /**
     * 새로운 카테고리를 생성합니다.
     */
    @Transactional
    public Category createCategory(String name, String memo) {
        validateDuplicateName(name, null);

        Category category = Category.builder()
                .name(name)
                .memo(memo)
                .build();

        return categoryRepository.save(category);
    }

    /**
     * 기존 카테고리 정보를 수정합니다.
     */
    @Transactional
    public Category updateCategory(Long categoryId, String name, String memo) {
        validateDuplicateName(name, categoryId);

        Category category = findCategory(categoryId);
        category.update(name, memo);
        return category;
    }

    /**
     * 선택한 카테고리를 삭제합니다.
     */
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = findCategory(categoryId);
        categoryRepository.delete(category);
    }

    /**
     * 공통 조회 메서드로 카테고리를 찾고, 없으면 404 예외를 발생시킵니다.
     */
    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."));
    }

    /**
     * 생성 또는 수정 전에 동일한 카테고리명이 존재하는지 검사합니다.
     */
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
