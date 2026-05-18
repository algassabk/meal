package com.ga.meal.service;

import com.ga.meal.entity.Category;
import com.ga.meal.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Creates a new category.
     */
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Gets all categories.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Gets one category by id.
     */
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    /**
     * Updates a category.
     */
    public Category updateCategory(Long id, Category updatedCategory) {
        Category category = getCategoryById(id);

        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());

        return categoryRepository.save(category);
    }

    /**
     * Deletes a category.
     */
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}