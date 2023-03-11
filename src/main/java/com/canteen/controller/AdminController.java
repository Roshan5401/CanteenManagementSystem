
package com.canteen.controller;

import java.io.IOException;

import java.security.Principal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.sql.results.graph.instantiation.internal.ArgumentDomainResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.MergedAnnotationCollectors;
import org.springframework.data.repository.query.Param;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.canteen.entities.CanteenUsers;

import com.canteen.entities.OrderEntity;
import com.canteen.entities.menuCanteen;
import com.canteen.repository.CanteenUserRepository;
import com.canteen.repository.MenuRepository;
import com.canteen.repository.OrderRepository;
import com.canteen.service.CanteenService;

import com.canteen.entities.OrderEntity;
import com.canteen.repository.CanteenUserRepository;
import com.canteen.service.OrderService;
import com.canteen.util.FeedbackPDFGenerator;
import com.canteen.util.PreviousOrdersPDFGenerator;
import com.canteen.util.UpcomingOrdersPDFGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;
import net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer.ForReturnTypeName;

@Controller
public class AdminController {

	@Autowired
	OrderService orderService;
	@Autowired
	CanteenUserRepository canteenUserRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	MenuRepository menuRepository;
	@Autowired
	CanteenService canteenService;
	@Autowired
	OrderRepository orderRepository;

	// Display Add and Update menu page
	@GetMapping("/admin/addAndUpdateMenu")
	public String addAndUpdateMenu(Model model) {
		List<menuCanteen> menuItems = new ArrayList<>();
		menuItems = menuRepository.getFoodByEnable(true);
		model.addAttribute("new_food", new menuCanteen());
		model.addAttribute("delete_food", new menuCanteen());
		model.addAttribute("menuItems", menuItems);
		return "admin/addandupdatemenu";
	}
	
	//full validation done no edge cases left
	@PostMapping("/admin/addfood")
	public RedirectView addfood(@RequestParam("name")String name,@RequestParam("type")String type,@RequestParam("price")String price,@RequestParam("foodServedDate")String date) {
		System.out.println("****");
		menuCanteen menu=new menuCanteen();
		
		long count1 = price.chars().filter(ch -> ch == '.').count();
		long count2=price.chars().filter(ch->(ch>='a' && ch<='z') || (ch>='A' && ch<='Z')).count();
		if(count1>1 || count2>0)
			return new RedirectView("/admin/addAndUpdateMenu");
		if(Double.parseDouble(price)<1)
			return new RedirectView("/admin/addAndUpdateMenu");
		menu.setName(name);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = dateFormat.parse(date);
            java.sql.Date sqlDate = new java.sql.Date(date1.getTime());
            menu.setFoodServedDate(sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
		menu.setType(type);
		
		menu.setEnable(true);
		Double d = Double.parseDouble(price);
		DecimalFormat dfor = new DecimalFormat("0.00");
		String s = dfor.format(d);
		Double dfinal = Double.valueOf(s);
		menu.setPrice(dfinal);
		menuRepository.save(menu);
		return new RedirectView("/admin/addAndUpdateMenu");
	}

	@GetMapping("/admin/deletefood/{ID}")
	public RedirectView deletefood(@ModelAttribute("old_food") menuCanteen menu, @PathVariable("ID") Integer Id) {

		Optional<menuCanteen> optional = menuRepository.findById(Id);
		menuCanteen food = optional.get();
		food.setEnable(false);
		menuRepository.save(food);
		return new RedirectView("/admin/addAndUpdateMenu");
	}

	// Display Update User Profile page
	@GetMapping("/admin/updateUserProfile")
	public String updateUserProfile(Model model, Principal principal) {
		String userName = principal.getName();
		CanteenUsers current_user = canteenUserRepository.findByEmail(userName);
		model.addAttribute("user", current_user);
		model.addAttribute("update_user", new CanteenUsers());
		return "admin/updateuserprofileadmin";
	}

	// Display previous orders page
	@GetMapping("/admin/viewPreviousOrders")
	public String viewPreviousOrders(Model m) {
		List<OrderEntity> orders = this.orderService.getAllOrders("Delivered");
		m.addAttribute("orders", orders);
		m.addAttribute("date", "null");
		m.addAttribute("id", "null");
		return "admin/viewpreviousordersadmin";
	}

	// Display upcoming orders page
	@GetMapping("/admin/viewUpcomingOrders")
	public String viewUpcomingOrders(Model m) {
		List<OrderEntity> orders = this.orderService.getAllOrders("Booked");
		System.out.println(orders);
		m.addAttribute("orders", orders);
		m.addAttribute("date", "null");
		m.addAttribute("id", "null");
		return "admin/viewupcomingordersadmin";
	}
	
	//change is made
	//alert required
	@GetMapping("/admin/findUserProfile")
	public String findUserProfile(Model model, Principal principal, @ModelAttribute("userEmail") String userEmail) {
		CanteenUsers current_user = canteenUserRepository.findByEmail(userEmail);
		if(current_user==null) {
			CanteenUsers currrent_users=canteenUserRepository.findByRole("ROLE_ADMIN");
			model.addAttribute("user",currrent_users);
			model.addAttribute("update_user",new CanteenUsers());
			System.out.println("No email found");
		}
		else {
		model.addAttribute("user", current_user);
		model.addAttribute("update_user", new CanteenUsers());
		}
		return "admin/updateuserprofileadmin";
	}

	@PostMapping("/admin/updateUserProfileRout")
	public RedirectView updateUserProfile(Model model, Principal principal,
			@ModelAttribute("newpassword") String newPasword, @ModelAttribute("update_user") CanteenUsers users,
			@ModelAttribute("token_email") String tokenEmail,RedirectAttributes attributes) {
		System.out.println("****");
		System.out.println(tokenEmail);
		CanteenUsers current_user = canteenUserRepository.findByEmail(tokenEmail);
		current_user.setName(users.getName());
		current_user.setPhone(users.getPhone());
		System.out.println(users.getName());
		if (newPasword.length() > 0) {
			current_user.setPassword(bCryptPasswordEncoder.encode(newPasword));
		}
		canteenUserRepository.save(current_user);
		attributes.addAttribute("success",1);
		return new RedirectView("/admin/updateUserProfile");
	}

	// Handle view Previous orders filter
	@GetMapping("/admin/previousorderbyfilter")
	public String previousOrdersByFilter(@ModelAttribute("vieworderbydate") String date,
			@ModelAttribute("userId") String userId, Model m) throws ParseException {
		if (date.length() == 0 && userId.length() != 0) {
			System.out.println("Date is null and user id is not");
			List<OrderEntity> orders = this.orderService.getAllOrdersByUserId("Delivered", userId);
			m.addAttribute("orders", orders);
			m.addAttribute("date", "null");
			m.addAttribute("id", userId);
		} else if (date.length() != 0 && userId.length() == 0) {
			System.out.println("Date is not null and User id is null");
			LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
			List<OrderEntity> orders = this.orderService.getAllOrdersByStatusAndDate("Delivered", date1);
			m.addAttribute("orders", orders);
			m.addAttribute("id", "null");
			m.addAttribute("date", date);
		} else if (date.length() != 0 && userId.length() != 0) {
			LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
			List<OrderEntity> orders = this.orderService.getAllOrdersByDateAndUserId(date1, userId, "Delivered");
			m.addAttribute("orders", orders);
			m.addAttribute("date", date);
			m.addAttribute("id", userId);
		} else {
			List<OrderEntity> orders = this.orderService.getAllOrders("Delivered");
			System.out.println(orders);
			m.addAttribute("orders", orders);
			m.addAttribute("date", "null");
			m.addAttribute("id", "null");
		}

		return "admin/viewpreviousordersadmin";

	}

	// Handle Upcoming orders filter
	@GetMapping("/admin/upcomingordersbyfilter")
	public String upcomingOrdersByFilter(@ModelAttribute("vieworderbydate") String date,
			@ModelAttribute("userId") String userId, Model m) {
		if (date.length() == 0 && userId.length() != 0) {
			System.out.println("Date is null and user id is not");
			List<OrderEntity> orders = this.orderService.getAllOrdersByUserId("Booked", userId);
			m.addAttribute("orders", orders);
			m.addAttribute("date", "null");
			m.addAttribute("id", userId);
		} else if (date.length() != 0 && userId.length() == 0) {
			System.out.println("Date is not null and User id is null");
			LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
			List<OrderEntity> orders = this.orderService.getAllOrdersByStatusAndDate("Booked", date1);
			m.addAttribute("orders", orders);
			m.addAttribute("id", "null");
			m.addAttribute("date", date);
		} else if (date.length() != 0 && userId.length() != 0) {
			LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
			List<OrderEntity> orders = this.orderService.getAllOrdersByDateAndUserId(date1, userId, "Booked");
			m.addAttribute("orders", orders);
			m.addAttribute("date", date);
			m.addAttribute("id", userId);
		} else {
			List<OrderEntity> orders = this.orderService.getAllOrders("Booked");
			System.out.println(orders);
			m.addAttribute("orders", orders);
			m.addAttribute("date", "null");
			m.addAttribute("id", "null");
		}

		return "admin/viewupcomingordersadmin";
	}

	@GetMapping("/admin/cancleOrder")
	public String cancleOrder(@ModelAttribute("disabledOrderId") String orderId, Model m) {
		int id_new = Integer.parseInt(orderId);
		Optional<OrderEntity> optional2 = orderRepository.findById(id_new);
		OrderEntity orderEntity = optional2.get();
		Double price = orderEntity.getTotalPrice();
		this.orderService.deleteOrder(orderId);
		List<OrderEntity> orders = this.orderService.getAllOrders("Booked");
		int id = orderEntity.getCanteenUsers().getId();
		Optional<CanteenUsers> optional = canteenUserRepository.findById(id);
		CanteenUsers canteenUsers = optional.get();
		Double tempPrice = canteenUsers.getWallet() + price;
		
		DecimalFormat dfor = new DecimalFormat("0.00");
		String s = dfor.format(tempPrice);
		Double finalPrice = Double.valueOf(s);
		
		canteenUsers.setWallet(finalPrice);
		canteenUserRepository.save(canteenUsers);
		m.addAttribute("orders", orders);
		return "admin/viewupcomingordersadmin";
	}

	// Display view Feedbacks page
	// this will display all the feedbacks of all the user-Done
	@GetMapping("/admin/viewFeedbacks")
	public String viewFeedbacks(Model model) {
		List<OrderEntity> allFeedBacks = this.orderService.getAllOrders("Delivered");
		List<OrderEntity> finalFeedbacks = allFeedBacks.stream().filter(feed -> feed.getFeedback() != null)
				.collect(Collectors.toList());
		System.out.println(allFeedBacks.size());
		model.addAttribute("feedbacks", finalFeedbacks);
		model.addAttribute("food", "null");
		return "admin/viewfeedbacksadmin";
	}

	@GetMapping("/admin/viewfeedbackbyname")
	public String viewFeedbackResult(@ModelAttribute("food") String food, Model m) {
		System.out.println(food);
		List<OrderEntity> orders = this.orderService.getAllOrders("Delivered");
		if (food == null || food.isBlank()) {
			List<OrderEntity> finalFeedbacks = orders.stream().filter(feed -> feed.getFeedback() != null)
					.collect(Collectors.toList());
			m.addAttribute("feedbacks", finalFeedbacks);
			m.addAttribute("food", "null");
			return "/admin/viewfeedbacksadmin";
		}
		List<OrderEntity> finalOrders = orders.stream().filter(order -> order.getFood().getName().equals(food))
				.collect(Collectors.toList());
		List<OrderEntity> finalFeedbacks = finalOrders.stream().filter(feed -> feed.getFeedback() != null)
				.collect(Collectors.toList());
		m.addAttribute("feedbacks", finalFeedbacks);
		m.addAttribute("token", finalFeedbacks);
		m.addAttribute("food", food);
		return "/admin/viewfeedbacksadmin";

	}

	@GetMapping("/admin/feedbackdownload")
	public String feedbackDownload(HttpServletResponse response, @ModelAttribute("food") String food, Model m)
			throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
		String currentDateTime = dateFormat.format(new Date());
		String headerkey = "Content-Disposition";
		String headervalue = "attachment; filename=Feedbacks" + currentDateTime + ".pdf";
		response.setHeader(headerkey, headervalue);
		List<OrderEntity> orders = this.orderService.getAllOrders("Delivered");
		List<OrderEntity> finalFeedbacks = null;
		if (food.equals("null")) {
			finalFeedbacks = orders.stream().filter(feed -> feed.getFeedback() != null).collect(Collectors.toList());
			System.out.println(finalFeedbacks);
			m.addAttribute("feedbacks", finalFeedbacks);
			m.addAttribute("food", "null");
		}

		else {
			List<OrderEntity> finalOrders = orders.stream().filter(order -> order.getFood().getName().equals(food))
					.collect(Collectors.toList());
			finalFeedbacks = finalOrders.stream().filter(feed -> feed.getFeedback() != null)
					.collect(Collectors.toList());
			m.addAttribute("feedbacks", finalFeedbacks);
			m.addAttribute("food", food);
		}

		FeedbackPDFGenerator generator = new FeedbackPDFGenerator();

		generator.generate(finalFeedbacks, response);
		return "/admin/viewfeedbackadmin";
	}

	@GetMapping("/admin/deleteOrderFeedback/{Id}")
	public RedirectView deletefeedback(@PathVariable("Id") int Id) {
		Optional<OrderEntity> optional = orderRepository.findById(Id);
		OrderEntity orderEntity = optional.get();
		orderEntity.setFeedback(null);
		orderRepository.save(orderEntity);
		return new RedirectView("/admin/viewFeedbacks");
	}

	@GetMapping("/admin/deliveredOrder/{orderId}")
	public RedirectView deliveredOrder(@PathVariable("orderId") int id) {
		System.out.println(id);
		OrderEntity order = this.orderService.getbyOrderId(id);
		System.out.println(order);
		order.setStatus("Delivered");
		this.orderRepository.save(order);
		return new RedirectView("/admin/viewUpcomingOrders");
	}

	@GetMapping("/admin/previousordersdownload")
	public String previousOrdersDownload(HttpServletResponse response, @ModelAttribute("date") String date,
			@ModelAttribute("id") String id, Model m) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
		String currentDateTime = dateFormat.format(new Date());
		String headerkey = "Content-Disposition";
		String headervalue = "attachment; filename=PreviousOrders" + currentDateTime + ".pdf";
		response.setHeader(headerkey, headervalue);
		System.out.println(date);
		System.out.println(id);
		List<OrderEntity> ordersList = null;
		if (date.equals("null") && id.equals("null")) {
			ordersList = orderService.getAllOrders("Delivered");
		}

		else if (date.equals("null")) {
			System.out.println("Date is null and user id is not");
			ordersList = this.orderService.getAllOrdersByUserId("Delivered", id);
			m.addAttribute("orders", ordersList);
		}

		else if (id.equals("null")) {
			System.out.println("Date is not null and User id is null");
			LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
			ordersList = this.orderService.getAllOrdersByStatusAndDate("Delivered", date1);
			m.addAttribute("orders", ordersList);
		}

		else {
			LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
			ordersList = this.orderService.getAllOrdersByDateAndUserId(date1, id, "Delivered");
			m.addAttribute("orders", ordersList);
		}

		PreviousOrdersPDFGenerator generator = new PreviousOrdersPDFGenerator();

		generator.generate(ordersList, response);
		return "/admin/viewpreviousordersadmin";
	}

	@GetMapping("/admin/upcomingordersdownload")
	public String upcomingOrdersDownload(HttpServletResponse response, @ModelAttribute("date") String date,
			@ModelAttribute("id") String id, Model m) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
		String currentDateTime = dateFormat.format(new Date());
		String headerkey = "Content-Disposition";
		String headervalue = "attachment; filename=UpcomingOrders" + currentDateTime + ".pdf";
		response.setHeader(headerkey, headervalue);
		System.out.println(date);
		System.out.println(id);
		List<OrderEntity> ordersList = null;
		if (date.equals("null") && id.equals("null")) {
			ordersList = orderService.getAllOrders("Booked");
		}

		else if (date.equals("null")) {
			System.out.println("Date is null and user id is not");
			ordersList = this.orderService.getAllOrdersByUserId("Booked", id);
			m.addAttribute("orders", ordersList);
		}

		else if (id.equals("null")) {
			System.out.println("Date is not null and User id is null");
			LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
			ordersList = this.orderService.getAllOrdersByStatusAndDate("Booked", date1);
			m.addAttribute("orders", ordersList);
		}

		else {
			LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
			ordersList = this.orderService.getAllOrdersByDateAndUserId(date1, id, "Booked");
			m.addAttribute("orders", ordersList);
		}

		UpcomingOrdersPDFGenerator generator = new UpcomingOrdersPDFGenerator();

		generator.generate(ordersList, response);
		return "/admin/viewupcomingordersadmin";
	}

}