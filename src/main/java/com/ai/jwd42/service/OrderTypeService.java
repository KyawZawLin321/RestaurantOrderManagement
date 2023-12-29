package com.ai.jwd42.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.jwd42.dto.OrderTypeForRestaurant;
import com.ai.jwd42.repo.OrderTypeRepository;

@Service
public class OrderTypeService {

	@Autowired
	private  OrderTypeRepository ordertyperepository;

	public  List<OrderTypeForRestaurant>findOrderTypeByRestaurantId(int id){
		List<OrderTypeForRestaurant> ordertypes=ordertyperepository.findOrderTypeByRestaurantId(id);

		return ordertypes;

	}

	public boolean checkOrderTypeAlreadyExist(String name) {
		OrderTypeForRestaurant ordertype=ordertyperepository.findOrderByOrderType(name);

		if(ordertype==null) {
			return false;
		}else {
			return true;
		}
	}
	public  List<OrderTypeForRestaurant>findOrderType(){
		List<OrderTypeForRestaurant> OrderTypes=ordertyperepository.findOrderType();
		return OrderTypes;

	}
	public void delete(int id) {

		ordertyperepository.deleteOrdertype(id);
	}
	public void updateOrdertype(OrderTypeForRestaurant ordertype) {
		ordertyperepository.updateOrdertype(ordertype);
	}

	public int insert(OrderTypeForRestaurant ordertype){
		return ordertyperepository.insert(ordertype);
	}
	public boolean checkOrderTypeAlreadyExist(int id,int restaurantId) {
		OrderTypeForRestaurant orderType=ordertyperepository.findOrderTypeById(id , restaurantId);
		if(orderType==null) {
			return false;
		}else {
			return true;
		}
	}

}
