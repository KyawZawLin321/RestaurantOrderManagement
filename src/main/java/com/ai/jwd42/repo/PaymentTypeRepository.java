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

import com.ai.jwd42.dto.PaymentTypeForRestaurant;

@Repository
public class PaymentTypeRepository extends BaseRepoistory{


	public List<PaymentTypeForRestaurant> findPaymentByRestaurantId(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT ptr.id,payment_type.name,ptr.phone_number,ptr.account_number,ptr.create_date,ptr.update_date FROM payment_type INNER JOIN payment_type_for_restaurant ptr ON payment_type.id = ptr.payment_type_id WHERE restaurant_id = ?;");
			ps.setInt(1,id);
			ResultSet rs = ps.executeQuery();
			List<PaymentTypeForRestaurant> paymenttypes = new ArrayList<>();
			while (rs.next()) {
				PaymentTypeForRestaurant paymenttype = new PaymentTypeForRestaurant();
				paymenttype.setId(rs.getInt(1));
				paymenttype.setPaymentTypeName(rs.getString(2));
				paymenttype.setPhoneNumber(rs.getString(3));
				paymenttype.setAccountNumber(rs.getString(4));
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					paymenttype.setCreateDate(timestampcreate.toLocalDateTime());
				}
				Timestamp timestampupdate = rs.getTimestamp(6);
				if (timestampupdate != null) {
				    paymenttype.setUpdateDate(timestampupdate.toLocalDateTime());
				}

				paymenttypes.add(paymenttype);
			}


			return paymenttypes;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public PaymentTypeForRestaurant findById(int id) {
		Connection con=getConnection();
		try {
			PreparedStatement ps=con.prepareStatement("Select * from payment_type where id=?");
			ps.setInt(1,id);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				PaymentTypeForRestaurant paymenttype = new PaymentTypeForRestaurant();
				paymenttype.setId(rs.getInt(1));
				paymenttype.setPaymentTypeName(rs.getString(2));


				return paymenttype;

		}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public PaymentTypeForRestaurant findPaymentByPaymentType(String name) {
		Connection con=getConnection();
		try {
			PreparedStatement ps=con.prepareStatement("select * from payment_type where name=?");
			ps.setString(1,name);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				PaymentTypeForRestaurant paymenttype = new PaymentTypeForRestaurant();
				paymenttype.setId(rs.getInt(1));
				paymenttype.setPaymentTypeName(rs.getString(2));

				return paymenttype;
			}
		}catch(SQLException e ){
			System.out.println(e.getMessage());
		}
		return null;
	}


	public void deletePaymenttype(int id) {
		Connection con=getConnection();
		try {
			PreparedStatement ps=con.prepareStatement("Delete from payment_type_for_restaurant where id=?");
			ps.setInt(1,id);
			ps.execute();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updatePaymenttype(PaymentTypeForRestaurant paymenttype) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update payment_type_for_restaurant set phone_number=?,update_date=?,qrcode=? where id = ?;");
			ps.setString(1, paymenttype.getPhoneNumber());
		    LocalDateTime updateDate = LocalDateTime.now();
			ps.setObject(2,updateDate);
			ps.setString(3, paymenttype.getQrcode());
			ps.setInt(4,paymenttype.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}
	public void updatePaymenttypeBank(PaymentTypeForRestaurant paymenttype) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update payment_type_for_restaurant set account_number=?,update_date=?,qrcode=? where id = ?;");
			ps.setString(1, paymenttype.getAccountNumber());
		    LocalDateTime updateDate = LocalDateTime.now();
			ps.setObject(2,updateDate);
			ps.setString(3, paymenttype.getQrcode());
			ps.setInt(4,paymenttype.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}
	public List<PaymentTypeForRestaurant> findPayMent() {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * from payment_type;");
			ResultSet rs = ps.executeQuery();
			List<PaymentTypeForRestaurant> paymenttypes = new ArrayList<>();
			while (rs.next()) {
				PaymentTypeForRestaurant paymenttype = new PaymentTypeForRestaurant();
				paymenttype.setId(rs.getInt(1));
				paymenttype.setPaymentTypeName(rs.getString(2));

				paymenttypes.add(paymenttype);
			}


			return paymenttypes;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	public PaymentTypeForRestaurant findPayMentByName(int id,int restaurantId) {
		Connection con=getConnection();
		try {
			PreparedStatement ps=con.prepareStatement("select * from payment_type_for_restaurant where payment_type_id=? and restaurant_id=?");
			ps.setInt(1,id);
			ps.setInt(2,restaurantId);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				PaymentTypeForRestaurant payment=new PaymentTypeForRestaurant();
				payment.setId(rs.getInt(1));
				payment.setPhoneNumber(rs.getString(2));
				payment.setAccountNumber(rs.getString(3));
				Timestamp timestampcreate = rs.getTimestamp(4);
				if (timestampcreate != null) {
					payment.setCreateDate(timestampcreate.toLocalDateTime());
				}
				Timestamp timestampupdate = rs.getTimestamp(5);
				if (timestampupdate != null) {
					payment.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				payment.setRestaurantId(rs.getInt(6));
				payment.setId(rs.getInt(7));
				return payment;
			}
		}catch(SQLException e ){
			System.out.println(e.getMessage());
		}
		return null;
	}
	public int insert(PaymentTypeForRestaurant payMent) {
		int result=0;
		Connection con=getConnection();
		try {
			PreparedStatement ps=con.prepareStatement("insert into payment_type_for_restaurant (phone_number,create_date,restaurant_id,payment_type_id,qrcode)values (?,?,?,?,?)");

			ps.setString(1, payMent.getPhoneNumber());
		    LocalDateTime createDate = LocalDateTime.now();

			ps.setObject(2, createDate);
			ps.setInt(3,payMent.getRestaurantId());
			ps.setInt(4, Integer.parseInt(payMent.getSelect()));
			ps.setString(5, payMent.getQrcode());
			result=ps.executeUpdate();
		}catch(SQLException e ) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	public int insertBank(PaymentTypeForRestaurant payMent) {
		int result=0;
		Connection con=getConnection();
		try {
			PreparedStatement ps=con.prepareStatement("insert into payment_type_for_restaurant (account_number,create_date,restaurant_id,payment_type_id,qrcode)values (?,?,?,?,?)");

			ps.setString(1, payMent.getAccountNumber());
		    LocalDateTime createDate = LocalDateTime.now();

			ps.setObject(2, createDate);
			ps.setInt(3,payMent.getRestaurantId());
			ps.setInt(4, Integer.parseInt(payMent.getSelect()));
			ps.setString(5, payMent.getQrcode());
			result=ps.executeUpdate();
		}catch(SQLException e ) {
			System.out.println(e.getMessage());
		}
		return result;
	}

}
