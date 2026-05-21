package com.ga.meal.service;

import com.ga.meal.entity.Favorite;
import com.ga.meal.entity.Recipe;
import com.ga.meal.entity.User;
import com.ga.meal.repository.FavoriteRepository;
import com.ga.meal.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final RecipeRepository recipeRepository;
    private final UserService userService;

    public Favorite addFavorite(Long recipeId) {
        User currentUser = userService.getCurrentUser();

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Favorite favorite = Favorite.builder()
                .user(currentUser)
                .recipe(recipe)
                .build();

        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getMyFavorites() {
        User currentUser = userService.getCurrentUser();
        return favoriteRepository.findByUserId(currentUser.getId());
    }

    public void deleteFavorite(Long favoriteId) {
        User currentUser = userService.getCurrentUser();

        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        if (!favorite.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own favorite");
        }

        favoriteRepository.delete(favorite);
    }
}