package com.scm.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.dao.UserRepository;
import com.scm.entities.User;

@Controller
@RequestMapping("/user")
public class UserController 
{
	@Autowired
	private UserRepository userReposiotry;
	
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
		String userName = principal.getName();
		
		
		User user = userReposiotry.getUserByUserName(userName);
		
		
		model.addAttribute("user",user);
		
		return "normal/user-dashboard";
	}

	
}
