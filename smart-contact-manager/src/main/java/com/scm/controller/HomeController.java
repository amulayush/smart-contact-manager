package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.dao.UserRepository;
import com.scm.entities.User;
import com.scm.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController 
{
	@Autowired
	private UserRepository userRepository;
	
	
	
	//This handles the home page
	
	@GetMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title", "Home - SCM");
		return "home";
		
	}
	
	
	//This handles the about page
	
	@GetMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title", "About - SCM");
		return "about";
		
	}
	
	
	//This handles the signup page

	
	@GetMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("user", new User());

		model.addAttribute("title", "Register - SCM");
		return "signup";
		
	}
	
	
	// handler for registering user
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, 
								@RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
								HttpSession session,
								BindingResult bdnResult,
								Model model)
	{
		try {
			if(!agreement)
			{
				System.out.println("You have not agreedd to T&C");
				throw new Exception("You have not agreedd to T&C");
			}
			
			if(bdnResult.hasErrors()) 
			{
				System.out.println("Error : " + bdnResult.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			
			System.out.println("Agreement : " + agreement);
			System.out.println("User : " + user);
			
			this.userRepository.save(user);

			
			/* not using the alert messages, we have yet to configure that
			
			session.setAttribute("message", new Message("Be happy while you can!", "alert-success"));
			
			*/

			model.addAttribute("user", new User());
			return "signup";
		}
		
		catch (Exception e){
			e.printStackTrace();
			model.addAttribute("user", user);
			
			
			/* not using the alert messages, we have yet to configure that
			 * 
			 * session.setAttribute("message", new Message("DAMN YOU FOOL!!!" + e.getMessage(),
															"alert-danger")); 
															
															*/


			
			return "signup";
			
		}
		
		
	}


}
