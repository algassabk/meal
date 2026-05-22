package com.ga.meal.controller;

import com.ga.meal.dto.RecipeRequest;
import com.ga.meal.entity.Recipe;
import com.ga.meal.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public Recipe createRecipe(@Valid @RequestBody RecipeRequest request) {
        return recipeService.createRecipe(request);
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/paged")
    public Page<Recipe> getAllRecipesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return recipeService.getAllRecipes(createPageable(page, size, sortBy, direction));
    }

    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    @PutMapping("/{id}")
    public Recipe updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeRequest request
    ) {
        return recipeService.updateRecipe(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return "Recipe deleted successfully";
    }

    @GetMapping("/search")
    public List<Recipe> searchRecipes(@RequestParam String keyword) {
        return recipeService.searchRecipes(keyword);
    }

    @GetMapping("/search/paged")
    public Page<Recipe> searchRecipesPaged(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return recipeService.searchRecipes(keyword, createPageable(page, size, sortBy, direction));
    }

    @GetMapping("/category/{categoryId}")
    public List<Recipe> getRecipesByCategory(
            @PathVariable Long categoryId
    ) {
        return recipeService.getRecipesByCategory(categoryId);
    }

    @GetMapping("/category/{categoryId}/paged")
    public Page<Recipe> getRecipesByCategoryPaged(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return recipeService.getRecipesByCategory(categoryId, createPageable(page, size, sortBy, direction));
    }

    @PutMapping("/{id}/visibility")
    public Recipe updateRecipeVisibility(
            @PathVariable Long id,
            @RequestParam Boolean isPublic
    ) {
        return recipeService.updateRecipeVisibility(id, isPublic);
    }

    @PostMapping("/{id}/image")
    public Recipe uploadRecipeImage(
            @PathVariable Long id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file
    ) {
        return recipeService.uploadRecipeImage(id, file);
    }

    private Pageable createPageable(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(sortBy);

        if (direction.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        return PageRequest.of(page, size, sort);
    }
}
