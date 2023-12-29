package com.ai.jwd42.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ai.jwd42.dto.Food;
import com.ai.jwd42.dto.FoodSet;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.CategoryService;
import com.ai.jwd42.service.FoodService;
import com.ai.jwd42.service.FoodSetService;
import com.ai.jwd42.service.OrderTypeService;
import com.ai.jwd42.service.PaymentTypeService;

@MultipartConfig
@Controller
@RequestMapping("/owner")
public class FoodSetController {

	@Autowired
	private FoodService foodService;
	@Autowired
	private FoodSetService foodSetService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private OrderTypeService orderTypeService;
	@Autowired
	private PaymentTypeService paymentTypeService;

	@RequestMapping(value = "/addFoodSet", method = RequestMethod.POST)
	public ModelAndView addFood(@ModelAttribute FoodSet foodSet, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException, ServletException {

		User user = (User) request.getSession().getAttribute("user");

		foodSet.setRestaurantId(user.getRestaurantId());
		Part file = request.getPart("image");
		if (foodSet.getSelect() == null || foodSet.getSelect().equals("")
				|| foodSetService.checkFoodSetIsEmpty(foodSet)) {
			redirectAttributes.addFlashAttribute("errorFoodSet", "Fill FoodSet Data !");
			return new ModelAndView("redirect:/owner/addProduct");
		} else if (foodSetService.checkFoodSetInput(foodSet)) {
			redirectAttributes.addFlashAttribute("errorFoodSet", "FoodSet Data is Too Long !");
			return new ModelAndView("redirect:/owner/addProduct");
		} else if (countSelectedOptions(foodSet.getSelect()) < 2) {
			redirectAttributes.addFlashAttribute("errorFoodSet", "FoodSet need two food at least!");
			return new ModelAndView("redirect:/owner/addProduct");
		}
		if (file != null && file.getSubmittedFileName() != "") {
			String imgFileName = file.getSubmittedFileName();
			String fileExtension = imgFileName.substring(imgFileName.lastIndexOf("."));
			String uuid = UUID.randomUUID().toString(); // Generating UUID

			// Constructing new filename with UUID and file extension
			String newFileName = uuid + fileExtension;
			String uploadPath = request.getServletContext().getRealPath("/resources/images/") + newFileName;

			try {
				FileOutputStream fos = new FileOutputStream(uploadPath);
				InputStream is = file.getInputStream();
				byte[] data = new byte[is.available()];
				is.read(data);
				fos.write(data);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Set the image property of FoodSet with the new file path
			newFileName = "/resources/images/" + uuid + fileExtension;
			foodSet.setImage(newFileName);
			// Perform the necessary database operations (insert/update)
			foodSetService.insert(foodSet);
			foodSetService.insertFood(foodSet);
			redirectAttributes.addFlashAttribute("addMessage", "Success Adding New FoodSet !");
			// Redirect to the desired view
			return new ModelAndView("redirect:/owner/foodSet");
		} else {
			// Handle the case when no file is uploaded
			System.out.println("No file uploaded.");
			redirectAttributes.addFlashAttribute("errorFoodSet", "Choose Image For New FoodSet !");
			return new ModelAndView("redirect:/owner/addProduct");
		}
	}

	private int countSelectedOptions(String select) {
		return Arrays.asList(select.split(",")).size();
	}

	@RequestMapping(value = "/foodSet", method = RequestMethod.GET)
	public ModelAndView foodSet(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		if (orderTypeService.findOrderTypeByRestaurantId(user.getRestaurantId()) == null
				|| orderTypeService.findOrderTypeByRestaurantId(user.getRestaurantId()).isEmpty()) {
			return new ModelAndView("redirect:/owner/ordertype");
		} else if (paymentTypeService.findPaymentByRestaurantId(user.getRestaurantId()) == null
				|| paymentTypeService.findPaymentByRestaurantId(user.getRestaurantId()).isEmpty()) {
			return new ModelAndView("redirect:/owner/paymenttype");
		} else if (categoryService.findAll(user.getRestaurantId()) == null
				|| categoryService.findAll(user.getRestaurantId()).isEmpty()) {
			return new ModelAndView("redirect:/owner/category");
		} else if (foodService.findAll(user.getRestaurantId()) == null
				|| foodService.findAll(user.getRestaurantId()).isEmpty()) {
			return new ModelAndView("redirect:/owner/addProduct");
		} else {

			model.addAttribute("foodSets", foodSetService.findAll(user.getRestaurantId()));
			return new ModelAndView("foodsets.html", "foodSet", new FoodSet());
		}

	}

	@RequestMapping(value = "/updateFoodSet", method = RequestMethod.POST)
	public ModelAndView updateFood(@ModelAttribute FoodSet foodSet, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException, ServletException {

		if (foodSetService.checkFoodSetIsEmpty(foodSet)) {
			redirectAttributes.addFlashAttribute("error", "Fill FoodSet Data !");
			redirectAttributes.addFlashAttribute("foodSetId", foodSet.getId());
			return new ModelAndView("redirect:/owner/foodSet");
		} else if (foodSetService.checkFoodSetInput(foodSet)) {
			redirectAttributes.addFlashAttribute("foodSetId", foodSet.getId());
			redirectAttributes.addFlashAttribute("error", "FoodSet Data is Too Long !");
			return new ModelAndView("redirect:/owner/foodSet");
		} else if (foodSetService.checkFoodSetNameAlreadyExist(foodSet.getName(), foodSet.getId())) {
			redirectAttributes.addFlashAttribute("error", "This FoodSet name is already exist");
			redirectAttributes.addFlashAttribute("foodSetId", foodSet.getId());
			return new ModelAndView("redirect:/owner/foodSet");
		} else {
			foodSetService.updateFoodSet(foodSet);
			redirectAttributes.addFlashAttribute("foodSetId", foodSet.getId());
			redirectAttributes.addFlashAttribute("message", "Success Foodset Update !");
			return new ModelAndView("redirect:/owner/foodSet");

		}

	}

	@RequestMapping(value = "/updateFoodSetImage", method = RequestMethod.POST)
	public ModelAndView updateFoodSetImage(@ModelAttribute FoodSet foodSet, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException, ServletException {

		Part file = request.getPart("image");
		if (file != null && file.getSubmittedFileName() != "") {
			String imgFileName = file.getSubmittedFileName();
			String fileExtension = imgFileName.substring(imgFileName.lastIndexOf("."));
			String uuid = UUID.randomUUID().toString(); // Generating UUID

			// Constructing new filename with UUID and file extension
			String newFileName = uuid + fileExtension;
			String uploadPath = request.getServletContext().getRealPath("/resources/images/") + newFileName;

			try {
				FileOutputStream fos = new FileOutputStream(uploadPath);
				InputStream is = file.getInputStream();
				byte[] data = new byte[is.available()];
				is.read(data);
				fos.write(data);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			newFileName = "/resources/images/" + uuid + fileExtension;
			foodSet.setImage(newFileName);

			foodSetService.updateFoodSetImg(foodSet);
			redirectAttributes.addFlashAttribute("foodSetId", foodSet.getId());
			redirectAttributes.addFlashAttribute("messageImage", "Success Foodset Update !");
			return new ModelAndView("redirect:/owner/foodSet");

		} else {
			// Handle the case when no file is uploaded
			System.out.println("No file uploaded.");
			redirectAttributes.addFlashAttribute("foodSetId", foodSet.getId());
			redirectAttributes.addFlashAttribute("errorImage", "Choose Image To Update FoodSet Image !");
			return new ModelAndView("redirect:/owner/foodSet");
		}

	}

	@RequestMapping(value = "/deleteFoodSet", method = RequestMethod.POST)
	public ModelAndView deleteFood(@ModelAttribute FoodSet foodSet, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		foodSetService.deleteFoodSetImg(foodSet.getId());
		foodSetService.deleteFoodForFoodSet(foodSet.getId());
		foodSetService.delete(foodSet.getId());

		redirectAttributes.addFlashAttribute("msg", "Success Foodset Update !");
		return new ModelAndView("redirect:/owner/foodSet");
	}


	@RequestMapping(value = "/foodList/{foodSetId}", method = RequestMethod.GET)
	public ModelAndView foodList(@PathVariable("foodSetId") int foodSetId, ModelMap model, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		model.addAttribute("foodforfoodsets", foodSetService.findFoodForFoodSetByFoodSetId(foodSetId));
		model.addAttribute("foods", foodService.findAll(user.getRestaurantId()));
		model.addAttribute("food", new Food());
		model.addAttribute("foodSetId", foodSetId);
		return new ModelAndView("foodlist.html", "foodSet", new FoodSet());
	}

	@RequestMapping(value = "/addFoodForFoodSet/{foodSetId}", method = RequestMethod.POST)
	public ModelAndView insertFoodForFoodSet(@PathVariable("foodSetId") int foodSetId, @ModelAttribute FoodSet foodSet,
			ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");

		foodSet.setId(foodSetId);
		foodSetService.insertFoodForFoodSet(foodSet);
		model.addAttribute("food", new Food());

		model.addAttribute("foodforfoodsets", foodSetService.findFoodForFoodSetByFoodSetId(foodSetId));
		model.addAttribute("foods", foodService.findAll(user.getRestaurantId()));
		model.addAttribute("message", "Success Add Food !");
		return new ModelAndView("foodlist.html", "foodSet", foodSet);
	}

	@RequestMapping(value = "/deleteForFood/{foodSetId}", method = RequestMethod.POST)
	public ModelAndView deleteForFood(@PathVariable("foodSetId") int foodSetId, @ModelAttribute FoodSet foodSet,
			ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");

		model.addAttribute("food", new Food());

		foodSetService.deleteForFood(foodSet.getId());
		model.addAttribute("foodforfoodsets", foodSetService.findFoodForFoodSetByFoodSetId(foodSetId));
		model.addAttribute("foods", foodService.findAll(user.getRestaurantId()));
		model.addAttribute("foodSets", foodSetService.findAll(user.getRestaurantId()));
		model.addAttribute("message", "Success Delete Food !");
		return new ModelAndView("foodlist.html", "foodSet", foodSet);
	}

	@RequestMapping(value = "/updateFoodForFoodSet/{foodSetId}", method = RequestMethod.POST)
	public ModelAndView updateFoodForFoodSet(@PathVariable("foodSetId") int foodSetId, @ModelAttribute FoodSet foodSet,
			ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");

		if (foodSetService.checkFoodIdAlreadyExistAtUpdate(foodSet.getId(), Integer.parseInt(foodSet.getSelect()),
				foodSetId)) {
			model.addAttribute("foodforfoodsets", foodSetService.findFoodForFoodSetByFoodSetId(foodSetId));
			model.addAttribute("foods", foodService.findAll(user.getRestaurantId()));

			model.addAttribute("food", new Food());

			model.addAttribute("error", "This Food is already exist for Update!!");
			return new ModelAndView("foodlist.html", "foodSet", new FoodSet());
		} else {
			model.addAttribute("food", new Food());
			foodSetService.updateFoodForFoodSet(foodSet);
			model.addAttribute("foodforfoodsets", foodSetService.findFoodForFoodSetByFoodSetId(foodSetId));
			model.addAttribute("foods", foodService.findAll(user.getRestaurantId()));

			model.addAttribute("message", "Success Update Food !");
			return new ModelAndView("foodlist.html", "foodSet", new FoodSet());
		}

	}
}
