package com.scm.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scm.dao.UserRepository;
import com.scm.entities.Contact;
import com.scm.entities.User;

import ch.qos.logback.core.net.SyslogOutputStream;

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
	
	
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
									Principal principal,
									@RequestParam("image") MultipartFile file)                        
	{
		try 
		{
			String name = principal.getName();
			
			User user = this.userReposiotry.getUserByUserName(name);
			contact.setUser(user);
			
			if(file.isEmpty())
			{
				System.out.println("There is no image");
			}
			else
			{
				//add some context to the files' name, then upload it to img folder in resources.
				
				contact.setImageUrl(user.getId() + file.getOriginalFilename());
				
				File savedFile = new ClassPathResource("/static/img").getFile();
				
				Path path = Paths.get(savedFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				System.out.println("Image has been uploaded");
			}
			
			user.getContacts().add(contact);
			this.userReposiotry.save(user);
			
			System.out.println("Added it");
			System.out.println("Data : " + contact);
			
		} catch (Exception e) 
		{
			System.out.println("Error : " + e.getMessage());
		}
		
		return "normal/add-contact-form";
	}

	
}











