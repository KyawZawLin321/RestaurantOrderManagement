package com.ai.jwd42.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ai.jwd42.dto.CartItem;
import com.ai.jwd42.dto.Order;
import com.ai.jwd42.dto.OrderMessage;
import com.ai.jwd42.dto.User;
import com.ai.jwd42.service.OrderService;
import com.ai.jwd42.service.PaymentService;
import com.ai.jwd42.service.UserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Controller
@MultipartConfig
@RequestMapping("/user")
public class UserOrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/order" }, method = RequestMethod.POST)
	public ModelAndView order(@ModelAttribute() Order order, ModelMap model, HttpSession session,
			HttpServletRequest request) throws IOException, ServletException {

		Part file = request.getPart("screenShot");
		if (file != null && file.getSubmittedFileName()!="") {
			String imgFileName = file.getSubmittedFileName();
			String fileExtension = imgFileName.substring(imgFileName.lastIndexOf("."));
			String uuid = UUID.randomUUID().toString(); // Generating UUID

			// Constructing new filename with UUID and file extension
			String newFileName = uuid + fileExtension;
			String uploadPath = request.getServletContext().getRealPath("/resources/screenshots/") + newFileName;

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
			newFileName = "/resources/screenshots/" + uuid + fileExtension;
			order.setScreenShot(newFileName);

		} else {
			// Handle the case when no file is uploaded
			System.out.println("No file uploaded.");
		}

		// generate order number
		String orderNumber = generateOrderNumber();
		List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
		int restaurantId = (int) session.getAttribute("restaurantId");
		for (CartItem cartItem : cartItems) {
			cartItem.setOrderNumber(orderNumber);

		}
	
		User user = (User) userService.findUserByEmail((String) session.getAttribute("username"));
		order.setRestaurantId(restaurantId);
		order.setCreateDate(java.time.LocalDateTime.now());
		order.setUserId(user.getId());
		order.setOrderNumber(orderNumber);

		String filePath = request.getServletContext().getRealPath("/resources/qrcodes/") + orderNumber + ".png";
		// Generate QR Code
		generateQRCode(orderNumber, filePath);
		String qrcode = "/resources/qrcodes/" + orderNumber + ".png";
		order.setQrcode(qrcode);

		// insert order
		orderService.insertOrder(order);
		// insert user payment info
		paymentService.insertUserPaymentInfo(order);

		// insert orderItem
		orderService.insertOrderItem(cartItems);
		System.out.println(qrcode);
		System.out.println(orderNumber);

		// insert orderReceipt
		orderService.insertOrderReceipt(orderNumber);

		// clean cartdata
		session.removeAttribute("restaurantId");
		session.removeAttribute("cartItems");

		return new ModelAndView("redirect:/user/orderReceipt/{orderNumber}", "orderNumber", orderNumber);
	}

	@RequestMapping(value = "/orderReceipt/{orderNumber}", method = RequestMethod.GET)
	public ModelAndView orderReceipt(@PathVariable("orderNumber") String orderNumber, ModelMap model,
			HttpServletRequest request) {
		User user = new User();
		model.addAttribute("message", "Welcome");
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (ck.getName().equals("rememberUser")) {
					user.setEmail(ck.getValue());
					model.addAttribute("back", " back !");
				}
			}
		}
		if (model.get("msg") != null) {
			model.addAttribute("msg", model.get("msg"));
		}
		if (model.get("back") != null) {
			model.addAttribute("back", model.get("back"));
		}
		if (model.get("error") != null) {
			model.addAttribute("error", model.get("error"));
		}
		if (model.get("registerMessage") != null) {
			model.addAttribute("registerMessage", model.get("registerMessage"));
		}
		if (model.get("registerSuccess") != null) {
			model.addAttribute("registerSuccess", model.get("registerSuccess"));
		}

		model.addAttribute("orderReceipt", orderService.findOrderReceiptByOrderNumber(orderNumber));
		model.addAttribute("orderItems", orderService.findOrderItemByOrderNumber(orderNumber));
		return new ModelAndView("userOrderReceipt.html", "user", new User());
	}

//	@RequestMapping(value = "/qrcode", method = RequestMethod.GET)
//	public ModelAndView getQrcode(ModelMap model, HttpSession session, HttpServletRequest request) {
//
//		String filePath = "C:/Users/ASUS/OneDrive/Desktop/JWD/java exercise2/Workout/Restaurant_Order_Management/src/main/webapp/resources/qrcodes/"
//				+ "1234567856785678" + ".png";
//		generateQRCode("1234567856785678", filePath);
//
//		model.addAttribute("orders", orderService.findOrderByUserId(4));
//		return new ModelAndView("userOrderRecord.html", "user", new User());
//
//	}

	@RequestMapping(value = "/orderRecord", method = RequestMethod.GET)
	public ModelAndView userOrderRecord(ModelMap model, HttpSession session, HttpServletRequest request) {
		User user = new User();
		model.addAttribute("message", "Welcome");
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (ck.getName().equals("rememberUser")) {
					user.setEmail(ck.getValue());
					model.addAttribute("back", " back !");
				}
			}
		}
		if (model.get("msg") != null) {
			model.addAttribute("msg", model.get("msg"));
		}
		if (model.get("back") != null) {
			model.addAttribute("back", model.get("back"));
		}
		if (model.get("error") != null) {
			model.addAttribute("error", model.get("error"));
		}
		if (model.get("registerMessage") != null) {
			model.addAttribute("registerMessage", model.get("registerMessage"));
		}
		if (model.get("registerSuccess") != null) {
			model.addAttribute("registerSuccess", model.get("registerSuccess"));
		}
		if (session.getAttribute("username") != null) {
			String email = (String) session.getAttribute("username");
			User users = userService.findUserByEmail(email);
			model.addAttribute("orders", orderService.findOrderByUserId(users.getId()));

			return new ModelAndView("userOrderRecord.html", "user", user);
		} else {

			return new ModelAndView("userOrderRecord.html", "user", user);
		}

	}

	@RequestMapping(value = "/orderMessage", method = RequestMethod.GET)
	public ModelAndView userOrderMessage(ModelMap model, HttpSession session, HttpServletRequest request) {
		User user = new User();
		model.addAttribute("message", "Welcome");
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (ck.getName().equals("rememberUser")) {
					user.setEmail(ck.getValue());
					model.addAttribute("back", " back !");
				}
			}
		}
//		if (model.get("msg") != null) {
//			model.addAttribute("msg", model.get("msg"));
//		}
//		if (model.get("back") != null) {
//			model.addAttribute("back", model.get("back"));
//		}
//		if (model.get("error") != null) {
//			model.addAttribute("error", model.get("error"));
//		}
//		if (model.get("registerMessage") != null) {
//			model.addAttribute("registerMessage", model.get("registerMessage"));
//		}
//		if (model.get("registerSuccess") != null) {
//			model.addAttribute("registerSuccess", model.get("registerSuccess"));
//		}
		if (session.getAttribute("username") != null && session.getAttribute("role") == "user") {
			String email = (String) session.getAttribute("username");
			User users = userService.findUserByEmail(email);
			List<OrderMessage> messages = orderService.findOrderMessage(users.getId());
			orderService.readOrderMessage(messages);
			model.addAttribute("messages", messages);
			System.out.println("msg is"+messages);
			return new ModelAndView("userOrderMessage.html", "user", user);
		} else {

			return new ModelAndView("userOrderMessage.html", "user", user);
		}

	}

	@RequestMapping(value = "/orderDetail/{orderNumber}", method = RequestMethod.GET)
	public ModelAndView orderDetail(@PathVariable("orderNumber") String orderNumber, ModelMap model,HttpServletRequest request) {
		User user = new User();
		model.addAttribute("message", "Welcome");
		if (request.getCookies() != null) {
			for (Cookie ck : request.getCookies()) {
				if (ck.getName().equals("rememberUser")) {
					user.setEmail(ck.getValue());
					model.addAttribute("back", " back !");
				}
			}
		}
		if (model.get("msg") != null) {
			model.addAttribute("msg", model.get("msg"));
		}
		if (model.get("back") != null) {
			model.addAttribute("back", model.get("back"));
		}
		if (model.get("error") != null) {
			model.addAttribute("error", model.get("error"));
		}
		if (model.get("registerMessage") != null) {
			model.addAttribute("registerMessage", model.get("registerMessage"));
		}
		if (model.get("registerSuccess") != null) {
			model.addAttribute("registerSuccess", model.get("registerSuccess"));
		}

		model.addAttribute("orderItems", orderService.findOrderItemByOrderNumber(orderNumber));
		model.addAttribute("orderNumber", orderNumber);
		return new ModelAndView("userOrderDetail.html", "user", new User());
	}

	public String generateOrderNumber() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = dateFormat.format(new Date());

		Random random = new Random();
		int randomDigits = 1000 + random.nextInt(9000); // Random 4-digit number

		return "ORD-" + timestamp + "-" + randomDigits;
	}

	private void generateQRCode(String orderNumber, String filePath) {
		int width = 200;
		int height = 200;
		int margin = 0; // Set margin to zero for no padding

		BitMatrix bitMatrix;
		try {
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.MARGIN, margin);

			bitMatrix = new MultiFormatWriter().encode(orderNumber, BarcodeFormat.QR_CODE, width, height, hints);
			Path path = FileSystems.getDefault().getPath(filePath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
