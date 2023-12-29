package com.ai.jwd42.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.jwd42.dto.PaymentTypeForRestaurant;
import com.ai.jwd42.repo.PaymentTypeRepository;

@Service
public class PaymentTypeService {

	@Autowired
	private PaymentTypeRepository paymenttyperepository;

	public List<PaymentTypeForRestaurant> findPaymentByRestaurantId(int id) {
		List<PaymentTypeForRestaurant> paymenttypes = paymenttyperepository.findPaymentByRestaurantId(id);

		return paymenttypes;

	}

	public boolean checkPaymentTypeAlreadyExist(String name) {
		PaymentTypeForRestaurant paymenttype = paymenttyperepository.findPaymentByPaymentType(name);

		if (paymenttype == null) {
			return false;
		} else {
			return true;
		}
	}

	public void delete(int id) {

		paymenttyperepository.deletePaymenttype(id);
	}

	public void updatePaymenttype(PaymentTypeForRestaurant paymenttype) {
		paymenttyperepository.updatePaymenttype(paymenttype);
	}

	public void updatePayMentTypeBank(PaymentTypeForRestaurant paymenttype) {
		paymenttyperepository.updatePaymenttypeBank(paymenttype);
	}

	public List<PaymentTypeForRestaurant> findPayMent() {
		List<PaymentTypeForRestaurant> paymenttypes = paymenttyperepository.findPayMent();

		return paymenttypes;

	}

	public boolean checkPayMentAlreadyExist(int id, int restaurantId) {
		PaymentTypeForRestaurant payMentType = paymenttyperepository.findPayMentByName(id, restaurantId);
		if (payMentType == null) {
			return false;
		} else {
			return true;
		}
	}

	public int insert(PaymentTypeForRestaurant paymenttype) {
		return paymenttyperepository.insert(paymenttype);
	}

	public int insertBank(PaymentTypeForRestaurant paymenttype) {
		return paymenttyperepository.insertBank(paymenttype);
	}

	public boolean checkPaymentInput(PaymentTypeForRestaurant paymentTypeForRestaurant) {
		if (paymentTypeForRestaurant.getAccountNumber().equals("")
				|| paymentTypeForRestaurant.getAccountNumber().equals(null)) {
			if (paymentTypeForRestaurant.getPhoneNumber().length() > 20) {
				return true;
			}
		} else {
			if (paymentTypeForRestaurant.getAccountNumber().length() > 19) {
				return true;
			}
		}
		return false;
	}
}
