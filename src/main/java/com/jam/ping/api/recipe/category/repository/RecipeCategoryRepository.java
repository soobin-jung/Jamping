package com.jam.ping.api.recipe.category.repository;

import com.jam.ping.api.recipe.category.domain.RecipeCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {

    Page<RecipeCategory> findAll(Pageable pageable);

    Page<RecipeCategory> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
