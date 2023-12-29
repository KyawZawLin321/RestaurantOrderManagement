package com.ai.jwd42.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.jwd42.dto.Food;
import com.ai.jwd42.dto.FoodSet;
import com.ai.jwd42.dto.Restaurant;
import com.ai.jwd42.dto.SearchResult;
import com.ai.jwd42.service.FoodService;
import com.ai.jwd42.service.FoodSetService;
import com.ai.jwd42.service.RestaurantService;

@RestController
public class SearchController {
	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private FoodSetService foodSetService;

	@Autowired
	private FoodService foodService;

	@PostMapping("/api/search")
	public SearchResult search(@RequestBody String q) {
		String query = q.toLowerCase();
		String[] parts = query.split("=");
		final String[] extractedValue = { "" }; // Array of size 1 to hold the value

		// Check if the split produced at least two parts
		if (parts.length >= 2) {
			extractedValue[0] = parts[1].toLowerCase(); // Extracted value in lowercase
			System.out.println(extractedValue[0]); // This will print the extracted value
		}

		List<Restaurant> matchingRestaurants = restaurantService.searchRestaurant(extractedValue[0]).stream()
				.filter(restaurant -> restaurant.getName().toLowerCase().contains(extractedValue[0]))
				.collect(Collectors.toList());

		List<Food> matchingFoodItems = foodService.searchFood(extractedValue[0]).stream()
				.filter(foodItem -> foodItem.getName().toLowerCase().contains(extractedValue[0]))
				.collect(Collectors.toList());

		List<FoodSet> matchingFoodSets = foodSetService.searchFoodSet(extractedValue[0]).stream()
				.filter(foodSet -> foodSet.getName().toLowerCase().contains(extractedValue[0]))
				.collect(Collectors.toList());

		return new SearchResult(matchingRestaurants, matchingFoodItems, matchingFoodSets);
	}

}
