package com.ai.jwd42.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.jwd42.dto.CartItem;
import com.ai.jwd42.dto.Order;
import com.ai.jwd42.dto.OrderMessage;
import com.ai.jwd42.dto.OrderReceipt;
import com.ai.jwd42.dto.OrderTypeForRestaurant;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.repo.OrderRepoistory;
import com.ai.jwd42.repo.UserRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepoistory orderRepoistory;

	@Autowired
	private UserRepository userRepository;

	public int orderRowCount(String email) {
		/* Food food = foodRepoistory.findFoodByEmail(email); */
		User user = userRepository.findUserByEmail(email);
		if (user == null) {
			return 0;
		} else {
			return orderRepoistory.orderRowCount(user.getRestaurantId());
		}
	}

	public List<OrderTypeForRestaurant> findOrderTypeByRestaurantId(int id) {

		return orderRepoistory.findOrderTypeByRestaurantId(id);
	}

	public void insertOrder(Order order) {
		orderRepoistory.insertOrder(order);

	}

	public void insertOrderItem(List<CartItem> cartItems) {
		orderRepoistory.insertOrderItem(cartItems);

	}

	public List<Order> findPendingOrder(int id) {
		return orderRepoistory.findPendingOrder(id);
	}

	public List<CartItem> findOrderItemByOrderNumber(String orderNumber) {
		return orderRepoistory.findOrderItemByOrderNumber(orderNumber);
	}

	public void confirmOrder(String orderNumber) {
		orderRepoistory.confirmOrder(orderNumber);
	}

	public List<Order> findAllOrder(int id) {
		return orderRepoistory.findAllOrder(id);
	}

	public List<Order> findConfirmOrder(int id) {
		return orderRepoistory.findConfirmOrder(id);
	}

	public List<Order> findOrderByUserId(int id) {

		return orderRepoistory.findOrderByUserId(id);
	}


	public void insertOrderReceipt(String orderNumber) {
		orderRepoistory.insertOrderReceipt(orderNumber);

	}

	public OrderReceipt findOrderReceiptByOrderNumber(String orderNumber) {

		return orderRepoistory.findOrderReceiptByOrderNumber(orderNumber);
	}

	public Order findOrderInfo(String orderNumber) {

		return orderRepoistory.findOrderInfo(orderNumber);
	}

	public void insertOrderMessage(OrderMessage orderMessage) {
		orderRepoistory.insertOrderMessage(orderMessage);

	}

	public void rejectOrder(String orderNumber) {
		orderRepoistory.rejectOrder(orderNumber);

	}

	public List<Order> findRejectOrder(int id) {

		 return orderRepoistory.findRejectOrder(id);
	}

	public List<OrderMessage> findOrderMessage(int id) {

		return orderRepoistory.findOrderMessage(id);
	}

	public int findUnreadOrderMesssage(int id) {
		return orderRepoistory.findUnreadOrderMessage(id);
	}

	public void readOrderMessage(List<OrderMessage> messages) {
		orderRepoistory.updateReadOrderMessage(messages);

	}

}
