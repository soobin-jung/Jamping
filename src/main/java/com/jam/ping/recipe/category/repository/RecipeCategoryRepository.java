package com.jam.ping.recipe.category.repository;

import com.jam.ping.recipe.category.domain.RecipeCategory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {

    @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
    Page<RecipeCategory> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
    Page<RecipeCategory> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
    Optional<RecipeCategory> findWithUsersById(Long id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
