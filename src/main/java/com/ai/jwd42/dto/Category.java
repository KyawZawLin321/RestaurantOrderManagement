package com.ai.jwd42.dto;

import java.time.LocalDateTime;

public class Category {
	private int id;
	private String foodName;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private int Restaurant_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
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

	public int getRestaurant_id() {
		return Restaurant_id;
	}

	public void setRestaurant_id(int restaurant_id) {
		Restaurant_id = restaurant_id;
	}

}
