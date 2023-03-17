package com.canteen.controller;

import java.io.File;
import java.io.IOException;

import java.security.Principal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.util.StringUtil;
import org.hibernate.sql.results.graph.instantiation.internal.ArgumentDomainResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.MergedAnnotationCollectors;
import org.springframework.data.repository.query.Param;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.canteen.entities.CanteenUsers;

import com.canteen.entities.OrderEntity;
import com.canteen.entities.menuCanteen;
import com.canteen.repository.CanteenUserRepository;
import com.canteen.repository.MenuRepository;
import com.canteen.repository.OrderRepository;
import com.canteen.service.CanteenService;


import com.canteen.service.OrderService;
import com.canteen.util.FeedbackExcelGenerator;
import com.canteen.util.FeedbackPDFGenerator;
import com.canteen.util.PreviousOrdersExcelGenerator;
import com.canteen.util.PreviousOrdersPDFGenerator;
import com.canteen.util.UpcomingOrdersExcelGenerator;

import com.canteen.service.EmailSenderService;
import com.canteen.entities.OrderEntity;
import com.canteen.repository.CanteenUserRepository;
import com.canteen.service.OrderService;
import com.canteen.util.FeedbackPDFGenerator;
import com.canteen.util.PreviousOrdersPDFGenerator;
import com.canteen.util.UpcomingOrdersPDFGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;

import jakarta.mail.Multipart;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.servlet.http.HttpServletRequest;
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
	@Autowired
	private EmailSenderService emailSenderService;

	// Display Add and Update menu page
	@GetMapping("/admin/addAndUpdateMenu")
	public String addAndUpdateMenu(Model model) {
		List<menuCanteen> menuItems = new ArrayList<>();
		menuItems = menuRepository.getFoodByEnable(true);
		model.addAttribute("delete_food", new menuCanteen());
		model.addAttribute("menuItems", menuItems);
		return "admin/addandupdatemenu";
	}
	
	//full validation done no edge cases left
	@PostMapping("/admin/addfood")
	public RedirectView addfood(@RequestParam("name")String name,@RequestParam(name = "type", required = false) String type,@RequestParam("price")String price,@RequestParam("foodServedDate")String date, @RequestParam("itemImage")MultipartFile file    ,RedirectAttributes attributes ) throws IllegalStateException, IOException {
		System.out.println("****");
		menuCanteen menu=new menuCanteen();
		if(type==null || type.isEmpty())
			return new RedirectView("/admin/addAndUpdateMenu");
		if(date==null || date.length()==0 ||name.length()==0 || name==null)
		{
			return new RedirectView("/admin/addAndUpdateMenu");
		}
		long count1 = price.chars().filter(ch -> ch == '.').count();

		long count2=price.chars().filter(ch->(ch>='a' && ch<='z') || (ch>='A' && ch<='Z') || (ch>=33 && ch<=45) || (ch>=58 && ch<=64) ||(ch>=91 && ch<=96) || (ch>=123 && ch<=126) || (ch==47)).count();
		
		if(count1>1 || count2>0) {
			attributes.addAttribute("PriceWrong",1);
			return new RedirectView("/admin/addAndUpdateMenu");
		}
			
		if(Double.parseDouble(price)<1)
		{
			attributes.addAttribute("PriceWrong",1);
			return new RedirectView("/admin/addAndUpdateMenu");
		}
			
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
		/*
		 * final String FOLDER_PATH = "C:/Users/ANURAGB/Downloads/CanteenItemsImages/";
		 * String filePath = FOLDER_PATH+file.getOriginalFilename();
		 * menu.setImage(filePath);
		 * 
		 * file.transferTo(new File(filePath));
		 */
		System.out.println(file.getSize());
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		if (file.getSize() < 500000)
			menu.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
			
		menuRepository.save(menu);
		attributes.addAttribute("Added",1);
		
		
		return new RedirectView("/admin/addAndUpdateMenu");
	}

	@GetMapping("/admin/deletefood/{ID}")
	public RedirectView deletefood(@ModelAttribute("old_food") menuCanteen menu, @PathVariable("ID") Integer Id,RedirectAttributes attributes) {

		Optional<menuCanteen> optional = menuRepository.findById(Id);
		menuCanteen food = optional.get();
		food.setEnable(false);
		menuRepository.save(food);
		attributes.addAttribute("deleted",1);
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

			CanteenUsers currrent_users=canteenUserRepository.findByEmail(principal.getName());

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
			@ModelAttribute("token_email") String tokenEmail, RedirectAttributes attributes) {
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
		attributes.addAttribute("success", 1);
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
		String message="Order Cancelled By admin.\nUsername:"+canteenUsers.getEmail()+"\nfood name: "+orderEntity.getFood().getName()+"\nAmount Refunded to Wallet:Rs"+orderEntity.getTotalPrice()+"\nWallet Balance: Rs"+finalPrice+"\nOrder Date:"+orderEntity.getOrderDate();
		emailSenderService.sendEmail(canteenUsers.getEmail(), "Message from Canteen Management", message);

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
	public String feedbackDownload(HttpServletResponse response,HttpServletRequest request, @ModelAttribute("food") String food, Model m)
			throws DocumentException, IOException {
		String buttonClicked = request.getParameter("pdf");
		if(buttonClicked!=null)//if pdf is clicked this will run
		{
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
		}
		else
		{
			
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

			FeedbackExcelGenerator generator = new FeedbackExcelGenerator();

			generator.generate(finalFeedbacks, response);
		}
		return null;
	}

	@GetMapping("/admin/deleteOrderFeedback/{Id}")
	public RedirectView deletefeedback(@PathVariable("Id") int Id,RedirectAttributes attributes) {
		Optional<OrderEntity> optional = orderRepository.findById(Id);
		OrderEntity orderEntity = optional.get();
		orderEntity.setFeedback(null);
		orderRepository.save(orderEntity);
		attributes.addAttribute("deleted",1);
		return new RedirectView("/admin/viewFeedbacks");
	}

	@GetMapping("/admin/deliveredOrder/{orderId}")
	public RedirectView deliveredOrder(@PathVariable("orderId") int id,RedirectAttributes attributes) {
		System.out.println(id);
		OrderEntity order = this.orderService.getbyOrderId(id);
		System.out.println(order);
		order.setStatus("Delivered");
		this.orderRepository.save(order);
		String message="Order Delivered.\nUsername:"+order.getCanteenUsers().getEmail()+"\nfood name: "+order.getFood().getName()+"\nTotal Price:Rs"+order.getTotalPrice()+"\nOrder Date:"+order.getOrderDate();
		emailSenderService.sendEmail(order.getCanteenUsers().getEmail(), "Message from Canteen Management", message);
		attributes.addAttribute("delivered",1);
		return new RedirectView("/admin/viewUpcomingOrders");
	}

	@GetMapping("/admin/previousordersdownload")
	public String previousOrdersDownload(HttpServletResponse response,HttpServletRequest request, @ModelAttribute("date") String date,
			@ModelAttribute("id") String id, Model m) throws DocumentException, IOException {
		
		String buttonClicked = request.getParameter("pdf");
		if(buttonClicked!=null)//if pdf is clicked this will run
		{
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
		}
		else//this is the code for excel part
		{
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
			PreviousOrdersExcelGenerator excelGenerator=new PreviousOrdersExcelGenerator(); 
			
            excelGenerator.generate(ordersList, response);
		}
		
		
		return null;
	}

	@GetMapping("/admin/upcomingordersdownload")
	public String upcomingOrdersDownload(HttpServletResponse response, HttpServletRequest request,
	                                     @ModelAttribute("date") String date, @ModelAttribute("id") String id, Model m)
	        throws DocumentException, IOException {
	    String buttonClicked = request.getParameter("pdf"); // get value of pdf button if clicked
	    if (buttonClicked != null) {
	        response.setContentType("application/pdf");
	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	        String currentDateTime = dateFormat.format(new Date());
	        String headerkey = "Content-Disposition";
	        String headervalue = "attachment; filename=UpcomingOrders_" + currentDateTime + ".pdf";
	        response.setHeader(headerkey, headervalue);
	        System.out.println(date);
	        System.out.println(id);
	        List<OrderEntity> ordersList = null;
	        if (date.equals("null") && id.equals("null")) {
	            ordersList = orderService.getAllOrders("Booked");
	        } else if (date.equals("null")) {
	            System.out.println("Date is null and user id is not");
	            ordersList = this.orderService.getAllOrdersByUserId("Booked", id);
	            m.addAttribute("orders", ordersList);
	        } else if (id.equals("null")) {
	            System.out.println("Date is not null and User id is null");
	            LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
	            ordersList = this.orderService.getAllOrdersByStatusAndDate("Booked", date1);
	            m.addAttribute("orders", ordersList);
	        } else {
	            LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
	            ordersList = this.orderService.getAllOrdersByDateAndUserId(date1, id, "Booked");
	            m.addAttribute("orders", ordersList);
	        }

	        UpcomingOrdersPDFGenerator generator = new UpcomingOrdersPDFGenerator();
	        generator.generate(ordersList, response);

	    } else {
	        // Excel button was clicked, handle Excel download
	        List<OrderEntity> ordersList = null;
	        if (date.equals("null") && id.equals("null")) {
	            ordersList = orderService.getAllOrders("Booked");
	        } else if (date.equals("null")) {
	            System.out.println("Date is null and user id is not");
	            ordersList = this.orderService.getAllOrdersByUserId("Booked", id);
	            m.addAttribute("orders", ordersList);
	        } else if (id.equals("null")) {
	            System.out.println("Date is not null and User id is null");
	            LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
	            ordersList = this.orderService.getAllOrdersByStatusAndDate("Booked", date1);
	            m.addAttribute("orders", ordersList);
	        } else {
	            LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
	            ordersList = this.orderService.getAllOrdersByDateAndUserId(date1, id, "Booked");
	            m.addAttribute("orders", ordersList);
	        }
	        
	        UpcomingOrdersExcelGenerator generator = new UpcomingOrdersExcelGenerator();
	        generator.generate(ordersList, response);
	    }
	    return null;
	}

	

	@GetMapping("/admin/analytics")
	public String analytics(Model model) {

		// The whole Order List
		List<OrderEntity> totalOrders = (List<OrderEntity>) this.orderRepository.findAll();
		List<OrderEntity> ordersDelivered = totalOrders.stream().filter(order->order.getStatus().equals("Delivered")).collect(Collectors.toList());
		// Total Orders / Veg Orders / Non-Veg Orders Count
		List<OrderEntity> vegFood = ordersDelivered.stream().filter(order -> order.getFood().getType().equals("Veg"))
				.collect(Collectors.toList());
		List<OrderEntity> nonVegFood = ordersDelivered.stream().filter(order -> order.getFood().getType().equals("Nonveg"))
				.collect(Collectors.toList());
		int totalOrdersCount = totalOrders.size();
		int noOfVegOrders = vegFood.size();
		int noOfNonVegOrders = nonVegFood.size();

		model.addAttribute("totalOrdersCount", totalOrdersCount);
		model.addAttribute("vegNum", noOfVegOrders);
		model.addAttribute("nonVegNum", noOfNonVegOrders);

		// Upcoming Orders Count
		List<OrderEntity> upcomingOrders = totalOrders.stream().filter(order -> order.getStatus().equals("Booked"))
				.collect(Collectors.toList());
		int upcomingOrdersCount = upcomingOrders.size();

		model.addAttribute("upcomingOrdersCount", upcomingOrdersCount);

		// Total Numbers of Active Users

		List<CanteenUsers> users = this.canteenUserRepository.findAll();
		int totalUsers = users.size();

		model.addAttribute("totalUsers", totalUsers);

		// Totals number of food Items Present

		List<menuCanteen> items = this.menuRepository.findAll();

		Date currentDate = new Date();
		@SuppressWarnings("deprecation")
		int currentMonth = currentDate.getMonth();
		@SuppressWarnings("deprecation")
		List<menuCanteen> itemsThisMonth = items.stream()
				.filter(order -> order.getFoodServedDate().getMonth() == currentMonth).collect(Collectors.toList());
		System.out.println(itemsThisMonth);
		int currentMonthItems = itemsThisMonth.size();

		model.addAttribute("currentMonthItems", currentMonthItems);

		// Food Item Bar (Sell)

		TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
		//Orders that are already delivered
		
		
		for (int i = 0; i < ordersDelivered.size(); i++) {
			OrderEntity order = ordersDelivered.get(i);
			int new_key = order.getFood().getID();

			if (map.containsKey(new_key)) {
				map.put(new_key, map.get(new_key) + 1);
			} else {
				map.put(new_key, 1);
			}
		}



		TreeMap<String, Integer> resultMap = new TreeMap<>();

		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			int key = entry.getKey();
			menuCanteen singleOrder = this.menuRepository.findById(key);
			resultMap.put(singleOrder.getName(), map.get(key));
		}

		// Extracting Item names and orderscount
		
		List<String> itemNames = new ArrayList<>();
		List<Integer> ordersCount = new ArrayList<>();
		
		for (Entry<String, Integer> entry : resultMap.entrySet())
		{
			itemNames.add(entry.getKey());
			ordersCount.add(entry.getValue());
			
		}
		
		model.addAttribute("barNames", itemNames);
		model.addAttribute("barHeight", ordersCount);
		
		// Passsing the month details i.e. how many orders delievered for a single month
		// orders Delivered 
		List<Integer> monthsData = new ArrayList<>();
		for(int i = 0; i<=11;i++) {
			monthsData.add(0);
		}
		
		
		for(int i = 0 ; i < ordersDelivered.size(); i++) {
			OrderEntity currOrder = ordersDelivered.get(i);
			Date currOrderDate = currOrder.getOrderDate();
			@SuppressWarnings("deprecation")
			int currOrderMonth = currOrderDate.getMonth();
			monthsData.set(currOrderMonth, monthsData.get(currOrderMonth)+1);
		}
		
		model.addAttribute("sellPerMonth", monthsData);

		return "/admin/analytics";
	}

}