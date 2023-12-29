package com.ai.jwd42.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.jwd42.dto.Food;
import com.ai.jwd42.dto.FoodForFoodSet;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.repo.FoodRepoistory;
import com.ai.jwd42.repo.UserRepository;

@Service
public class FoodService {

	@Autowired
	private FoodRepoistory foodRepoistory;

	@Autowired
	private UserRepository userRepository;

	public int foodRowCount(String email) {
		/* Food food = foodRepoistory.findFoodByEmail(email); */
		User user = userRepository.findUserByEmail(email);
		if (user == null) {
			return 0;
		} else {
			return foodRepoistory.foodRowCount(user.getRestaurantId());
		}
	}

	public List<Food> findAllFoodByRestaurantId(int id) {

		return foodRepoistory.findAllFoodByRestaurantId(id);
	}

	public Food findFoodById(int foodId) {

		return foodRepoistory.findFoodById(foodId);
	}

	public int insert(Food food) {
		return foodRepoistory.insert(food);
	}

	public List<Food> findAll(int id) {
		List<Food> foods = foodRepoistory.findAll(id);
		return foods;
	}

	public void updateFood(Food food) {
		foodRepoistory.updateFood(food);

	}

	public boolean checkFoodNameAlreadyExist(String name, int id) {
		Food food = foodRepoistory.findFoodByName(name);
		if (food == null) {
			return false;
		} else {

			if (id == food.getId()) {
				return false;
			}
			return true;
		}
	}

	public void updateFoodImg(Food food) {
		foodRepoistory.updateFoodImg(food);
	}

	public boolean checkFoodPathAlreadyExist(String path, int id) {
		Food food = foodRepoistory.findFoodByName(path);
		if (food == null) {
			return false;
		} else {

			if (id == food.getId()) {
				return false;
			}
			return true;
		}

	}

	public void delete(int id) {

		foodRepoistory.deleteFood(id);
	}

	public void deleteFoodImg(int id) {

		foodRepoistory.deleteFoodImg(id);
	}

	public List<Food> searchFood(String keyword) {
		return foodRepoistory.searchFood(keyword);
	}

	public List<FoodForFoodSet> findFoodForFoodSet(int id) {
		return foodRepoistory.findFoodForFoodSet(id);

	}

	public boolean checkFoodInput(Food food) {

		if (food.getName().length() > 40 || food.getDescription().length() > 255 || food.getPrice() > 9999999
				|| food.getMaxQuantity() > 100) {
			return true;
		}

		return false;
	}

	public boolean checkFoodIsEmpty(Food food) {
		if (food.getName()==null || food.getDescription()== null || food.getPrice()==0  || food.getMaxQuantity()==0 || food.getName().equals("") || food.getDescription().equals("") || food.getSelect()==null || food.getSelect().equals(""))  {
			return true;
		}
		return false;
	}

	public List<Food> findFoodByCategoryId(int id) {
		return foodRepoistory.findFoodByCategoryId(id);
	
	}

	public List<FoodForFoodSet> findFoodForFoodSetByFoodId(int id) {
		// TODO Auto-generated method stub
		return foodRepoistory.findFoodForFoodSetByFoodId(id);
	}

}
