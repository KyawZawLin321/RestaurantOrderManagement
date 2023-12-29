package com.ai.jwd42.dto;

import java.time.LocalDateTime;

public class OrderMessage {

	private int id;
	private String rejectMessage;
	private String orderCompletionTime;
	private String deliveryArrivalTime;
	private String orderNumber;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private String restaurantName;
	private String restaurantPhoneNumber;
	private String status;

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getRestaurantPhoneNumber() {
		return restaurantPhoneNumber;
	}

	public void setRestaurantPhoneNumber(String restaurantPhoneNumber) {
		this.restaurantPhoneNumber = restaurantPhoneNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRejectMessage() {
		return rejectMessage;
	}

	public void setRejectMessage(String rejectMessage) {
		this.rejectMessage = rejectMessage;
	}

	public String getDeliveryArrivalTime() {
		return deliveryArrivalTime;
	}

	public void setDeliveryArrivalTime(String deliveryArrivalTime) {
		this.deliveryArrivalTime = deliveryArrivalTime;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderCompletionTime() {
		return orderCompletionTime;
	}

	public void setOrderCompletionTime(String orderCompletionTime) {
		this.orderCompletionTime = orderCompletionTime;
	}

}
