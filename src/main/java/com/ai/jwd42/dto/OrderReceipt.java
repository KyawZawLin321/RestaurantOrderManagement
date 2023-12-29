package com.ai.jwd42.dto;

import java.time.LocalDateTime;

public class OrderReceipt {
	private int id;
	private boolean hasTaken;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private String orderNumber;
	private double totalAmount;
	private double unitPrice;
	private int quantity;
	private String restaurantName;
	private String restaurantEmail;
	private String restaurantPhoneNumber;
	private String restaurantAddress;
	private String status;
	private int userId;
	private int orderTypeId;
	private int paymentTypeId;
	private String deliveryAddress;
	private double deliveryCharge;
	private double taxCharge;
	private String qrcode;
	private String itemName;

	public double getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(double deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public double getTaxCharge() {
		return taxCharge;
	}

	public void setTaxCharge(double taxCharge) {
		this.taxCharge = taxCharge;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getOrderTypeId() {
		return orderTypeId;
	}

	public void setOrderTypeId(int orderTypeId) {
		this.orderTypeId = orderTypeId;
	}

	public int getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(int paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isHasTaken() {
		return hasTaken;
	}

	public void setHasTaken(boolean hasTaken) {
		this.hasTaken = hasTaken;
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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getRestaurantEmail() {
		return restaurantEmail;
	}

	public void setRestaurantEmail(String restaurantEmail) {
		this.restaurantEmail = restaurantEmail;
	}

	public String getRestaurantAddress() {
		return restaurantAddress;
	}

	public void setRestaurantAddress(String restaurantAddress) {
		this.restaurantAddress = restaurantAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getRestaurantPhoneNumber() {
		return restaurantPhoneNumber;
	}

	public void setRestaurantPhoneNumber(String restaurantPhoneNumber) {
		this.restaurantPhoneNumber = restaurantPhoneNumber;
	}



}
