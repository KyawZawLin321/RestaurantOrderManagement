package com.ai.jwd42.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ai.jwd42.dto.CartItem;
import com.ai.jwd42.dto.Order;
import com.ai.jwd42.dto.OrderMessage;
import com.ai.jwd42.dto.OrderReceipt;
import com.ai.jwd42.dto.OrderTypeForRestaurant;

@Repository
public class OrderRepoistory extends BaseRepoistory {

	public int orderRowCount(int id) {
		Connection con = getConnection();
		int rowCount = 0;
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT COUNT(*) from order_table  where restaurant_id= ? and status='pending' ; ");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				rowCount = rs.getInt(1);
				System.out.println("Number of rows in the role table: " + rowCount);

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return rowCount;
	}

	public List<OrderTypeForRestaurant> findOrderTypeByRestaurantId(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from order_type_for_restaurant o inner join order_type t on o.order_type_id=t.id where o.restaurant_id=? ; ");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<OrderTypeForRestaurant> orderForRestaurants = new ArrayList<>();
			while (rs.next()) {
				OrderTypeForRestaurant orderForRestaurant = new OrderTypeForRestaurant();
				orderForRestaurant.setId(rs.getInt(1));
				orderForRestaurant.setOrderTypeId(rs.getInt(2));
				orderForRestaurant.setRestaurantId(rs.getInt(3));
				Timestamp timestampupdate = rs.getTimestamp(5);
				if (timestampupdate != null) {
					orderForRestaurant.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(4);
				if (timestampcreate != null) {
					orderForRestaurant.setCreateDate(timestampcreate.toLocalDateTime());
				}
				orderForRestaurant.setOrderTypeName(rs.getString(7));

				orderForRestaurants.add(orderForRestaurant);
			}
			return orderForRestaurants;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void insertOrder(Order order) {
		Connection con = getConnection();

		try {
			PreparedStatement ps = con.prepareStatement(
					"insert into order_table(order_number,create_date,user_id,total_price,order_type_id,payment_type_id,delivery_address,delivery_charge,tax_charge,restaurant_id,qrcode) values(?,?,?,?,?,?,?,?,?,?,?);");
			ps.setString(1, order.getOrderNumber());
			ps.setTimestamp(2, Timestamp.valueOf(order.getCreateDate()));
			ps.setInt(3, order.getUserId());
			ps.setDouble(4, order.getTotalPrice());
			ps.setInt(5, order.getOrderTypeId());
			ps.setInt(6, order.getPaymentTypeId());
			ps.setString(7, order.getDeliveryAddress());
			ps.setDouble(8, order.getDeliveryCharge());
			ps.setDouble(9, order.getTaxCharge());
			ps.setInt(10, order.getRestaurantId());
			ps.setString(11, order.getQrcode());
			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void insertOrderItem(List<CartItem> cartItems) {
		Connection con = getConnection();

		try {
			PreparedStatement ps = con.prepareStatement(
					"insert into order_item(quantity,price,food_id,food_set_id,order_number) values(?,?,?,?,?);");

			for (CartItem cartItem : cartItems) {
				ps.setInt(1, cartItem.getQuantity());
				ps.setDouble(2, cartItem.getPrice());
				if (cartItem.getType().equalsIgnoreCase("food")) {
					ps.setInt(3, cartItem.getId());
					ps.setNull(4, java.sql.Types.INTEGER);
				} else {
					ps.setInt(4, cartItem.getId());
					ps.setNull(3, java.sql.Types.INTEGER);
				}
				ps.setString(5, cartItem.getOrderNumber());
				ps.executeUpdate();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public List<Order> findPendingOrder(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from order_table o inner join payment_type pt on pt.id=o.payment_type_id inner join order_type ot on ot.id=o.order_type_id inner join user u on o.user_id=u.id  where o.restaurant_id= ? and status='pending' ;");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<Order> orders = new ArrayList<>();
			while (rs.next()) {
				Order order = new Order();
				order.setId(rs.getInt(1));
				order.setOrderNumber(rs.getString(2));
				order.setStatus(rs.getString(3));
				Timestamp timestampupdate = rs.getTimestamp(5);
				if (timestampupdate != null) {
					order.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(4);
				if (timestampcreate != null) {
					order.setCreateDate(timestampcreate.toLocalDateTime());
				}
				order.setUserId(rs.getInt(6));
				order.setTotalPrice(rs.getDouble(7));
				order.setDeliveryAddress(rs.getString(10));
				order.setDeliveryCharge(rs.getDouble(11));
				order.setTaxCharge(rs.getDouble(12));
				order.setPaymentTypeName(rs.getString(16));
				order.setOrderTypeName(rs.getString(18));
				order.setCustomerEmail(rs.getString(21));

				orders.add(order);
			}
			return orders;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	public List<CartItem> findOrderItemByOrderNumber(String orderNumber) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select oi.id,oi.quantity,oi.price,oi.order_number,o.delivery_charge,o.tax_charge,f.name,fs.name,fi.path,fsi.path from order_item oi inner join order_table o on o.order_number=oi.order_number left join food f on f.id=oi.food_id left join food_set fs on fs.id=oi.food_set_id left join food_image fi on fi.food_id=f.id left join food_set_image fsi on fsi.food_set_id=fs.id where o.order_number= ?;");

			ps.setString(1, orderNumber);
			ResultSet rs = ps.executeQuery();
			List<CartItem> orderItems = new ArrayList<>();
			while (rs.next()) {
				CartItem orderItem = new CartItem();
				orderItem.setId(rs.getInt(1));
				orderItem.setQuantity(rs.getInt(2));
				orderItem.setPrice(rs.getDouble(3));
				orderItem.setOrderNumber(rs.getString(4));
				orderItem.setDeliveryCharge(rs.getDouble(5));
				orderItem.setTaxCharge(rs.getDouble(6));

				if (rs.getString(8) == null) {
					orderItem.setName(rs.getString(7));
					orderItem.setImage(rs.getString(9));
				} else {
					orderItem.setName(rs.getString(8));
					orderItem.setImage(rs.getString(10));
				}

				orderItems.add(orderItem);
			}
			return orderItems;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void confirmOrder(String orderNumber) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("update order_table set status='confirm' where order_number= ? ;");
			ps.setString(1, orderNumber);

			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public List<Order> findAllOrder(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from order_table o inner join payment_type pt on pt.id=o.payment_type_id inner join order_type ot on ot.id=o.order_type_id inner join user u on o.user_id=u.id  where o.restaurant_id= ? ;");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<Order> orders = new ArrayList<>();
			while (rs.next()) {
				Order order = new Order();
				order.setId(rs.getInt(1));
				order.setOrderNumber(rs.getString(2));
				order.setStatus(rs.getString(3));
				Timestamp timestampupdate = rs.getTimestamp(5);
				if (timestampupdate != null) {
					order.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(4);
				if (timestampcreate != null) {
					order.setCreateDate(timestampcreate.toLocalDateTime());
				}
				order.setUserId(rs.getInt(6));
				order.setTotalPrice(rs.getDouble(7));
				order.setDeliveryAddress(rs.getString(10));
				order.setDeliveryCharge(rs.getDouble(11));
				order.setTaxCharge(rs.getDouble(12));
				order.setPaymentTypeName(rs.getString(15));
				order.setOrderTypeName(rs.getString(17));
				order.setCustomerEmail(rs.getString(20));

				orders.add(order);
			}
			return orders;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public List<Order> findConfirmOrder(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from order_table o inner join payment_type pt on pt.id=o.payment_type_id inner join order_type ot on ot.id=o.order_type_id inner join user u on o.user_id=u.id  where o.restaurant_id= ?  and status='confirm' ;");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<Order> orders = new ArrayList<>();
			while (rs.next()) {
				Order order = new Order();
				order.setId(rs.getInt(1));
				order.setOrderNumber(rs.getString(2));
				order.setStatus(rs.getString(3));
				Timestamp timestampupdate = rs.getTimestamp(5);
				if (timestampupdate != null) {
					order.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(4);
				if (timestampcreate != null) {
					order.setCreateDate(timestampcreate.toLocalDateTime());
				}
				order.setUserId(rs.getInt(6));
				order.setTotalPrice(rs.getDouble(7));
				order.setDeliveryAddress(rs.getString(10));
				order.setDeliveryCharge(rs.getDouble(11));
				order.setTaxCharge(rs.getDouble(12));
				order.setPaymentTypeName(rs.getString(16));
				order.setOrderTypeName(rs.getString(18));
				order.setCustomerEmail(rs.getString(21));

				orders.add(order);
			}
			return orders;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public List<Order> findRejectOrder(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from order_table o inner join payment_type pt on pt.id=o.payment_type_id inner join order_type ot on ot.id=o.order_type_id inner join user u on o.user_id=u.id  where o.restaurant_id= ?  and status='reject' ;");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<Order> orders = new ArrayList<>();
			while (rs.next()) {
				Order order = new Order();
				order.setId(rs.getInt(1));
				order.setOrderNumber(rs.getString(2));
				order.setStatus(rs.getString(3));
				Timestamp timestampupdate = rs.getTimestamp(5);
				if (timestampupdate != null) {
					order.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(4);
				if (timestampcreate != null) {
					order.setCreateDate(timestampcreate.toLocalDateTime());
				}
				order.setUserId(rs.getInt(6));
				order.setTotalPrice(rs.getDouble(7));
				order.setDeliveryAddress(rs.getString(10));
				order.setDeliveryCharge(rs.getDouble(11));
				order.setTaxCharge(rs.getDouble(12));
				order.setPaymentTypeName(rs.getString(16));
				order.setOrderTypeName(rs.getString(18));
				order.setCustomerEmail(rs.getString(21));

				orders.add(order);
			}
			return orders;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public List<Order> findOrderByUserId(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from order_table o inner join payment_type pt on pt.id=o.payment_type_id inner join order_type ot on ot.id=o.order_type_id inner join restaurant r on o.restaurant_id=r.id   where o.user_id= ? ;");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<Order> orders = new ArrayList<>();
			while (rs.next()) {
				Order order = new Order();
				order.setId(rs.getInt(1));
				order.setOrderNumber(rs.getString(2));
				order.setStatus(rs.getString(3));
				Timestamp timestampupdate = rs.getTimestamp(5);
				if (timestampupdate != null) {
					order.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(4);
				if (timestampcreate != null) {
					order.setCreateDate(timestampcreate.toLocalDateTime());
				}
				order.setUserId(rs.getInt(6));
				order.setTotalPrice(rs.getDouble(7));
				order.setDeliveryAddress(rs.getString(10));
				order.setDeliveryCharge(rs.getDouble(11));
				order.setTaxCharge(rs.getDouble(12));
				order.setRestaurantId(rs.getInt(13));
				order.setPaymentTypeName(rs.getString(16));
				order.setOrderTypeName(rs.getString(18));
				order.setRestaurantName(rs.getString(20));

				orders.add(order);
			}
			return orders;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public List<OrderReceipt> getOrderReceipt(String orderNumber) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select ore.id,o.order_number,o.status,o.user_id,o.total_price,o.order_type_id,o.payment_type_id,o.delivery_address,o.delivery_charge,o.tax_charge,o.qrcode,ore.has_taken,ore.create_date,ore.update_date,r.name,r.address,r.email,r.phone_number,oi.quantity,oi.price,f.name,fs.name from order_table o "
							+ "inner join order_receipt ore on ore.order_number=o.order_number inner join restaurant r on r.id=o.restaurant_id inner join order_item oi on oi.order_number=o.order_number left join food f on f.id=oi.food_id left join food_set fs on fs.id=oi.food_set_id where o.order_number= ? ;");

			ps.setString(1, orderNumber);
			ResultSet rs = ps.executeQuery();
			List<OrderReceipt> orderReceipts = new ArrayList<>();
			while (rs.next()) {
				OrderReceipt orderReceipt = new OrderReceipt();
				orderReceipt.setId(rs.getInt(1));
				orderReceipt.setOrderNumber(rs.getString(2));
				orderReceipt.setStatus(rs.getString(3));
				orderReceipt.setUserId(rs.getInt(4));
				orderReceipt.setTotalAmount(rs.getDouble(5));
				orderReceipt.setOrderTypeId(rs.getInt(6));
				orderReceipt.setPaymentTypeId(rs.getInt(7));
				orderReceipt.setDeliveryAddress(rs.getString(8));
				orderReceipt.setDeliveryCharge(rs.getDouble(9));
				orderReceipt.setTaxCharge(rs.getDouble(10));
				orderReceipt.setQrcode(rs.getString(11));
				orderReceipt.setHasTaken(rs.getBoolean(12));
				Timestamp timestampupdate = rs.getTimestamp(14);
				if (timestampupdate != null) {
					orderReceipt.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(13);
				if (timestampcreate != null) {
					orderReceipt.setCreateDate(timestampcreate.toLocalDateTime());
				}
				orderReceipt.setRestaurantName(rs.getString(15));
				orderReceipt.setRestaurantAddress(rs.getString(16));
				orderReceipt.setRestaurantEmail(rs.getString(17));
				orderReceipt.setRestaurantPhoneNumber(rs.getString(18));
				orderReceipt.setQuantity(rs.getInt(19));
				orderReceipt.setUnitPrice(rs.getDouble(20));

				if (rs.getString(22) == null) {
					orderReceipt.setItemName(rs.getString(21));
				} else {
					orderReceipt.setItemName(rs.getString(22));
				}

				orderReceipts.add(orderReceipt);
			}
			return orderReceipts;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void insertOrderReceipt(String orderNumber) {
		Connection con = getConnection();

		try {
			PreparedStatement ps = con
					.prepareStatement("insert into order_receipt(create_date,order_number) value(?,?);");

			LocalDateTime createDate = LocalDateTime.now();
			ps.setTimestamp(1, Timestamp.valueOf(createDate));
			ps.setString(2, orderNumber);

			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public OrderReceipt findOrderReceiptByOrderNumber(String orderNumber) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select ore.id,o.order_number,o.status,o.user_id,o.total_price,o.order_type_id,o.payment_type_id,o.delivery_address,o.delivery_charge,o.tax_charge,o.qrcode,ore.has_taken,ore.create_date,ore.update_date,r.name,r.address,r.email,r.phone_number from order_table o inner join order_receipt ore on ore.order_number=o.order_number inner join restaurant r on r.id=o.restaurant_id  where o.order_number= ? ;");

			ps.setString(1, orderNumber);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OrderReceipt orderReceipt = new OrderReceipt();
				orderReceipt.setId(rs.getInt(1));
				orderReceipt.setOrderNumber(rs.getString(2));
				orderReceipt.setStatus(rs.getString(3));
				orderReceipt.setUserId(rs.getInt(4));
				orderReceipt.setTotalAmount(rs.getDouble(5));
				orderReceipt.setOrderTypeId(rs.getInt(6));
				orderReceipt.setPaymentTypeId(rs.getInt(7));
				orderReceipt.setDeliveryAddress(rs.getString(8));
				orderReceipt.setDeliveryCharge(rs.getDouble(9));
				orderReceipt.setTaxCharge(rs.getDouble(10));
				orderReceipt.setQrcode(rs.getString(11));
				orderReceipt.setHasTaken(rs.getBoolean(12));
				Timestamp timestampupdate = rs.getTimestamp(14);
				if (timestampupdate != null) {
					orderReceipt.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(13);
				if (timestampcreate != null) {
					orderReceipt.setCreateDate(timestampcreate.toLocalDateTime());
				}
				orderReceipt.setRestaurantName(rs.getString(15));
				orderReceipt.setRestaurantAddress(rs.getString(16));
				orderReceipt.setRestaurantEmail(rs.getString(17));
				orderReceipt.setRestaurantPhoneNumber(rs.getString(18));

				return orderReceipt;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public Order findOrderInfo(String orderNumber) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select up.id,up.screen_shot,up.order_number,o.delivery_address,o.total_price,pt.name,ot.name from user_payment_info up inner join order_table o on o.order_number=up.order_number inner join payment_type pt on pt.id=o.payment_type_id inner join order_type ot on ot.id=o.order_type_id where up.order_number= ? ;");

			ps.setString(1, orderNumber);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Order order = new Order();
				order.setId(rs.getInt(1));
				order.setScreenShot(rs.getString(2));
				order.setOrderNumber(rs.getString(3));
				order.setDeliveryAddress(rs.getString(4));
				order.setTotalPrice(rs.getDouble(5));
				order.setPaymentTypeName(rs.getString(6));
				order.setOrderTypeName(rs.getString(7));

				return order;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void insertOrderMessage(OrderMessage orderMessage) {
		Connection con = getConnection();

		try {
			PreparedStatement ps = con.prepareStatement(
					"insert into order_message(reject_message,order_completion_time,delivery_arrival_time,create_date,order_number) value(?,?,?,?,?);");

			LocalDateTime createDate = LocalDateTime.now();

			ps.setString(1, orderMessage.getRejectMessage());
			ps.setString(2, orderMessage.getOrderCompletionTime());
			ps.setString(3, orderMessage.getDeliveryArrivalTime());
			ps.setTimestamp(4, Timestamp.valueOf(createDate));
			ps.setString(5, orderMessage.getOrderNumber());

			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void rejectOrder(String orderNumber) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("update order_table set status='reject' where order_number= ? ;");
			ps.setString(1, orderNumber);

			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public List<OrderMessage> findOrderMessage(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select om.id,om.reject_message,om.order_completion_time,om.delivery_arrival_time,om.create_date,om.order_number,o.status,r.name,r.phone_number from order_message om inner join order_table o on o.order_number=om.order_number inner join restaurant r on r.id= o.restaurant_id inner join user u on u.id=o.user_id where o.user_id= ? and u.role_id= 1 ;");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<OrderMessage> orderMessages = new ArrayList<>();
			while (rs.next()) {
				OrderMessage orderMessage = new OrderMessage();
				orderMessage.setId(rs.getInt(1));
				orderMessage.setRejectMessage(rs.getString(2));
				orderMessage.setOrderCompletionTime(rs.getString(3));
				orderMessage.setDeliveryArrivalTime(rs.getString(4));
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					orderMessage.setCreateDate(timestampcreate.toLocalDateTime());
				}
				orderMessage.setOrderNumber(rs.getString(6));
				orderMessage.setStatus(rs.getString(7));
				orderMessage.setRestaurantName(rs.getString(8));
				orderMessage.setRestaurantPhoneNumber(rs.getString(9));

				orderMessages.add(orderMessage);
			}
			return orderMessages;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public int findUnreadOrderMessage(int id) {
		Connection con = getConnection();
		int rowCount=0;
		try {
			PreparedStatement ps = con.prepareStatement(
					"select COUNT(*) from order_message om inner join order_table o on o.order_number=om.order_number  where o.user_id= ? and has_read=0 ;");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				rowCount = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return rowCount;
	}

	public void updateReadOrderMessage(List<OrderMessage> messages) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("update order_message set has_read=true where id= ?;");

			for(OrderMessage message : messages) {
			ps.setInt(1, message.getId());

			ps.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
