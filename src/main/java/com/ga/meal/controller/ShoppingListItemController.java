package com.ga.meal.controller;

import com.ga.meal.entity.ShoppingListItem;
import com.ga.meal.service.ShoppingListItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping-list-items")
@RequiredArgsConstructor
public class ShoppingListItemController {

    private final ShoppingListItemService shoppingListItemService;

    @PostMapping("/{shoppingListId}/{ingredientId}")
    public ShoppingListItem addItemToShoppingList(
            @PathVariable Long shoppingListId,
            @PathVariable Long ingredientId,
            @RequestBody ShoppingListItem shoppingListItem
    ) {
        return shoppingListItemService.addItemToShoppingList(
                shoppingListId,
                ingredientId,
                shoppingListItem
        );
    }

    @GetMapping("/{shoppingListId}")
    public List<ShoppingListItem> getShoppingListItems(@PathVariable Long shoppingListId) {
        return shoppingListItemService.getShoppingListItems(shoppingListId);
    }

    @PutMapping("/{shoppingListItemId}")
    public ShoppingListItem updateShoppingListItem(
            @PathVariable Long shoppingListItemId,
            @RequestBody ShoppingListItem shoppingListItem
    ) {
        return shoppingListItemService.updateShoppingListItem(
                shoppingListItemId,
                shoppingListItem
        );
    }

    @DeleteMapping("/{shoppingListItemId}")
    public String deleteShoppingListItem(@PathVariable Long shoppingListItemId) {
        shoppingListItemService.deleteShoppingListItem(shoppingListItemId);
        return "Shopping list item deleted successfully";
    }
}