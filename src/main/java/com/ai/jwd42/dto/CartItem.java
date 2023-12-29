package com.ai.jwd42.dto;



public class CartItem {

	private String type;
	private int id;
	private String name;
	private double price;
	private boolean avaliable;
	private int quantity;
	private int maxQuantity;
	private int categoryId;
	private String category;
	private String image;
	private int restaurantId;
	private String description;
	private int orderId;
	private String orderNumber;
	private double deliveryCharge;
	private double taxCharge;


	public CartItem() {

	}

	public CartItem(String type, int id, String name, double price, int maxQuantity, String image, int restaurantId, int quantity ,boolean avaliable,String description) {
		super();
		this.type = type;
		this.id = id;
		this.name = name;
		this.price = price;
		this.maxQuantity = maxQuantity;
		this.image = image;
		this.restaurantId = restaurantId;
		this.quantity = quantity;
		this.avaliable = avaliable;
		this.description = description;
	}

	public CartItem(String type, int id, String name, double price, int maxQuantity,String category, String image,
			int restaurantId, int quantity , boolean avaliable, String description) {
		super();
		this.type = type;
		this.id = id;
		this.name = name;
		this.price = price;
		this.maxQuantity = maxQuantity;
		this.category = category;
		this.image = image;
		this.restaurantId = restaurantId;
		this.quantity =quantity;
		this.avaliable = avaliable;
		this.description = description;
	}




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

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isAvaliable() {
		return avaliable;
	}

	public void setAvaliable(boolean avaliable) {
		this.avaliable = avaliable;
	}

	public int getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(int maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}



}

