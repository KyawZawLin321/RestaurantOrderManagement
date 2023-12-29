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

import com.ai.jwd42.dto.FoodSet;

@Repository
public class FoodSetRepoistory extends BaseRepoistory {

	int lastFoodSetId = 0;//

	public int foodSetRowCount(int restaurant_id) {
		Connection con = getConnection();
		int rowCount = 0;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM food_set where restaurant_id =?");
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

	public List<FoodSet> findAllFoodSetByRestaurantId(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from food_set s inner join food_set_image i on i.food_set_id=s.id  where restaurant_id= ? and isAvaliable=1;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<FoodSet> foodSets = new ArrayList<>();
			while (rs.next()) {
				FoodSet foodSet = new FoodSet();
				foodSet.setId(rs.getInt(1));
				foodSet.setName(rs.getString(2));
				foodSet.setPrice(rs.getDouble(3));
				foodSet.setDescription(rs.getString(4));
				Timestamp timestampupdate = rs.getTimestamp(6);
				if (timestampupdate != null) {
					foodSet.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					foodSet.setCreateDate(timestampcreate.toLocalDateTime());
				}
				foodSet.setMaxQuantity(rs.getInt(9));
				foodSet.setImage(rs.getString(11));
				foodSets.add(foodSet);
			}
			return foodSets;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	public FoodSet findFoodSetById(int foodSetId) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"select * from food_set s inner join food_set_image i on i.food_set_id=s.id  where s.id= ? and isAvaliable=1;");
			ps.setInt(1, foodSetId);
			ResultSet rs = ps.executeQuery();
			FoodSet foodSet = new FoodSet();
			if (rs.next()) {

				foodSet.setId(rs.getInt(1));
				foodSet.setName(rs.getString(2));
				foodSet.setPrice(rs.getDouble(3));
				foodSet.setDescription(rs.getString(4));
				Timestamp timestampupdate = rs.getTimestamp(6);
				if (timestampupdate != null) {
					foodSet.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					foodSet.setCreateDate(timestampcreate.toLocalDateTime());
				}
				foodSet.setRestaurantId(rs.getInt(7));
				foodSet.setMaxQuantity(rs.getInt(9));
				foodSet.setImage(rs.getString(11));

			}
			return foodSet;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public int insert(FoodSet foodSet) {
		int result = 0;
		Connection con = getConnection();

		try {
			// Start a transaction
			con.setAutoCommit(false);

			// Insert into the food table
			PreparedStatement ps = con.prepareStatement(
					"INSERT INTO food_set (name, price, description, create_date,restaurant_id,  isAvaliable, max_quantity) VALUES ( ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, foodSet.getName());
			ps.setDouble(2, foodSet.getPrice());
			ps.setString(3, foodSet.getDescription());
			LocalDateTime createDate = LocalDateTime.now();
			ps.setObject(4, createDate);
			ps.setInt(5, foodSet.getRestaurantId());

			ps.setBoolean(6, foodSet.isAvaliable());
			ps.setInt(7, foodSet.getMaxQuantity());
			// Set other parameters...

			result = ps.executeUpdate();

			if (result > 0) {
				// Get the last inserted food_id
				ResultSet generatedKeys = ps.getGeneratedKeys();

				if (generatedKeys.next()) {
					lastFoodSetId = generatedKeys.getInt(1);

					// Insert into the food_image table
					PreparedStatement psImage = con
							.prepareStatement("INSERT INTO food_set_image ( path, food_set_id) VALUES ( ?, ?)");

					psImage.setString(1, foodSet.getImage());
					psImage.setInt(2, lastFoodSetId);

					result = psImage.executeUpdate();
					System.out.println("ds");
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
		return lastFoodSetId;

	}

	public int insertFood(FoodSet foodSet) {
		int result = 0;

		Connection con = getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("insert into food_for_food_set (food_id,food_set_id) values (?,?) ");
			String foodIds = foodSet.getSelect();
			String[] parts = foodIds.split(",");
			int[] ids = new int[parts.length];
			for (int i = 0; i < parts.length; i++) {
				try {
					ids[i] = Integer.parseInt(parts[i].trim());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			for (int foodId : ids) {
				ps.setInt(1, foodId);
				ps.setInt(2, lastFoodSetId);
				result = ps.executeUpdate();

			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}

	public List<FoodSet> findAll(int id) {
		Connection con = getConnection();
		List<FoodSet> foodSets = new ArrayList<>();
		try {
			PreparedStatement ps = con
					.prepareStatement("Select f.id,f.name,fimg.path,f.price,f.description,max_quantity,f.isAvaliable "
							+ "from food_set f inner join food_set_image fimg on f.id=fimg.food_set_id where restaurant_id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				FoodSet foodSet = new FoodSet();
				foodSet.setId(rs.getInt(1));
				foodSet.setName(rs.getString(2));
				foodSet.setImage(rs.getString(3));
				foodSet.setPrice(rs.getDouble(4));
				foodSet.setDescription(rs.getString(5));
				foodSet.setMaxQuantity(rs.getInt(6));
				foodSet.setAvaliable(rs.getBoolean(7));
				foodSets.add(foodSet);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return foodSets;

	}

	public FoodSet findFoodSetByName(String name) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from food_set where name=?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				FoodSet foodSet = new FoodSet();
				foodSet.setId(rs.getInt(1));
				foodSet.setName(rs.getString(2));
				foodSet.setPrice(rs.getInt(3));
				foodSet.setDescription(rs.getString(4));
				Timestamp timestampupdate = rs.getTimestamp(6);
				if (timestampupdate != null) {
					foodSet.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(5);
				if (timestampcreate != null) {
					foodSet.setCreateDate(timestampcreate.toLocalDateTime());
				}
				foodSet.setRestaurantId(rs.getInt(7));
				foodSet.setAvaliable(rs.getBoolean(8));
				foodSet.setMaxQuantity(rs.getInt(9));
				return foodSet;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void updateFoodSet(FoodSet foodSet) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update food_set set name=?,price=?,description=?,"
					+ "update_date=?,isAvaliable=?,max_quantity=? where id = ?;");
			ps.setString(1, foodSet.getName());
			ps.setDouble(2, foodSet.getPrice());
			ps.setString(3, foodSet.getDescription());
			LocalDateTime updateDate = LocalDateTime.now();

			ps.setObject(4, updateDate);
			ps.setBoolean(5, foodSet.isAvaliable());
			ps.setInt(6, foodSet.getMaxQuantity());
			ps.setInt(7, foodSet.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void updateFoodSetImg(FoodSet foodSet) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update food_set_image set path=? where food_set_id = ?;");

			ps.setString(1, foodSet.getImage());
			ps.setInt(2, foodSet.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void deleteFoodSet(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM food_set WHERE id =?;");
			ps.setInt(1, id);
			ps.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteFoodSetImg(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM food_set_image WHERE food_set_id =?");
			ps.setInt(1, id);
			ps.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteFoodForFoodSet(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM food_for_food_set WHERE food_set_id=?;");
			ps.setInt(1, id);
			ps.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public List<FoodSet> searchFoodSet(String keyword) {
		Connection con = getConnection();
		List<FoodSet> foodSets = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM food_set WHERE LOWER(name) LIKE LOWER(?) and isAvaliable=1;");

			keyword = "%" + keyword.toLowerCase() + "%";

			ps.setString(1, keyword);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				FoodSet foodSet = new FoodSet();
				foodSet.setId(rs.getInt("id"));
				foodSet.setName(rs.getString("name"));

				foodSets.add(foodSet);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return foodSets;
	}

	public List<FoodSet> findFoodForFoodSetByFoodSetId(int id) {
		Connection con = getConnection();
		List<FoodSet> foodSets = new ArrayList<>();
		try {
			PreparedStatement ps = con.prepareStatement(
					"Select fs.id,f.name,fi.path,f.price,f.description,f.max_quantity,f.id,f.isAvaliable from food f inner join food_for_food_set  fs on f.id=fs.food_id inner join food_image fi on f.id=fi.food_id where food_set_id=? ;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				FoodSet foodSet = new FoodSet();
				foodSet.setId(rs.getInt(1));
				foodSet.setName(rs.getString(2));
				foodSet.setImage(rs.getString(3));
				foodSet.setPrice(rs.getDouble(4));
				foodSet.setDescription(rs.getString(5));
				foodSet.setMaxQuantity(rs.getInt(6));
				foodSet.setFoodId(rs.getInt(7));
				foodSet.setAvaliable(rs.getBoolean(8));

				foodSets.add(foodSet);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return foodSets;

	}

	public List<FoodSet> findFoodForFoodSet(int foodid, int foodSetId) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("select * from food_for_food_set where food_id=? and food_set_id=?");
			ps.setInt(1, foodid);
			ps.setInt(2, foodSetId);
			ResultSet rs = ps.executeQuery();
			List<FoodSet> foodSets = new ArrayList<>();
			while (rs.next()) {
				FoodSet foodSet = new FoodSet();
				foodSet.setFoodforfoodsetId(rs.getInt(1));
				foodSet.setFoodId(rs.getInt(2));
				foodSet.setId(rs.getInt(3));
				foodSets.add(foodSet);
			}
			return foodSets;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public int insertFoodForFoodSet(FoodSet foodSet) {
		int result = 0;
		Connection con = getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("insert into food_for_food_set (food_id,food_set_id) values (?,?) ");

			ps.setInt(1, Integer.parseInt(foodSet.getSelect()));
			ps.setInt(2, foodSet.getId());
			result = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}

	public void delete(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM food_for_food_set WHERE id=?;");
			ps.setInt(1, id);
			ps.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateFoodForFoodSet(FoodSet foodSet) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update food_for_food_set set food_id=? where id = ?;");
			ps.setInt(1, Integer.parseInt(foodSet.getSelect()));
			ps.setInt(2, foodSet.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}
