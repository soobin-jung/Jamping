package com.jam.ping.recipe.main.repository;

import com.jam.ping.recipe.main.domain.Recipe;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @EntityGraph(attributePaths = {"recipeCategory", "createdBy", "updatedBy"})
    Page<Recipe> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"recipeCategory", "createdBy", "updatedBy"})
    Page<Recipe> findByRecipeCategoryId(Long recipeCategoryId, Pageable pageable);

    @EntityGraph(attributePaths = {"recipeCategory", "createdBy", "updatedBy"})
    Page<Recipe> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"recipeCategory", "createdBy", "updatedBy"})
    Page<Recipe> findByRecipeCategoryIdAndNameContainingIgnoreCase(Long recipeCategoryId, String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"recipeCategory", "createdBy", "updatedBy"})
    Optional<Recipe> findWithDetailsById(Long id);

    boolean existsByRecipeCategoryIdAndNameIgnoreCase(Long recipeCategoryId, String name);

    boolean existsByRecipeCategoryIdAndNameIgnoreCaseAndIdNot(Long recipeCategoryId, String name, Long id);
}
