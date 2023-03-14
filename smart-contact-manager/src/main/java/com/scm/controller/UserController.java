package com.scm.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scm.dao.ContactRepository;
import com.scm.dao.UserRepository;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userReposiotry;

	@Autowired
	private ContactRepository contactRepository;

	// runs for every method, i.e., every model passed in this controller

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();

		User user = userReposiotry.getUserByUserName(userName);

		model.addAttribute("user", user);
	}

	// handles user dashboard

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		model.addAttribute("title", "User Dashboard");

		return "normal/user-dashboard";
	}

	// handles add form

	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add-contact-form";

	}

	// processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, Principal principal,
			@RequestParam("image") MultipartFile file, HttpSession session) {
		try {
			String name = principal.getName();

			User user = this.userReposiotry.getUserByUserName(name);
			contact.setUser(user);

			if (file.isEmpty()) {
				contact.setImageUrl("contact.png");
				System.out.println("There is no image");
			} else {
				// add some context to the files' name, then upload it to img folder in
				// resources.

				contact.setImageUrl(file.getOriginalFilename());

				File savedFile = new ClassPathResource("/static/img").getFile();

				Path path = Paths.get(savedFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Image has been uploaded");
			}

			user.getContacts().add(contact);
			this.userReposiotry.save(user);

			System.out.println("Added it");
			System.out.println("Data : " + contact);

			session.setAttribute("message", new Message("Yooo we added it!", "success"));

		} catch (Exception e) {
			session.setAttribute("message", new Message("Shit hit the fan.", "danger"));

			System.out.println("Error : " + e.getMessage());
		}

		return "redirect:/user/show-contacts/0";
	}

	// show contacts handler
	// show n=5 contacts per page
	// page = current page
	@GetMapping("/show-contacts/{page}")
	public String showContacts(Model model, Principal principal, @PathVariable("page") Integer page) {
		String userName = principal.getName();

		User user = this.userReposiotry.getUserByUserName(userName);

		int userId = user.getId();

		Pageable pageReq = PageRequest.of(page, 5);

		Page<Contact> contacts = this.contactRepository.findContactsByUser(userId, pageReq);

		model.addAttribute("currentPage", page);
		model.addAttribute("contacts", contacts);
		model.addAttribute("title", "Show Contacts");
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show-contacts";
	}

	// show particular contacts' details

	@GetMapping("/contact/{cId}")
	public String showContactDetails(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);

		Contact contact = contactOptional.get();

		String userName = principal.getName();

		User user = this.userReposiotry.getUserByUserName(userName);

		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}

		System.out.println("Cid : " + cId);
		return "normal/contact-detail";
	}

	// delete contact handler

	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Principal principal, HttpSession session) {
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		String userName = principal.getName();

		User user = this.userReposiotry.getUserByUserName(userName);

		if ((user.getId() == contact.getUser().getId()) && (contact != null)) {
			contact.setUser(null);
			this.contactRepository.delete(contact);
			System.out.println("Contact deleted");
		}

		session.setAttribute("message", new Message("Contact deleted successfully", "success"));

		return "redirect:/user/show-contacts/0";
	}

	// update form handler
	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId") Integer cId, Model model) {
		model.addAttribute("title", "Update Contact");

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		model.addAttribute("contact", contact);

		return "normal/update-form";
	}

	// update process handler
	@PostMapping("/update-contact-process")
	public String updateHandler(@ModelAttribute Contact contact,
					@RequestParam("image") MultipartFile file,
			Model model, Principal principal, HttpSession session) {
		System.out.println("Updating contact: " + contact.getName());

		Contact oldContact = this.contactRepository.findById(contact.getcId()).get();

		try {

			// if new image has been selected
			if (!file.isEmpty()) {
				
				// delete old photo
				
				String defaultImageUrl = "contact.png";
				
				if (!defaultImageUrl.equals(oldContact.getImageUrl())) {
					System.out.println(oldContact.getImageUrl());
					File deleteFile = new ClassPathResource("/static/img").getFile();
					File doneFile = new File(deleteFile, oldContact.getImageUrl());
					doneFile.delete();
					System.out.println("Deleting file");
				}
				
				

				// update new photo

				File savedFile = new ClassPathResource("/static/img").getFile();

				Path path = Paths.get(savedFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImageUrl(file.getOriginalFilename());

			} else {
				contact.setImageUrl(oldContact.getImageUrl());
			}

			String userName = principal.getName();
			User user = this.userReposiotry.getUserByUserName(userName);
			contact.setUser(user);

			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Your countact has been updated", "success"));
			System.out.println(oldContact.getImageUrl());
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		return "redirect:/user/contact/" + contact.getcId();
	}

}
