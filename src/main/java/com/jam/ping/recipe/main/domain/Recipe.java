package com.jam.ping.recipe.main.domain;

import com.jam.ping.recipe.category.domain.RecipeCategory;
import com.jam.ping.user.main.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Entity
@Table(name = "recipes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_category_id", nullable = false)
    private RecipeCategory recipeCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private Recipe(
            String name,
            String ingredients,
            String instructions,
            RecipeCategory recipeCategory,
            User createdBy,
            User updatedBy
    ) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.recipeCategory = recipeCategory;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public void update(String name, String ingredients, String instructions, RecipeCategory recipeCategory, User updatedBy) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.recipeCategory = recipeCategory;
        this.updatedBy = updatedBy;
    }
}
