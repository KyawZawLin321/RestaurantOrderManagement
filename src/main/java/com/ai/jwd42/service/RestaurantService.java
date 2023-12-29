package com.ai.jwd42.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.jwd42.dto.Restaurant;
import com.ai.jwd42.repo.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	public List<Restaurant> findAllRestaurant() {

		return restaurantRepository.findAllRestaurant();
	}

	public Restaurant findRestaurantById(int id) {

		return restaurantRepository.findRestaurantById(id);
	}

	public List<Restaurant> findRestaurantByLimit() {

		return restaurantRepository.findRestaurantByLimit();
	}

	public Restaurant findRestaurantByEmail(String email) {
		Restaurant restaurant = restaurantRepository.findRestaurantByEmail(email);
		return restaurant;
	}

	public boolean checkEmailAlreadyExistAtUpdate(String email, int id) {

		Restaurant restaurant = restaurantRepository.findRestaurantByEmail(email);
		if (restaurant == null) {
			return false;
		} else {
			if (restaurant.getId() == id) {
				return false;
			}
			return true;
		}
	}

	public Restaurant findAllOwner(int id) {

		return restaurantRepository.findOwnerRestaurant(id);
	}

	public int addRestaurant(Restaurant restaurant) {
		return restaurantRepository.addRestaurant(restaurant);
	}

	public boolean checkEmailAlreadyExist(String email) {

		Restaurant restaurant = restaurantRepository.findRestaurantByEmail(email);
		if (restaurant == null) {
			return false;
		} else {
			return true;
		}
	}

	public void updateOwner(Restaurant restaurant) {
		restaurantRepository.updateOwner(restaurant);
	}

	public void updateRestaurant(Restaurant restaurant) {
		restaurantRepository.updateRestaurant(restaurant);
	}

	public void updateRestaurantDescription(Restaurant restaurant) {
		restaurantRepository.updateRestaurantDescription(restaurant);
	}

	public void updateRestaurantImg(Restaurant restaurant) {
		restaurantRepository.updateRestaurantImg(restaurant);
	}

	public void deleteRestaurant(int id) {
		restaurantRepository.deleteRestaurant(id);
	}

	public void deleteOwner(int id) {
		restaurantRepository.deleteOwner(id);
	}

	public void deleteRestaurantImg(int id) {

		restaurantRepository.deleteRestaurantImg(id);
	}

	public Restaurant findRestaurantProfileByEmail(String email) {

		return restaurantRepository.findRestaurantProfileByEmail(email);
	}

	public void updateRestaurantLogo(Restaurant restaurant) {
		restaurantRepository.updateRestaurantLogo(restaurant);

	}

	public boolean checkRestaurant(Restaurant restaurant) {
		if (restaurant.getName().length() > 40 || restaurant.getEmail().length() > 40
				|| restaurant.getPhone_number().length() > 20 || restaurant.getDescription().length() > 255
				|| restaurant.getAddress().length() > 255) {
			return true;
		}
		return false;
	}

	public List<Restaurant> searchRestaurant(String keyword) {
		return restaurantRepository.searchRestaurant(keyword);
	}

}
