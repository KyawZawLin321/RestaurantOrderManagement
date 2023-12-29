package com.ai.jwd42.controller;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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

import com.ai.jwd42.dto.OrderTypeForRestaurant;
import com.ai.jwd42.dto.PaymentTypeForRestaurant;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.OrderTypeService;
import com.ai.jwd42.service.PaymentTypeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Controller
@RequestMapping("/owner")
public class ServiceController {
	@Autowired
	private OrderTypeService ordertypeservice;

	@Autowired
	private PaymentTypeService paymenttypeservice;

	@RequestMapping(value = "/ordertype", method = RequestMethod.GET)
	public ModelAndView ordertype(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		OrderTypeForRestaurant orderTypeForRestaurant = new OrderTypeForRestaurant();
		PaymentTypeForRestaurant paymentTypeForRestaurant = new PaymentTypeForRestaurant();

		model.addAttribute("orderTypeList", ordertypeservice.findOrderType());

		model.addAttribute("orderTypes", ordertypeservice.findOrderTypeByRestaurantId(user.getRestaurantId()));

		model.addAttribute("paymentTypeForRestaurant", paymentTypeForRestaurant);

		return new ModelAndView("ordertype.html", "orderTypeForRestaurant", orderTypeForRestaurant);

	}

	@RequestMapping(value = "/paymenttype", method = RequestMethod.GET)
	public ModelAndView paymemttype(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		PaymentTypeForRestaurant paymentTypeForRestaurant = new PaymentTypeForRestaurant();

		model.addAttribute("paymenttypes", paymenttypeservice.findPaymentByRestaurantId(user.getRestaurantId()));
		model.addAttribute("payMents", paymenttypeservice.findPayMent());

		return new ModelAndView("paymenttype.html", "paymentTypeForRestaurant", paymentTypeForRestaurant);

	}

	@RequestMapping(value = "/deleteOrdertype", method = RequestMethod.POST)
	public ModelAndView deleteOrdertype(@ModelAttribute() OrderTypeForRestaurant orderTypeForRestaurant, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		ordertypeservice.delete(orderTypeForRestaurant.getId());
		redirectAttributes.addFlashAttribute("message", "Success Delete Order Type !");
		return new ModelAndView("redirect:/owner/ordertype");

	}

	@RequestMapping(value = "/deletePaymenttype", method = RequestMethod.POST)
	public ModelAndView deletePaymenttype(
			@ModelAttribute("SpringWeb") PaymentTypeForRestaurant paymentTypeForRestaurant, ModelMap model,
			RedirectAttributes redirectAttributes) {

		paymenttypeservice.delete(paymentTypeForRestaurant.getId());
		redirectAttributes.addFlashAttribute("msg", "Success Delete Payment Type !");
		return new ModelAndView("redirect:/owner/paymenttype");

	}

	@RequestMapping(value = "/updatePaymenttype", method = RequestMethod.POST)
	public ModelAndView updatePaymenttype(@ModelAttribute PaymentTypeForRestaurant paymentTypeForRestaurant,
			ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

		String payMentPhone = paymentTypeForRestaurant.getPhoneNumber();
		if (paymentTypeForRestaurant.getPhoneNumber() == "" && paymentTypeForRestaurant.getAccountNumber() == "") {
			redirectAttributes.addFlashAttribute("paymentTypeId", paymentTypeForRestaurant.getId());
			redirectAttributes.addFlashAttribute("error", "Fill Your Payment Account No. !");
			return new ModelAndView("redirect:/owner/paymenttype");
		}else if(paymenttypeservice.checkPaymentInput(paymentTypeForRestaurant)) {
			redirectAttributes.addFlashAttribute("paymentTypeId", paymentTypeForRestaurant.getId());
			redirectAttributes.addFlashAttribute("error", "Your Payment Account No is Too Long !");
			return new ModelAndView("redirect:/owner/paymenttype");
		} else if (payMentPhone == null || payMentPhone.isEmpty()) {

			// for Bank
			String filePath = request.getServletContext().getRealPath("/resources/qrcodes/")
					+ paymentTypeForRestaurant.getAccountNumber() + ".png";
			// Generate QR Code
			generateQRCode(paymentTypeForRestaurant.getAccountNumber(), filePath);
			String qrcode = "/resources/qrcodes/" + paymentTypeForRestaurant.getAccountNumber() + ".png";

			paymentTypeForRestaurant.setQrcode(qrcode);
			paymentTypeForRestaurant.setId(paymentTypeForRestaurant.getId());

			paymenttypeservice.updatePayMentTypeBank(paymentTypeForRestaurant);
			redirectAttributes.addFlashAttribute("paymentTypeId", paymentTypeForRestaurant.getId());
			redirectAttributes.addFlashAttribute("message", "Success Update Payment Type !");
			return new ModelAndView("redirect:/owner/paymenttype");
		} else {

			// for Ph.no
			String filePath = request.getServletContext().getRealPath("/resources/qrcodes/")
					+ paymentTypeForRestaurant.getPhoneNumber() + ".png";
			// Generate QR Code
			generateQRCode(paymentTypeForRestaurant.getPhoneNumber(), filePath);
			String qrcode = "/resources/qrcodes/" + paymentTypeForRestaurant.getPhoneNumber() + ".png";

			paymentTypeForRestaurant.setQrcode(qrcode);
			paymentTypeForRestaurant.setId(paymentTypeForRestaurant.getId());
			paymenttypeservice.updatePaymenttype(paymentTypeForRestaurant);
			redirectAttributes.addFlashAttribute("paymentTypeId", paymentTypeForRestaurant.getId());
			redirectAttributes.addFlashAttribute("message", "Success Update Payment Type !");
			return new ModelAndView("redirect:/owner/paymenttype");
		}

	}

	@RequestMapping(value = "/addPayMent", method = RequestMethod.POST)
	public ModelAndView addPayMentType(@ModelAttribute PaymentTypeForRestaurant paymentTypeForRestaurant,
			ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		User user = (User) request.getSession().getAttribute("user");
		String payMentPhone = paymentTypeForRestaurant.getPhoneNumber();

		if (paymentTypeForRestaurant.getSelect() == null || paymentTypeForRestaurant.getSelect() == "") {

			redirectAttributes.addFlashAttribute("addError", "Choose One Payment Type !");
			return new ModelAndView("redirect:/owner/paymenttype");
		} else if (paymentTypeForRestaurant.getPhoneNumber() == ""
				&& paymentTypeForRestaurant.getAccountNumber() == "") {

			redirectAttributes.addFlashAttribute("addError", "Fill Your Payment Account No. !");
			return new ModelAndView("redirect:/owner/paymenttype");
		} else if (paymenttypeservice.checkPaymentInput(paymentTypeForRestaurant)) {
			redirectAttributes.addFlashAttribute("addError", "Payemnt Account No Too Long !");
			return new ModelAndView("redirect:/owner/paymenttype");
		} else if (paymenttypeservice.checkPayMentAlreadyExist(Integer.parseInt(paymentTypeForRestaurant.getSelect()),
				user.getRestaurantId())) {
			redirectAttributes.addFlashAttribute("addError", "This PayMentType is already exist !");
			return new ModelAndView("redirect:/owner/paymenttype");
		} else if (payMentPhone == null || payMentPhone.isEmpty()) {
			// for Bank
			String filePath = request.getServletContext().getRealPath("/resources/qrcodes/")
					+ paymentTypeForRestaurant.getAccountNumber() + ".png";
			// Generate QR Code
			generateQRCode(paymentTypeForRestaurant.getAccountNumber(), filePath);
			String qrcode = "/resources/qrcodes/" + paymentTypeForRestaurant.getAccountNumber() + ".png";

			paymentTypeForRestaurant.setQrcode(qrcode);

			paymentTypeForRestaurant.setRestaurantId(user.getRestaurantId());
			paymenttypeservice.insertBank(paymentTypeForRestaurant);
			redirectAttributes.addFlashAttribute("addMessage", "Success Add Payment Type !");
			return new ModelAndView("redirect:/owner/paymenttype");
		} else {
			// for Ph.no
			String filePath = request.getServletContext().getRealPath("/resources/qrcodes/")
					+ paymentTypeForRestaurant.getPhoneNumber() + ".png";
			// Generate QR Code
			generateQRCode(paymentTypeForRestaurant.getPhoneNumber(), filePath);
			String qrcode = "/resources/qrcodes/" + paymentTypeForRestaurant.getPhoneNumber() + ".png";

			paymentTypeForRestaurant.setQrcode(qrcode);
			paymentTypeForRestaurant.setRestaurantId(user.getRestaurantId());
			paymenttypeservice.insert(paymentTypeForRestaurant);
			redirectAttributes.addFlashAttribute("addMessage", "Success Add Payment Type !");
			return new ModelAndView("redirect:/owner/paymenttype");
		}

	}

	@RequestMapping(value = "/addOrderType", method = RequestMethod.POST)
	public ModelAndView addOrderType(@ModelAttribute OrderTypeForRestaurant orderTypeForRestaurant, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		User user = (User) request.getSession().getAttribute("user");

		if (orderTypeForRestaurant.getSelect() == null || orderTypeForRestaurant.getSelect() == "") {

			redirectAttributes.addFlashAttribute("addError", "Choose One Order Type !");
			return new ModelAndView("redirect:/owner/ordertype");
		} else if (ordertypeservice.checkOrderTypeAlreadyExist(Integer.parseInt(orderTypeForRestaurant.getSelect()),
				user.getRestaurantId())) {
			redirectAttributes.addFlashAttribute("addError", "This OrderType is already exist");
			return new ModelAndView("redirect:/owner/ordertype");
		} else {
			orderTypeForRestaurant.setRestaurantId(user.getRestaurantId());

			ordertypeservice.insert(orderTypeForRestaurant);
			redirectAttributes.addFlashAttribute("message", "Success Add Order Type !");
			return new ModelAndView("redirect:/owner/ordertype");
		}

	}

	private void generateQRCode(String data, String filePath) {
		int width = 200;
		int height = 200;
		int margin = 0; // Set margin to zero for no padding

		BitMatrix bitMatrix;
		try {
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.MARGIN, margin);

			bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);
			Path path = FileSystems.getDefault().getPath(filePath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
