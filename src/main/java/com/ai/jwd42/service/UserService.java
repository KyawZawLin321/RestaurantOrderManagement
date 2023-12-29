package com.ai.jwd42.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.jwd42.dto.Restaurant;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<User> findCustomer(int id) {

		List<User> users = userRepository.findCustomer(id);
		return users;
	}

	public boolean checkEmailandPasswordForUser(String email, String password) {

		User user = userRepository.findUserByEmail(email);
		if (user == null) {
			return false;
		} else {
			if (user.getRoleId() == 1) {
				if (BCrypt.checkpw(password, user.getPassword())) {

					return true;
				}
			}

		}

		return false;
	}

	public boolean checkEmailandPasswordForOwner(String email, String password) {

		User user = userRepository.findUserByEmail(email);
		if (user == null) {
			return false;
		} else {
			if (BCrypt.checkpw(password, user.getPassword()) && user.getRoleId() == 2) {
				return true;
			}
		}

		return false;
	}

	public boolean checkEmailandPasswordForAdmin(String email, String password) {

		User user = userRepository.findUserByEmail(email);
		if (user == null) {
			return false;
		} else {
			if (BCrypt.checkpw(password, user.getPassword()) && user.getRoleId() == 3) {
				return true;
			}
		}

		return false;
	}

	public User findUserByEmail(String email) {
		User user = userRepository.findUserByEmail(email);
		return user;
	}

	public List<User> findAllUserRole() {

		return userRepository.findAllUserRole();
	}

	public List<User> findAllUser() {

		return userRepository.findAllUser();
	}

	public boolean checkPhoneNumberAlreadyExist(String phone_number) {

		User user = userRepository.findUserByPhoneNumber(phone_number);
		if (user == null) {
			return false;
		} else {
			return true;
		}
	}

	public int userRowCount(String email) {
		User user = userRepository.findUserByEmail(email);
		if (user == null) {
			return 0;
		} else {
			return userRepository.userRowCount(user.getRestaurantId());
		}
	}

	public int userRow() {
		return userRepository.countrow();
	}

	public int ownerRow() {
		return userRepository.ownercountrow();
	}

	public int restaurantRow() {
		return userRepository.restaurantcountrow();
	}

	public boolean checkPhoneNumberAlreadyExistAtUpdate(String phone_number, int id) {

		User user = userRepository.findUserByPhoneNumber(phone_number);
		if (user == null) {
			return false;
		} else if (user.getId() == id) {
			return false;
		} else {
			return true;
		}
	}

	public boolean checkEmailAlreadyExist(String email) {

		User user = userRepository.findUserByEmail(email);
		if (user == null) {
			return false;
		} else {
			return true;
		}
	}

	public boolean checkEmailAlreadyExistAtUpdate(String email, int id) {

		User user = userRepository.findUserByEmail(email);
		if (user == null) {
			return false;
		} else {
			if (user.getId() == id) {
				return false;
			}
			return true;
		}
	}

	public int insertUser(User user) {

		return userRepository.insertUser(user);
	}

	public void updateUser(User user) {

		userRepository.updateUser(user);
	}

	public void deleteUser(int id) {

		userRepository.deleteUser(id);
	}

	public void changePassword(String passowrd, String email) {

		userRepository.changePassword(email, passowrd);

	}

	public User findByEmail(String email) {
		UserRepository userRepo = new UserRepository();
		return userRepo.findUserByEmail(email);
	}

	public boolean checkOwner(Restaurant restaurant) {
		if (restaurant.getOwnername().length() > 40 || restaurant.getOwneremail().length() > 40
				|| restaurant.getOwnerPhone().length() > 20 || restaurant.getOwneraddress().length() > 255) {
			return true;
		}
		return false;
	}

	public boolean checkUserIsEmpty(User user) {
		if (user.getName() == null || user.getName().equals("") || user.getEmail() == null || user.getEmail().equals("")
				|| user.getPhoneNumber() == null || user.getPhoneNumber().equals("") || user.getAddress() == null
				|| user.getAddress().equals("")) {
			return true;
		}
		return false;
	}

	public boolean checkUserInput(User user) {
		if (user.getName().length() > 40 || user.getEmail().length() > 40 || user.getPhoneNumber().length() > 20
				|| user.getAddress().length() > 255) {
			return true;
		}
		return false;
	}

}
