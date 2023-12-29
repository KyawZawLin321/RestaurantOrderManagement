package com.ai.jwd42.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ai.jwd42.dto.Restaurant;

@Repository
public class RestaurantRepository extends BaseRepoistory {

	public List<Restaurant> findAllRestaurant() {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from restaurant r inner join restaurant_image i on r.id=i.restaurant_id ;");
			ResultSet rs = ps.executeQuery();
			List<Restaurant> restaurants = new ArrayList<>();
			while (rs.next()) {
				Restaurant restaurant = new Restaurant();
				restaurant.setId(rs.getInt(1));
				restaurant.setName(rs.getString(2));
				restaurant.setAddress(rs.getString(3));
				Timestamp timestampupdate = rs.getTimestamp(4);
				if (timestampupdate != null) {
					restaurant.setUpdate_date(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					restaurant.setCreate_date(timestampcreate.toLocalDateTime());
				}
				restaurant.setDescription(rs.getString(6));
				restaurant.setPhone_number(rs.getString(7));
				restaurant.setEmail(rs.getString(8));
				restaurant.setLogo(rs.getString(9));
				restaurant.setImage(rs.getString(11));

				restaurants.add(restaurant);
			}
			return restaurants;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public Restaurant findRestaurantById(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from restaurant r inner join restaurant_image i on r.id=i.restaurant_id  where r.id= ? ; ");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			Restaurant restaurant = new Restaurant();
			if (rs.next()) {

				restaurant.setId(rs.getInt(1));
				restaurant.setName(rs.getString(2));
				restaurant.setAddress(rs.getString(3));
				Timestamp timestampupdate = rs.getTimestamp(4);
				if (timestampupdate != null) {
					restaurant.setUpdate_date(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					restaurant.setCreate_date(timestampcreate.toLocalDateTime());
				}
				restaurant.setDescription(rs.getString(6));
				restaurant.setPhone_number(rs.getString(7));
				restaurant.setEmail(rs.getString(8));
				restaurant.setLogo(rs.getString(9));
				restaurant.setImage(rs.getString(11));

			}
			return restaurant;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public List<Restaurant> findRestaurantByLimit() {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from restaurant r inner join restaurant_image i on r.id=i.restaurant_id LIMIT 4;");
			ResultSet rs = ps.executeQuery();
			List<Restaurant> restaurants = new ArrayList<>();
			while (rs.next()) {
				Restaurant restaurant = new Restaurant();
				restaurant.setId(rs.getInt(1));
				restaurant.setName(rs.getString(2));
				restaurant.setAddress(rs.getString(3));
				Timestamp timestampupdate = rs.getTimestamp(4);
				if (timestampupdate != null) {
					restaurant.setUpdate_date(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					restaurant.setCreate_date(timestampcreate.toLocalDateTime());
				}
				restaurant.setDescription(rs.getString(6));
				restaurant.setPhone_number(rs.getString(7));
				restaurant.setEmail(rs.getString(8));
				restaurant.setLogo(rs.getString(9));
				restaurant.setImage(rs.getString(11));

				restaurants.add(restaurant);
			}
			return restaurants;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public Restaurant findOwnerRestaurant(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from user where  restaurant_id=? and role_id=? ;");
			ps.setInt(1, id);
			ps.setInt(2, 2);
			ResultSet rs = ps.executeQuery();

			Restaurant restaurant = new Restaurant();
			if (rs.next()) {

				restaurant.setId(rs.getInt(1));
				restaurant.setOwnername(rs.getString(2));
				restaurant.setOwneremail(rs.getString(3));
				restaurant.setOwnerPhone(rs.getString(4));

				restaurant.setOwneraddress(rs.getString(5));
				Timestamp timestampupdate = rs.getTimestamp(7);
				if (timestampupdate != null) {
					restaurant.setUpdate_date(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(6);
				if (timestampcreate != null) {
					restaurant.setCreate_date(timestampcreate.toLocalDateTime());
				}

			}
			return restaurant;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public Restaurant findRestaurantByEmail(String email) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("Select * from restaurant where email=? ");
			ps.setString(1, email);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Restaurant restaurant = new Restaurant();
				restaurant.setId(rs.getInt(1));
				restaurant.setName(rs.getString(2));
				restaurant.setAddress(rs.getString(3));
				Timestamp timestampupdate = rs.getTimestamp(4);
				if (timestampupdate != null) {
					restaurant.setUpdate_date(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					restaurant.setCreate_date(timestampcreate.toLocalDateTime());
				}

				restaurant.setDescription(rs.getString(6));
				restaurant.setEmail(rs.getString(7));
				restaurant.setPhone_number(rs.getString(8));

				return restaurant;

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public int addRestaurant(Restaurant restaurant) {
		int result = 0;
		Connection con = getConnection();

		try {
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(
					"INSERT INTO restaurant (name, address, create_date,description,email,phone_number,logo_path) VALUES (?, ?, ?, ?, ?, ?,?)",
					Statement.RETURN_GENERATED_KEYS);
			System.out.println("Hello");
			ps.setString(1, restaurant.getName());
			ps.setString(2, restaurant.getAddress());
			LocalDateTime createDate = LocalDateTime.now();
			ps.setObject(3, createDate);
			ps.setString(4, restaurant.getDescription());
			ps.setString(5, restaurant.getEmail());
			ps.setString(6, restaurant.getPhone_number());
			ps.setString(7, restaurant.getLogo());
			result = ps.executeUpdate();

			if (result > 0) {
				// Get the last inserted food_id
				ResultSet generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					int restaurantId = generatedKeys.getInt(1);

					// Insert into the food_image table

					PreparedStatement psImage = con
							.prepareStatement("INSERT INTO restaurant_image ( path, restaurant_id) VALUES ( ?, ?)");

					psImage.setString(1, restaurant.getImage());
					System.out.println("restaurant : " + restaurant.getImage());
					psImage.setInt(2, restaurantId);
					result = psImage.executeUpdate();

					PreparedStatement psuser = con.prepareStatement(
							"INSERT INTO user (name, email, phone_number,address, create_date,password,role_id,restaurant_id) VALUES (?, ?, ?, ?, ?,?,2,?)");
					psuser.setString(1, restaurant.getOwnername());
					psuser.setString(2, restaurant.getOwneremail());
					psuser.setString(3, restaurant.getOwnerPhone());
					psuser.setString(4, restaurant.getOwneraddress());
					psuser.setObject(5, createDate);
					psuser.setObject(6, restaurant.getPassword());
					psuser.setInt(7, restaurantId);
					/* psuser.setInt(7, restaurant.getRole_id()); */

					result = psuser.executeUpdate();

					// Commit the transaction
					con.commit();
				}
			}

		} catch (SQLException e) {
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException rollbackException) {
				System.out.println(rollbackException.getMessage());
			}
			System.out.println(e.getMessage());
		} finally {
			try {
				if (con != null) {
					con.setAutoCommit(true);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}

	public void updateOwner(Restaurant restaurant) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"update user set phone_number = ? , name = ? , email = ?,address=?,update_date=? where id = ?;");
			ps.setString(1, restaurant.getOwnerPhone());
			ps.setString(2, restaurant.getOwnername());
			ps.setString(3, restaurant.getOwneremail());
			ps.setString(4, restaurant.getOwneraddress());
			LocalDateTime updateDate = LocalDateTime.now();
			ps.setObject(5, updateDate);
			ps.setInt(6, restaurant.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void updateRestaurant(Restaurant restaurant) {
		Connection con = getConnection();
		System.out.println("Hello update data success.");
		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE restaurant SET name = ?, address = ?, update_date = ?, description = ?, email = ?, phone_number = ? WHERE id = ?;");
			ps.setString(1, restaurant.getName());
			ps.setString(2, restaurant.getAddress());
			LocalDateTime updateDate = LocalDateTime.now();
			ps.setObject(3, updateDate);
			ps.setString(4, restaurant.getDescription());
			ps.setString(5, restaurant.getEmail());
			ps.setString(6, restaurant.getPhone_number());

			ps.setInt(7, restaurant.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateRestaurantDescription(Restaurant restaurant) {
		Connection con = getConnection();
		System.out.println("Hello update data success.");
		try {
			PreparedStatement ps = con
					.prepareStatement("UPDATE restaurant SET  update_date = ?, description = ? WHERE id = ?;");

			LocalDateTime updateDate = LocalDateTime.now();
			ps.setObject(1, updateDate);
			ps.setString(2, restaurant.getDescription());

			ps.setInt(3, restaurant.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateRestaurantImg(Restaurant restaurant) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update restaurant_image set path=? where restaurant_id = ?;");
			ps.setString(1, restaurant.getImage());
			ps.setInt(2, restaurant.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteRestaurant(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("delete from restaurant where id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void deleteOwner(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("delete from user where restaurant_id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void deleteRestaurantImg(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM restaurant_image WHERE restaurant_id =?");
			ps.setInt(1, id);
			ps.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public Restaurant findRestaurantProfileByEmail(String email) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select r.id,r.name,r.email,r.phone_number,r.address,r.create_date,r.update_date,r.description,r.logo_path,ri.path from restaurant r inner join user u on u.restaurant_id=r.id inner join restaurant_image ri on ri.restaurant_id=r.id where u.email=?;");
			ps.setString(1, email);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Restaurant restaurant = new Restaurant();
				restaurant.setId(rs.getInt(1));
				restaurant.setName(rs.getString(2));
				restaurant.setEmail(rs.getString(3));
				restaurant.setPhone_number(rs.getString(4));
				restaurant.setAddress(rs.getString(5));
				Timestamp timestampupdate = rs.getTimestamp(7);
				if (timestampupdate != null) {
					restaurant.setUpdate_date(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(6);
				if (timestampcreate != null) {
					restaurant.setCreate_date(timestampcreate.toLocalDateTime());
				}
				restaurant.setDescription(rs.getString(8));
				restaurant.setLogo(rs.getString(9));
				restaurant.setImage(rs.getString(10));
				System.out.println("restaurant : " + restaurant.getName());
				return restaurant;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void updateRestaurantLogo(Restaurant restaurant) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update restaurant set logo_path= ? where id = ?;");
			ps.setString(1, restaurant.getLogo());
			ps.setInt(2, restaurant.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public List<Restaurant> searchRestaurant(String keyword) {
		Connection con = getConnection();
		List<Restaurant> restaurants = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM restaurant WHERE LOWER(name) LIKE LOWER(?); ");

			keyword = "%" + keyword.toLowerCase() + "%";

			ps.setString(1, keyword);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Restaurant restaurant = new Restaurant();
				restaurant.setId(rs.getInt(1));
				restaurant.setName(rs.getString(2));
				restaurant.setAddress(rs.getString(3));
				restaurant.setDescription(rs.getString(7));
				restaurant.setPhone_number(rs.getString(8));
				restaurant.setEmail(rs.getString(9));
				restaurant.setLogo(rs.getString(9));
				restaurants.add(restaurant);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return restaurants;
	}

}
