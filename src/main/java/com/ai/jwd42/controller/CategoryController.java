package com.ai.jwd42.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ai.jwd42.dto.Category;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.CategoryService;
import com.ai.jwd42.service.FoodService;

@Controller
@RequestMapping("/owner")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FoodService foodService;

	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public ModelAndView category(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		User user = (User) request.getSession().getAttribute("user");
		model.addAttribute("categorys", categoryService.findAll(user.getRestaurantId()));
		return new ModelAndView("category.html", "category", new Category());
	}

	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	public ModelAndView addCategory(@ModelAttribute Category category, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		User user = (User) request.getSession().getAttribute("user");

		if (category.getFoodName() == "") {
			redirectAttributes.addFlashAttribute("categoryId", category.getId());
			redirectAttributes.addFlashAttribute("addError", "Fill Category Name !");
			return new ModelAndView("redirect:/owner/category");
		} else if (category.getFoodName().length() > 30) {
			redirectAttributes.addFlashAttribute("categoryId", category.getId());
			redirectAttributes.addFlashAttribute("addError", "Category Name too Long!");
			return new ModelAndView("redirect:/owner/category");
		} else if (categoryService.checkCategoryAlreadyExist(category.getFoodName(), user.getRestaurantId())) {
			redirectAttributes.addFlashAttribute("categoryId", category.getId());
			redirectAttributes.addFlashAttribute("addError", "This Category name is already exist");
			return new ModelAndView("redirect:/owner/category");
		} else {
			category.setRestaurant_id(user.getRestaurantId());
			categoryService.insert(category);
			redirectAttributes.addFlashAttribute("categoryId", category.getId());
			redirectAttributes.addFlashAttribute("addMessage", "Success Add New Category");
			return new ModelAndView("redirect:/owner/category");
		}

	}

	@RequestMapping(value = "/deleteCategory", method = RequestMethod.POST)
	public ModelAndView deleteCategory(@ModelAttribute("SpringWeb") Category category, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		System.out.println("fof"+foodService.findFoodByCategoryId(category.getId()));
		if(!foodService.findFoodByCategoryId(category.getId()).isEmpty()) {
			redirectAttributes.addFlashAttribute("categoryId", category.getId());
			redirectAttributes.addFlashAttribute("errorDelete", "This Category is using for food.You Can't Delete!");
			return new ModelAndView("redirect:/owner/category");
		}else {

		categoryService.delete(category.getId());
		redirectAttributes.addFlashAttribute("msg", "Success Delete Category !");
		return new ModelAndView("redirect:/owner/category");
		}

	}

	@RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
	public ModelAndView updateCategory(@ModelAttribute Category category, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		User user = (User) request.getSession().getAttribute("user");
		if (categoryService.checkCategoryAlreadyExist(category.getFoodName(), user.getRestaurantId())) {
			redirectAttributes.addFlashAttribute("categoryId", category.getId());
			redirectAttributes.addFlashAttribute("error", "This Category name is already exist can't update !");
			return new ModelAndView("redirect:/owner/category");
		} else if (category.getFoodName().length() > 30) {
			redirectAttributes.addFlashAttribute("categoryId", category.getId());
			redirectAttributes.addFlashAttribute("error", "Category Name too Long!");
			return new ModelAndView("redirect:/owner/category");
		} else {
			category.setRestaurant_id(user.getRestaurantId());
			categoryService.updateCategory(category);
			redirectAttributes.addFlashAttribute("categoryId", category.getId());
			redirectAttributes.addFlashAttribute("message", "Success Update Category !");
			return new ModelAndView("redirect:/owner/category");
		}

	}

}
