package com.ai.jwd42.dto;

import java.time.LocalDateTime;

public class User {
	private int id;
	private String name;
	private String password;
	private String email;
	private String phoneNumber;
	private String loginDateTime;
	private boolean rememberMe;
	private String newPassword;
	private String comfirmPassword;
	private String address;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private int roleId;
	private int restaurantId;
	private String role;

	public User() {

	}

	public User(String email, String role) {
		super();
		this.email = email;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLoginDateTime() {
		return loginDateTime;
	}

	public void setLoginDateTime(String loginDateTime) {
		this.loginDateTime = loginDateTime;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getComfirmPassword() {
		return comfirmPassword;
	}

	public void setComfirmPassword(String comfirmPassword) {
		this.comfirmPassword = comfirmPassword;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
