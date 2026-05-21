package com.ga.meal.controller;

import com.ga.meal.entity.Favorite;
import com.ga.meal.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{recipeId}")
    public Favorite addFavorite(@PathVariable Long recipeId) {
        return favoriteService.addFavorite(recipeId);
    }

    @GetMapping
    public List<Favorite> getMyFavorites() {
        return favoriteService.getMyFavorites();
    }

    @DeleteMapping("/{favoriteId}")
    public String deleteFavorite(@PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        return "Favorite deleted successfully";
    }
}