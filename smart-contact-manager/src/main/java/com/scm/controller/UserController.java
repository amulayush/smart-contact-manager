package com.scm.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.dao.UserRepository;
import com.scm.entities.Contact;
import com.scm.entities.User;

@Controller
@RequestMapping("/user")
public class UserController 
{
	@Autowired
	private UserRepository userReposiotry;
	
	
	//runs for every method, i.e., every model passed in this controller
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal)
	{
		String userName = principal.getName();
		
		User user = userReposiotry.getUserByUserName(userName);
		
		model.addAttribute("user",user);
	}
	
	
	//handles user dashboard
	
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
		
		model.addAttribute("title", "User Dashboard");
		
		return "normal/user-dashboard";
	}
	
	
	//handles add form
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) 
	{
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add-contact-form";
		
	}

	
}











