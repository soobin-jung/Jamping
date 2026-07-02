package com.jam.ping.api.recipe.main.domain;

import com.jam.ping.global.domain.CommonEntity;
import com.jam.ping.api.recipe.category.domain.RecipeCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe extends CommonEntity {

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

    public static Recipe create(String name, String ingredients, String instructions, RecipeCategory recipeCategory) {
        Recipe recipe = new Recipe();
        recipe.name = name;
        recipe.ingredients = ingredients;
        recipe.instructions = instructions;
        recipe.recipeCategory = recipeCategory;
        return recipe;
    }

    public void update(String name, String ingredients, String instructions, RecipeCategory recipeCategory) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.recipeCategory = recipeCategory;
    }
}
