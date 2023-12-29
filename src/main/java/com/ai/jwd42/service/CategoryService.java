package com.ai.jwd42.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.jwd42.dto.Category;
import com.ai.jwd42.repo.CategoryRepo;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepo categoryRepo;

	public List<Category> findAll(int id) {
		List<Category> categorys = categoryRepo.findALl(id);
		return categorys;

	}

	public boolean checkCategoryAlreadyExist(String name, int id) {
		Category category = categoryRepo.findCategoryByName(name, id);
		if (category == null) {
			return false;
		} else {
			return true;
		}
	}

	public int insert(Category category) {
		return categoryRepo.insert(category);
	}

	public void delete(int id) {

		categoryRepo.deleteCategory(id);
	}

	public void updateCategory(Category category) {
		categoryRepo.updateCategory(category);
	}

	public List<Category> searchCategory(String keyword) {
		return categoryRepo.searchByName(keyword);
	}

}
