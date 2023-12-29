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

import com.ai.jwd42.dto.OrderTypeForRestaurant;

@Repository
public class OrderTypeRepository extends BaseRepoistory{

	public List<OrderTypeForRestaurant> findOrderTypeByRestaurantId(int id) {
		Connection con = getConnection();
		try {

			PreparedStatement ps = con.prepareStatement("SELECT order_type.id,order_type.name,otr.create_date,otr.update_date FROM order_type INNER JOIN order_type_for_restaurant otr ON order_type.id = otr.order_type_id WHERE restaurant_id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<OrderTypeForRestaurant> ordertypes = new ArrayList<>();
			while (rs.next()) {
				OrderTypeForRestaurant ordertype = new OrderTypeForRestaurant();
				ordertype.setId(rs.getInt(1));
				ordertype.setOrderTypeName(rs.getString(2));
				Timestamp timestampcreate = rs.getTimestamp(3);
				if (timestampcreate != null) {
					ordertype.setCreateDate(timestampcreate.toLocalDateTime());
				}
				Timestamp timestampupdate = rs.getTimestamp(4);
				if (timestampupdate != null) {
					ordertype.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				ordertypes.add(ordertype);
			}


			return ordertypes;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public OrderTypeForRestaurant findById(int id) {
		Connection con=getConnection();
		try {
			PreparedStatement ps=con.prepareStatement("Select * from order_type where id=?");
			ps.setInt(1,id);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				OrderTypeForRestaurant ordertype=new OrderTypeForRestaurant();
				ordertype.setId(rs.getInt(1));
				ordertype.setOrderTypeName(rs.getString(2));
				Timestamp timestampcreate = rs.getTimestamp(3);
				if (timestampcreate != null) {
					ordertype.setCreateDate(timestampcreate.toLocalDateTime());
				}
				Timestamp timestampupdate = rs.getTimestamp(4);
				if (timestampupdate != null) {
					ordertype.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				return ordertype;

			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public OrderTypeForRestaurant findOrderByOrderType(String name) {
		Connection con=getConnection();
		try {
			PreparedStatement ps=con.prepareStatement("select * from order_type where name=?");
			ps.setString(1,name);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				OrderTypeForRestaurant ordertype=new OrderTypeForRestaurant();

				ordertype.setId(rs.getInt(1));
				ordertype.setOrderTypeName(rs.getString(2));
				Timestamp timestampcreate = rs.getTimestamp(3);
				if (timestampcreate != null) {
					ordertype.setCreateDate(timestampcreate.toLocalDateTime());
				}
				Timestamp timestampupdate = rs.getTimestamp(4);
				if (timestampupdate != null) {
					ordertype.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				return ordertype;
			}
		}catch(SQLException e ){
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void deleteOrdertype(int id) {
		Connection con=getConnection();
		try {
			PreparedStatement ps=con.prepareStatement("Delete from order_type_for_restaurant where order_type_id=?");
			ps.setInt(1,id);
			ps.execute();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateOrdertype(OrderTypeForRestaurant ordertype) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update order_type_for_restaurant set name=?,update_date=? where id = ?;");
			ps.setString(1, ordertype.getOrderTypeName());
		    LocalDateTime updateDate = LocalDateTime.now();
			ps.setObject(2,updateDate);
			ps.setInt(3,ordertype.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}
	public List<OrderTypeForRestaurant> findOrderType() {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * from order_type;");
			ResultSet rs = ps.executeQuery();
			List<OrderTypeForRestaurant> orderTypes = new ArrayList<>();
			while (rs.next()) {
				OrderTypeForRestaurant orderType = new OrderTypeForRestaurant();
				orderType.setId(rs.getInt(1));
				orderType.setOrderTypeName(rs.getString(2));

				orderTypes.add(orderType);
			}


			return orderTypes;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	public int insert(OrderTypeForRestaurant ordertype) {
		int result=0;
		Connection con=getConnection();
		try {
			PreparedStatement ps=con.prepareStatement("insert into order_type_for_restaurant (order_type_id,create_date,restaurant_id)values (?,?,?)");

			ps.setInt(1,Integer.parseInt(ordertype.getSelect()));
		    LocalDateTime createDate = LocalDateTime.now();

			ps.setObject(2, createDate);
			ps.setInt(3,ordertype.getRestaurantId());
			result=ps.executeUpdate();
		}catch(SQLException e ) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	public OrderTypeForRestaurant findOrderTypeById(int id,int restaurantId) {
		Connection con=getConnection();
		try {
			PreparedStatement ps=con.prepareStatement("select * from order_type_for_restaurant where order_type_id=? and restaurant_id= ?");
			ps.setInt(1,id);
			ps.setInt(2,restaurantId);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				OrderTypeForRestaurant ordertype=new OrderTypeForRestaurant();
				ordertype.setId(rs.getInt(1));
				ordertype.setSelect(rs.getString(2));
				ordertype.setRestaurantId(rs.getInt(3));
				Timestamp timestampcreate = rs.getTimestamp(4);
				if (timestampcreate != null) {
					ordertype.setCreateDate(timestampcreate.toLocalDateTime());
				}
				Timestamp timestampupdate = rs.getTimestamp(5);
				if (timestampupdate != null) {
					ordertype.setUpdateDate(timestampupdate.toLocalDateTime());
				}

				return ordertype;
			}
		}catch(SQLException e ){
			System.out.println(e.getMessage());
		}
		return null;
	}
}
