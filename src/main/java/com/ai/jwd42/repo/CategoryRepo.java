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

import com.ai.jwd42.dto.Category;

@Repository
public class CategoryRepo extends BaseRepoistory {
	public List<Category> findALl(int id) {
		Connection con = getConnection();
		List<Category> categorys = new ArrayList<>();
		try {
			PreparedStatement ps = con.prepareStatement("Select * from food_category where restaurant_id= ? ;");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Category category = new Category();
				category.setId(rs.getInt(1));
				category.setFoodName(rs.getString(2));
				Timestamp timestampupdate = rs.getTimestamp(4);
				if (timestampupdate != null) {
					category.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(3);
				if (timestampcreate != null) {
					category.setCreateDate(timestampcreate.toLocalDateTime());
				}
				categorys.add(category);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return categorys;

	}

	public int insert(Category category) {
		int result = 0;
		Connection con = getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("insert into food_category (name,create_date,restaurant_id)values (?,?,?) ");
			LocalDateTime createDate = LocalDateTime.now();

			ps.setString(1, category.getFoodName());
			ps.setObject(2, createDate);
			ps.setInt(3, category.getRestaurant_id());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	public Category findCategoryByName(String name, int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("select * from food_category where name=? and restaurant_id=?");
			ps.setString(1, name);
			ps.setInt(2, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Category category = new Category();
				category.setId(rs.getInt(1));
				category.setFoodName(rs.getString(2));
				Timestamp timestampupdate = rs.getTimestamp(4);
				if (timestampupdate != null) {
					category.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(3);
				if (timestampcreate != null) {
					category.setCreateDate(timestampcreate.toLocalDateTime());
				}
				return category;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public Category findById(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("Select * from food_category where id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Category category = new Category();
				category.setId(rs.getInt(1));
				category.setFoodName(rs.getString(2));
				Timestamp timestampupdate = rs.getTimestamp(4);
				if (timestampupdate != null) {
					category.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp(3);
				if (timestampcreate != null) {
					category.setCreateDate(timestampcreate.toLocalDateTime());
				}
				return category;

			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void deleteCategory(int id) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("Delete from food_category where id=?");
			ps.setInt(1, id);
			ps.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateCategory(Category category) {
		Connection con = getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("update food_category set name=?,update_date=? where id = ?;");
			ps.setString(1, category.getFoodName());
			LocalDateTime updateDate = LocalDateTime.now();
			ps.setObject(2, updateDate);
			ps.setInt(3, category.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public List<Category> searchByName(String keyword) {
		Connection con = getConnection();
		List<Category> categories = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM food_category WHERE name LIKE ?");
			keyword = "%" + keyword.trim() + "%";
			ps.setString(1, keyword);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Category category = new Category();
				category.setId(rs.getInt("id"));
				category.setFoodName(rs.getString("name"));
				Timestamp timestampupdate = rs.getTimestamp("update_date");
				if (timestampupdate != null) {
					category.setUpdateDate(timestampupdate.toLocalDateTime());
				}
				Timestamp timestampcreate = rs.getTimestamp("create_date");
				if (timestampcreate != null) {
					category.setCreateDate(timestampcreate.toLocalDateTime());
				}
				categories.add(category);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return categories;
	}
}
