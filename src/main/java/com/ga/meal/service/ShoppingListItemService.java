package com.ga.meal.service;

import com.ga.meal.entity.Ingredient;
import com.ga.meal.entity.ShoppingList;
import com.ga.meal.entity.ShoppingListItem;
import com.ga.meal.repository.IngredientRepository;
import com.ga.meal.repository.ShoppingListItemRepository;
import com.ga.meal.repository.ShoppingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListItemService {

    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final IngredientRepository ingredientRepository;

    public ShoppingListItem addItemToShoppingList(
            Long shoppingListId,
            Long ingredientId,
            ShoppingListItem shoppingListItem
    ) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new RuntimeException("Shopping list not found"));

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        shoppingListItem.setShoppingList(shoppingList);
        shoppingListItem.setIngredient(ingredient);

        return shoppingListItemRepository.save(shoppingListItem);
    }

    public List<ShoppingListItem> getShoppingListItems(Long shoppingListId) {
        return shoppingListItemRepository.findByShoppingListId(shoppingListId);
    }

    public ShoppingListItem updateShoppingListItem(
            Long shoppingListItemId,
            ShoppingListItem updatedShoppingListItem
    ) {
        ShoppingListItem shoppingListItem = shoppingListItemRepository.findById(shoppingListItemId)
                .orElseThrow(() -> new RuntimeException("Shopping list item not found"));

        shoppingListItem.setQuantity(updatedShoppingListItem.getQuantity());

        return shoppingListItemRepository.save(shoppingListItem);
    }

    public void deleteShoppingListItem(Long shoppingListItemId) {
        ShoppingListItem shoppingListItem = shoppingListItemRepository.findById(shoppingListItemId)
                .orElseThrow(() -> new RuntimeException("Shopping list item not found"));

        shoppingListItemRepository.delete(shoppingListItem);
    }
}