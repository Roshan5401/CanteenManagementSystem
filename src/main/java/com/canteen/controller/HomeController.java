package com.canteen.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.canteen.entities.CanteenUsers;
import com.canteen.entities.GlobalEmployees;
import com.canteen.repository.CanteenUserRepository;
import com.canteen.repository.GlobalRepository;
import com.project.helper.Message;

import jakarta.servlet.http.HttpSession;



@Controller
public class HomeController {
	
	@Autowired
	CanteenUserRepository canteenUserRepository;
	
	@Autowired
	GlobalRepository globalRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("canteenUser", new CanteenUsers());
		return "signup";
	}
	@PostMapping("/do_register")
	public String doRegister(@ModelAttribute("canteenUser") CanteenUsers canteenUsers,HttpSession httpSession,Model model){
		try {
			GlobalEmployees emp=globalRepository.findByEmail(canteenUsers.getEmail());
			if(emp==null)
			{
				System.out.println("You are not our employee, Contact Hr to solve your issue");
				httpSession.setAttribute("message",new Message("You are not our employee, Contact Hr to solve your issue", "alert-error"));
				return "loginFailed";
			}
			else {
				canteenUsers.setPassword(bCryptPasswordEncoder.encode(canteenUsers.getPassword()));
				canteenUsers.setRole("ROLE_USER");
				canteenUsers.setWallet(0.00);
				canteenUserRepository.save(canteenUsers);
				model.addAttribute("canteenUsers", canteenUsers);
				httpSession.setAttribute("message",new Message("Successfully Registered!!", "alert-success"));
				String message="You Have Successfully Registerd for Canteen Services.\nUsername:"+canteenUsers.getEmail()+"\nWallet Balance: Rs0.00";
				emailSenderService.sendEmail(canteenUsers.getEmail(), "Message from Canteen Management", message);
				return "signup";
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("canteenUsers", canteenUsers);
			httpSession.setAttribute("message", new Message("Something went wrong "+e.getMessage(),"alert-error"));
			return "signup";
		}
		
	}
	@GetMapping("/signin")
	public String login(Model model) {
		return "signin";
	}
	@GetMapping("/")
	public RedirectView signin(Model model,Principal principal)
	{
		String userName=principal.getName();
		CanteenUsers users=canteenUserRepository.findByEmail(userName);
		System.out.println(users.getRole());
		if((users.getRole()).equals("ROLE_USER"))
		{
			System.out.println("1");
			return new RedirectView("/user/bookOrder");
		}
		model.addAttribute("userName", userName);
		System.out.println("login Successful");
		return new RedirectView("/admin/addAndUpdateMenu");
	}
	@GetMapping("/signin/error")
	public String failureLogin()
	{
		return "loginFailed";
	}
}
