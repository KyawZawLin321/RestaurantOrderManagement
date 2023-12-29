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

import com.ai.jwd42.dto.User;

@Repository
public class UserRepository extends BaseRepoistory {

	public List<User> findCustomer(int id) {
		Connection con = getConnection();
		List<User> users = new ArrayList<>();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select  distinct user.name,user.email,user.phone_number,address from order_table inner join user on order_table.user_id=user.id where order_table.restaurant_id=?; ");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setName(rs.getString(1));
				user.setEmail(rs.getString(2));
				user.setPhoneNumber(rs.getString(3));
				user.setAddress(rs.getString(4));

				users.add(user);

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return users;
	}

	public User findUserByEmail(String email) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from user  where email=?;");

			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt(1));
				user.setName(rs.getString(2));
				user.setPassword(rs.getString(8));
				user.setEmail(rs.getString(3));
				user.setPhoneNumber(rs.getString(4));
				user.setAddress(rs.getString(5));
				Timestamp timestampupdate = rs.getTimestamp(7);
				if (timestampupdate != null) {
					user.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(6);
				if (timestampcreate != null) {
					user.setCreateDate(timestampcreate.toLocalDateTime());
				}
				user.setRoleId(rs.getInt(9));
				user.setRestaurantId(rs.getInt(10));
				return user;

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public User findUserByPhoneNumber(String phone_number) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from user  where phone_number=?;");

			ps.setString(1, phone_number);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt(1));
				user.setName(rs.getString(2));
				user.setEmail(rs.getString(3));
				user.setPhoneNumber(rs.getString(4));
				user.setAddress(rs.getString(5));
				Timestamp timestampupdate = rs.getTimestamp(7);
				if (timestampupdate != null) {
					user.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(6);
				if (timestampcreate != null) {
					user.setCreateDate(timestampcreate.toLocalDateTime());
				}
				user.setPassword(rs.getString(8));
				user.setRoleId(rs.getInt(9));
				user.setRestaurantId(rs.getInt(10));
				return user;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public List<User> findAllUser() {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from user");
			ResultSet rs = ps.executeQuery();
			List<User> users = new ArrayList<>();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt(1));
				user.setName(rs.getString(2));
				user.setEmail(rs.getString(3));
				user.setPhoneNumber(rs.getString(4));
				user.setAddress(rs.getString(5));
				Timestamp timestampupdate = rs.getTimestamp(7);
				if (timestampupdate != null) {
					user.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(6);
				if (timestampcreate != null) {
					user.setCreateDate(timestampcreate.toLocalDateTime());
				}
				user.setPassword(rs.getString(8));
				user.setRoleId(rs.getInt(9));
				user.setRestaurantId(rs.getInt(10));

				users.add(user);
			}
			return users;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	public List<User> findAllUserRole() {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from user where role_id=1");
			ResultSet rs = ps.executeQuery();
			List<User> users = new ArrayList<>();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt(1));
				user.setName(rs.getString(2));
				user.setEmail(rs.getString(3));
				user.setPhoneNumber(rs.getString(4));
				user.setAddress(rs.getString(5));
				Timestamp timestampupdate = rs.getTimestamp(7);
				if (timestampupdate != null) {
					user.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(6);
				if (timestampcreate != null) {
					user.setCreateDate(timestampcreate.toLocalDateTime());
				}
				user.setPassword(rs.getString(8));
				user.setRoleId(rs.getInt(9));
				user.setRestaurantId(rs.getInt(10));

				users.add(user);
			}
			return users;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public User findUserById(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from user where id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt(1));
				user.setName(rs.getString(2));
				user.setEmail(rs.getString(3));
				user.setPhoneNumber(rs.getString(4));
				user.setAddress(rs.getString(5));
				Timestamp timestampupdate = rs.getTimestamp(7);
				if (timestampupdate != null) {
					user.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(6);
				if (timestampcreate != null) {
					user.setCreateDate(timestampcreate.toLocalDateTime());
				}
				user.setPassword(rs.getString(8));
				user.setRoleId(rs.getInt(9));
				user.setRestaurantId(rs.getInt(10));
				return user;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public int userRowCount(int restaurant_id) {
		Connection con = getConnection();
		int rowCount = 0;
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT COUNT(DISTINCT user.id) as distinct_user_count FROM order_table INNER JOIN user ON order_table.user_id = user.id WHERE order_table.restaurant_id = ?;");
			ps.setInt(1, restaurant_id);
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

	public int countrow() {
		Connection con = getConnection();
		int rowCount = 0;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS total_rows FROM user where role_id=1;");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rowCount = rs.getInt(1);
				System.out.println("Number of rows in the role table: " + rowCount);

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return rowCount;
	}

	public int ownercountrow() {
		Connection con = getConnection();
		int rowCount = 0;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS total_rows FROM user where role_id=2;");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rowCount = rs.getInt(1);
				System.out.println("Number of rows in the role table: " + rowCount);

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return rowCount;
	}

	public int restaurantcountrow() {
		Connection con = getConnection();
		int rowCount = 0;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS total_rows FROM restaurant ");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rowCount = rs.getInt(1);
				System.out.println("Number of rows in the role table: " + rowCount);

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return rowCount;
	}

	public int insertUser(User user) {
		Connection con = getConnection();
		int result = 0;
		try {
			PreparedStatement ps = con.prepareStatement(
					"insert into user(name,email,password,phone_number,address,create_date) value(?,?,?,?,?,?)");
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getPhoneNumber());
			ps.setString(5, user.getAddress());
			LocalDateTime createDate = LocalDateTime.now();
			ps.setObject(6, createDate);
			result = ps.executeUpdate();

			return result;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;

	}


	public void updateUser(User user) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"update user set phone_number = ? , name = ? , email = ?,address=?,update_date=? where id = ?;");
			ps.setString(1, user.getPhoneNumber());
			ps.setString(2, user.getName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getAddress());
			LocalDateTime updateDate = LocalDateTime.now();
			ps.setObject(5, updateDate);
			ps.setInt(6, user.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void deleteUser(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("delete from user where id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void changePassword(String email, String password) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update user set password = ? where email = ?;");
			ps.setString(1, password);
			ps.setString(2, email);

			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}
