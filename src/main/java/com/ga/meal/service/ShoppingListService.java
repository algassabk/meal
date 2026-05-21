package com.ga.meal.service;

import com.ga.meal.entity.ShoppingList;
import com.ga.meal.entity.User;
import com.ga.meal.repository.ShoppingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final UserService userService;

    public ShoppingList createShoppingList(ShoppingList shoppingList) {
        User currentUser = userService.getCurrentUser();

        shoppingList.setUser(currentUser);

        return shoppingListRepository.save(shoppingList);
    }

    public List<ShoppingList> getMyShoppingLists() {
        User currentUser = userService.getCurrentUser();

        return shoppingListRepository.findByUserId(currentUser.getId());
    }

    public ShoppingList getShoppingListById(Long id) {
        return shoppingListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shopping list not found"));
    }

    public ShoppingList updateShoppingList(Long id, ShoppingList updatedShoppingList) {
        ShoppingList shoppingList = getShoppingListById(id);

        shoppingList.setTitle(updatedShoppingList.getTitle());

        return shoppingListRepository.save(shoppingList);
    }

    public void deleteShoppingList(Long id) {
        ShoppingList shoppingList = getShoppingListById(id);

        shoppingListRepository.delete(shoppingList);
    }
}