package com.ai.jwd42.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.jwd42.dto.FoodSet;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.repo.FoodSetRepoistory;
import com.ai.jwd42.repo.UserRepository;

@Service
public class FoodSetService {

	@Autowired
	private FoodSetRepoistory foodSetRepoistory;

	@Autowired
	private UserRepository userRepository;

	public int foodSetRowCount(String email) {
		User user = userRepository.findUserByEmail(email);
		if (user == null) {
			return 0;
		} else {
			return foodSetRepoistory.foodSetRowCount(user.getRestaurantId());
		}
	}

	public List<FoodSet> findAllFoodSetByRestaurantId(int id) {

		return foodSetRepoistory.findAllFoodSetByRestaurantId(id);
	}

	public FoodSet findFoodSetById(int foodSetId) {

		return foodSetRepoistory.findFoodSetById(foodSetId);
	}

	// sss
	public boolean checkFoodSetNameAlreadyExist(String name, int id) {
		FoodSet foodSet = foodSetRepoistory.findFoodSetByName(name);
		if (foodSet == null) {
			return false;
		} else {

			if (id == foodSet.getId()) {
				return false;
			}
			return true;
		}
	}

	public boolean checkFoodSetPathAlreadyExist(String path, int id) {
		FoodSet foodSet = foodSetRepoistory.findFoodSetByName(path);
		if (foodSet == null) {
			return false;
		} else {

			if (id == foodSet.getId()) {
				return false;
			}
			return true;
		}

	}

	public int insert(FoodSet foodSet) {
		return foodSetRepoistory.insert(foodSet);
	}

	public int insertFood(FoodSet foodSet) {
		return foodSetRepoistory.insertFood(foodSet);
	}

	public List<FoodSet> findAll(int id) {
		List<FoodSet> foodSets = foodSetRepoistory.findAll(id);
		return foodSets;
	}

	public List<FoodSet> findFoodForFoodSetByFoodSetId(int id) {
		List<FoodSet> foodSets = foodSetRepoistory.findFoodForFoodSetByFoodSetId(id);
		return foodSets;
	}

	public void updateFoodSet(FoodSet foodSet) {
		foodSetRepoistory.updateFoodSet(foodSet);

	}

	public void updateFoodSetImg(FoodSet foodSet) {
		foodSetRepoistory.updateFoodSetImg(foodSet);
	}

	public void delete(int id) {

		foodSetRepoistory.deleteFoodSet(id);
	}

	public void deleteFoodSetImg(int id) {

		foodSetRepoistory.deleteFoodSetImg(id);
	}

	public void deleteFoodForFoodSet(int id) {

		foodSetRepoistory.deleteFoodForFoodSet(id);
	}

	public List<FoodSet> searchFoodSet(String keyword) {
		return foodSetRepoistory.searchFoodSet(keyword);
	}

//	public boolean checkFoodIdAlreadyExist(int id, int foodId) {
//		List<FoodSet> foodSets = foodSetRepoistory.findFoodForFoodSet(id, foodId);
//		for (FoodSet foodSet : foodSets) {
//			if (foodSet == null) {
//				return false;
//			} else {
//				return true;
//			}
//		}
//		return false;
//	}

	public int insertFoodForFoodSet(FoodSet foodSet) {
		return foodSetRepoistory.insertFoodForFoodSet(foodSet);
	}

	public void deleteForFood(int id) {

		foodSetRepoistory.delete(id);
	}

	public boolean checkFoodIdAlreadyExistAtUpdate(int id, int foodId, int foodSetId) {
		List<FoodSet> foodSets = foodSetRepoistory.findFoodForFoodSet(foodId, foodSetId);

		for (FoodSet foodSet : foodSets) {
			if (foodSet == null) {
				return false;
			} else {

				if (id == foodSet.getFoodforfoodsetId()) {
					return true;
				}
			}
		}
		return false;

	}

	public void updateFoodForFoodSet(FoodSet foodSet) {
		foodSetRepoistory.updateFoodForFoodSet(foodSet);

	}

	public boolean checkFoodSetIsEmpty(FoodSet foodSet) {
		if (foodSet.getName() == null || foodSet.getName().equals("") || foodSet.getDescription() == null
				|| foodSet.getDescription().equals("") || foodSet.getPrice() == 0 || foodSet.getMaxQuantity() == 0) {
			return true;
		}
		return false;
	}

	public boolean checkFoodSetInput(FoodSet foodSet) {
		if (foodSet.getName().length() > 40 || foodSet.getDescription().length() > 255 || foodSet.getPrice() > 9999999
				|| foodSet.getMaxQuantity() > 100) {
			return true;
		}
		return false;
	}

}
