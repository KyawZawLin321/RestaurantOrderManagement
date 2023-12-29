package com.ai.jwd42.controller;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ai.jwd42.dto.OrderMessage;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.CategoryService;
import com.ai.jwd42.service.FoodService;
import com.ai.jwd42.service.OrderService;
import com.ai.jwd42.service.OrderTypeService;
import com.ai.jwd42.service.PaymentTypeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Controller
@RequestMapping("/owner")
public class OwnerOrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private FoodService foodService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private OrderTypeService orderTypeService;
	@Autowired
	private PaymentTypeService paymentTypeService;

	private void generateQRCode(String orderNumber, String filePath) {
		int width = 300;
		int height = 300;

		BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(orderNumber, BarcodeFormat.QR_CODE, width, height);
			Path path = FileSystems.getDefault().getPath(filePath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String generateOrderNumber() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = dateFormat.format(new Date());

		Random random = new Random();
		int randomDigits = 1000 + random.nextInt(9000); // Random 4-digit number

		return "ORD-" + timestamp + "-" + randomDigits;
	}

	@RequestMapping(value = "/orderInfo/{orderNumber}", method = RequestMethod.GET)
	public ModelAndView orderPaymentInfo(@PathVariable("orderNumber") String orderNumber, ModelMap model) {

		model.addAttribute("orderInfo", orderService.findOrderInfo(orderNumber));
		model.addAttribute("orderNumber", orderNumber);
		return new ModelAndView("ownerOrderPayment.html");
	}

	@RequestMapping(value = "/orderDetail/{orderNumber}", method = RequestMethod.GET)
	public ModelAndView foodList(@PathVariable("orderNumber") String orderNumber, ModelMap model) {

		model.addAttribute("orderItems", orderService.findOrderItemByOrderNumber(orderNumber));
		model.addAttribute("orderNumber", orderNumber);
		return new ModelAndView("ownerOrderDetail.html");
	}

	@RequestMapping(value = "/confirmOrder", method = RequestMethod.POST)
	public ModelAndView comfirmOrder(@ModelAttribute OrderMessage orderMessage, HttpSession session,
			RedirectAttributes redirectAttributes) {

		orderService.insertOrderMessage(orderMessage);
		orderService.confirmOrder(orderMessage.getOrderNumber());

		redirectAttributes.addFlashAttribute("isConfirm", true);
		redirectAttributes.addFlashAttribute("orderNumber", orderMessage.getOrderNumber());

		return new ModelAndView("redirect:/owner/allOrderConfirm");

	}

	@RequestMapping(value = "/rejectOrder", method = RequestMethod.POST)
	public ModelAndView rejectOrder(@ModelAttribute OrderMessage orderMessage, RedirectAttributes redirectAttributes) {

		orderService.rejectOrder(orderMessage.getOrderNumber());
		orderService.insertOrderMessage(orderMessage);

		redirectAttributes.addFlashAttribute("isReject", true);
		redirectAttributes.addFlashAttribute("orderNumber", orderMessage.getOrderNumber());
		return new ModelAndView("redirect:/owner/allOrderReject");

	}

	@RequestMapping(value = "/allOrderPending", method = RequestMethod.GET)
	public ModelAndView orderPending(ModelMap model, HttpSession session) {

		User user = (User) session.getAttribute("user");
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
			model.addAttribute("orders", orderService.findPendingOrder(user.getRestaurantId()));

			return new ModelAndView("ownerOrderPending.html", "orderMessage", new OrderMessage());
		}

	}

	@RequestMapping(value = "/allOrderReject", method = RequestMethod.GET)
	public ModelAndView orderReject(ModelMap model, HttpSession session) {

		User user = (User) session.getAttribute("user");
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
			Boolean isReject = (Boolean) model.get("isReject");
			String orderNumber = (String) model.get("orderNumber");
			if (isReject != null) {
				model.addAttribute("message", orderNumber + " Rejection process successful");
			}
			model.addAttribute("orders", orderService.findRejectOrder(user.getRestaurantId()));

			return new ModelAndView("ownerOrderReject.html", "orderMessage", new OrderMessage());
		}

	}

	@RequestMapping(value = "/allOrderConfirm", method = RequestMethod.GET)
	public ModelAndView allOwnerOrder(ModelMap model, HttpSession session) {

		User user = (User) session.getAttribute("user");
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
			model.addAttribute("orders", orderService.findConfirmOrder(user.getRestaurantId()));

			// Check if isConfirm is true
			Boolean isConfirm = (Boolean) model.get("isConfirm");

			String orderNumber = (String) model.get("orderNumber");
			if (isConfirm != null && isConfirm) {
				model.addAttribute("message", orderNumber + " Confirmation process successful");
			}
			return new ModelAndView("ownerOrderConfirm.html");
		}
	}

}
