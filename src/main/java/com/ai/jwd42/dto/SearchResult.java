package com.ai.jwd42.dto;

import java.util.List;

public class SearchResult {

    private List<Restaurant> restaurants;
    private List<Food> foods;
    private List<FoodSet> foodSets;

    // Constructors, getters, setters, etc.

    // Example constructor (add others as needed)
    public SearchResult(List<Restaurant> restaurants, List<Food> foods, List<FoodSet> foodSets) {
        this.restaurants = restaurants;
        this.foods = foods;
        this.foodSets = foodSets;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public List<FoodSet> getFoodSets() {
        return foodSets;
    }
}
