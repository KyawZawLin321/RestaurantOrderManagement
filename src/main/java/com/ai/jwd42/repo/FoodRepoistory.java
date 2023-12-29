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

import com.ai.jwd42.dto.Food;
import com.ai.jwd42.dto.FoodForFoodSet;

@Repository
public class FoodRepoistory extends BaseRepoistory {

	public int foodRowCount(int restaurant_id) {
		Connection con = getConnection();
		int rowCount = 0;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM food where restaurant_id=? ");
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

	public List<Food> findAllFoodByRestaurantId(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from food f inner join food_image i on i.food_id=f.id where restaurant_id= ? and isAvaliable=1;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<Food> foods = new ArrayList<>();
			while (rs.next()) {
				Food food = new Food();
				food.setId(rs.getInt(1));
				food.setName(rs.getString(2));
				food.setPrice(rs.getDouble(3));
				food.setDescription(rs.getString(4));
				Timestamp timestampupdate = rs.getTimestamp(6);
				if (timestampupdate != null) {
					food.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					food.setCreateDate(timestampcreate.toLocalDateTime());
				}
				food.setRestaurantId(rs.getInt(7));
				food.setCategoryId(rs.getInt(8));
				food.setMaxQuantity(rs.getInt(10));
				food.setImage(rs.getString(12));
				foods.add(food);
			}
			return foods;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public Food findFoodById(int foodId) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from food f inner join food_image i on i.food_id=f.id inner join food_category c on c.id=f.food_category_id where food_id= ? and isAvaliable=1 ");
			ps.setInt(1, foodId);
			ResultSet rs = ps.executeQuery();

			Food food = new Food();
			if (rs.next()) {

				food.setId(rs.getInt(1));
				food.setName(rs.getString(2));
				food.setPrice(rs.getDouble(3));
				food.setDescription(rs.getString(4));
				Timestamp timestampupdate = rs.getTimestamp(6);
				if (timestampupdate != null) {
					food.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					food.setCreateDate(timestampcreate.toLocalDateTime());
				}
				food.setRestaurantId(rs.getInt(7));
				food.setCategoryId(rs.getInt(8));
				food.setAvaliable(rs.getBoolean(9));
				food.setMaxQuantity(rs.getInt(10));
				food.setImage(rs.getString(12));
				food.setCategory(rs.getString(15));

			}
			return food;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public int insert(Food food) {
		int result = 0;
		Connection con = getConnection();
		try {
			// Start a transaction
			con.setAutoCommit(false);

			// Insert into the food table
			PreparedStatement psFood = con.prepareStatement(
					"INSERT INTO food (name, price, description, create_date,restaurant_id, food_category_id, isAvaliable, max_quantity) VALUES ( ?,?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			psFood.setString(1, food.getName());
			psFood.setDouble(2, food.getPrice());
			psFood.setString(3, food.getDescription());
			LocalDateTime createDate = LocalDateTime.now();
			psFood.setObject(4, createDate);
			psFood.setInt(5, food.getRestaurantId());

			psFood.setInt(6, Integer.parseInt(food.getSelect()));
			psFood.setBoolean(7, food.isAvaliable());
			psFood.setInt(8, food.getMaxQuantity());
			// Set other parameters...

			result = psFood.executeUpdate();
			System.out.println("d");
			if (result > 0) {
				// Get the last inserted food_id
				ResultSet generatedKeys = psFood.getGeneratedKeys();
				if (generatedKeys.next()) {
					int lastFoodId = generatedKeys.getInt(1);

					// Insert into the food_image table
					PreparedStatement psImage = con
							.prepareStatement("INSERT INTO food_image ( path, food_id) VALUES ( ?, ?)");

					psImage.setString(1, food.getImage());
					psImage.setInt(2, lastFoodId);

					result = psImage.executeUpdate();

					// Commit the transaction
					con.commit();
				}
			}
		} catch (SQLException e) {
			try {
				// Rollback the transaction in case of an exception
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException rollbackException) {
				System.out.println(rollbackException.getMessage());
			}
			System.out.println(e.getMessage());
		} finally {
			try {
				// Restore auto-commit mode
				if (con != null) {
					con.setAutoCommit(true);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}

	public List<Food> findAll(int id) {
		Connection con = getConnection();
		List<Food> foods = new ArrayList<>();
		try {
			PreparedStatement ps = con.prepareStatement(
					"Select f.id,f.name,fimg.path,f.price,f.description,max_quantity,f.isAvaliable,f.food_category_id,c.name from food f inner join food_image fimg on f.id=fimg.food_id inner join food_category c on c.id=f.food_category_id where f.restaurant_id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Food food = new Food();
				food.setId(rs.getInt(1));
				food.setName(rs.getString(2));
				food.setImage(rs.getString(3));
				food.setPrice(rs.getDouble(4));
				food.setDescription(rs.getString(5));
				food.setMaxQuantity(rs.getInt(6));
				food.setAvaliable(rs.getBoolean(7));
				food.setCategoryId(rs.getInt(8));
				food.setSelect(rs.getString(9));
				foods.add(food);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return foods;

	}

	public void updateFood(Food food) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update food set name=?,price=?,description=?,"
					+ "update_date=?,food_category_id=?,isAvaliable=?,max_quantity=? where id = ?;");
			ps.setString(1, food.getName());
			ps.setDouble(2, food.getPrice());
			ps.setString(3, food.getDescription());
			LocalDateTime updateDate = LocalDateTime.now();

			ps.setObject(4, updateDate);
			ps.setInt(5, Integer.parseInt(food.getSelect()));
			ps.setBoolean(6, food.isAvaliable());
			ps.setInt(7, food.getMaxQuantity());
			ps.setInt(8, food.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public Food findFoodByName(String name) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from food where name=?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Food food = new Food();
				food.setId(rs.getInt(1));
				food.setName(rs.getString(2));
				food.setPrice(rs.getInt(3));
				food.setDescription(rs.getString(4));
				Timestamp timestampupdate = rs.getTimestamp(6);
				if (timestampupdate != null) {
					food.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					food.setCreateDate(timestampcreate.toLocalDateTime());
				}
				food.setRestaurantId(rs.getInt(7));
				food.setSelect(rs.getString(8));
				food.setAvaliable(rs.getBoolean(9));
				food.setMaxQuantity(rs.getInt(10));
				return food;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void updateFoodImg(Food food) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update food_image set path=? where food_id = ?;");

			ps.setString(1, food.getImage());
			ps.setInt(2, food.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void deleteFood(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM food WHERE id =?;");
			ps.setInt(1, id);
			ps.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteFoodImg(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM food_image WHERE food_id =?");
			ps.setInt(1, id);
			ps.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public List<Food> searchFood(String keyword) {
		Connection con = getConnection();
		List<Food> foods = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM food WHERE LOWER(name) LIKE LOWER(?) and isAvaliable=1 ;");

			keyword = "%" + keyword.toLowerCase() + "%";

			ps.setString(1, keyword);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Food food = new Food();
				food.setId(rs.getInt("id"));
				food.setName(rs.getString("name"));

				foods.add(food);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return foods;
	}

	public List<FoodForFoodSet> findFoodForFoodSet(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from food_for_food_set where id=?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<FoodForFoodSet> foodForFoodSets = new ArrayList<>();
			while (rs.next()) {
				FoodForFoodSet foodForFoodSet = new FoodForFoodSet();
				foodForFoodSet.setId(rs.getInt(1));
				foodForFoodSet.setFoodId(rs.getInt(2));
				foodForFoodSet.setFoodSetId(id);
				foodForFoodSets.add(foodForFoodSet);
			}
			return foodForFoodSets;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	public List<FoodForFoodSet> findFoodForFoodSetByFoodId(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from food_for_food_set where food_id=?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<FoodForFoodSet> foodForFoodSets = new ArrayList<>();
			while (rs.next()) {
				FoodForFoodSet foodForFoodSet = new FoodForFoodSet();
				foodForFoodSet.setId(rs.getInt(1));
				foodForFoodSet.setFoodId(rs.getInt(2));
				foodForFoodSet.setFoodSetId(id);
				foodForFoodSets.add(foodForFoodSet);
			}
			return foodForFoodSets;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public List<Food> findFoodByCategoryId(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from food where food_category_id=?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<Food> foods = new ArrayList<>();

			while (rs.next()) {
				Food food = new Food();
				food.setId(rs.getInt(1));
				food.setName(rs.getString(2));
				food.setPrice(rs.getInt(3));
				food.setDescription(rs.getString(4));
				Timestamp timestampupdate = rs.getTimestamp(6);
				if (timestampupdate != null) {
					food.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					food.setCreateDate(timestampcreate.toLocalDateTime());
				}
				food.setRestaurantId(rs.getInt(7));
				food.setSelect(rs.getString(8));
				food.setAvaliable(rs.getBoolean(9));
				food.setMaxQuantity(rs.getInt(10));
				foods.add(food);
			}

			return foods;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
