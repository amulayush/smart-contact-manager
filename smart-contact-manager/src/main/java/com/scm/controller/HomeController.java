package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scm.dao.UserRepository;
import com.scm.entities.User;

@Controller
public class HomeController 
{
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/home")
	public String home()
	{
		return "home";
		
	}


}
