package com.ai.jwd42.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ai.jwd42.dto.Order;
import com.ai.jwd42.dto.PaymentTypeForRestaurant;

@Repository
public class PaymentRepository extends BaseRepoistory {

	public List<PaymentTypeForRestaurant> findPaymentTypeByRestaurantId(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from payment_type_for_restaurant p inner join payment_type t on p.payment_type_id=t.id where p.restaurant_id= ?; ");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<PaymentTypeForRestaurant> paymentForRestaurants = new ArrayList<>();
			while (rs.next()) {
				PaymentTypeForRestaurant paymentForRestaurant = new PaymentTypeForRestaurant();
				paymentForRestaurant.setId(rs.getInt(1));
				paymentForRestaurant.setPhoneNumber(rs.getString(2));
				paymentForRestaurant.setAccountNumber(rs.getString(3));
				Timestamp timestampupdate = rs.getTimestamp(5);
				if (timestampupdate != null) {
					paymentForRestaurant.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(4);
				if (timestampcreate != null) {
					paymentForRestaurant.setCreateDate(timestampcreate.toLocalDateTime());
				}
				paymentForRestaurant.setRestaurantId(rs.getInt(6));
				paymentForRestaurant.setPaymentTypeId(rs.getInt(7));
				paymentForRestaurant.setQrcode(rs.getString(8));
				paymentForRestaurant.setPaymentTypeName(rs.getString(10));

				paymentForRestaurants.add(paymentForRestaurant);
			}
			return paymentForRestaurants;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void insertUserPaymentInfo(Order order) {
		Connection con = getConnection();

		try {
			PreparedStatement ps = con.prepareStatement(
					"insert into user_payment_info(screen_shot,order_number) value( ? , ? );");
			ps.setString(1, order.getScreenShot());
			ps.setString(2, order.getOrderNumber());

			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
