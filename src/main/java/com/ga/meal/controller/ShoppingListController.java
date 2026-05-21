package com.ga.meal.controller;

import com.ga.meal.entity.ShoppingList;
import com.ga.meal.service.ShoppingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping-lists")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    @PostMapping
    public ShoppingList createShoppingList(@RequestBody ShoppingList shoppingList) {
        return shoppingListService.createShoppingList(shoppingList);
    }

    @GetMapping
    public List<ShoppingList> getMyShoppingLists() {
        return shoppingListService.getMyShoppingLists();
    }

    @GetMapping("/{id}")
    public ShoppingList getShoppingListById(@PathVariable Long id) {
        return shoppingListService.getShoppingListById(id);
    }

    @PutMapping("/{id}")
    public ShoppingList updateShoppingList(
            @PathVariable Long id,
            @RequestBody ShoppingList shoppingList
    ) {
        return shoppingListService.updateShoppingList(id, shoppingList);
    }

    @DeleteMapping("/{id}")
    public String deleteShoppingList(@PathVariable Long id) {
        shoppingListService.deleteShoppingList(id);
        return "Shopping list deleted successfully";
    }
}