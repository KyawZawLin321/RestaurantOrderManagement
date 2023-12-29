package com.ai.jwd42.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.jwd42.dto.Order;
import com.ai.jwd42.dto.PaymentTypeForRestaurant;
import com.ai.jwd42.repo.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	PaymentRepository paymentRepository;

	public List<PaymentTypeForRestaurant> findPaymentTypeByRestaurantId(int id) {

		return paymentRepository.findPaymentTypeByRestaurantId(id);
	}

	public void insertUserPaymentInfo(Order order) {
		paymentRepository.insertUserPaymentInfo(order);

	}

}
