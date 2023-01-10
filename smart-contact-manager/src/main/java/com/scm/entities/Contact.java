package com.scm.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="contact")
public class Contact 
{
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private int cId;
	
	private String name;
	
	private String nickName;
	
	@Column(length=5000)
	private String description;
	
	private String phone;
	
	private String work;
	
	private String email;
	
	private String imageUrl;	

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private User user;

	public int getcId() {
		return cId;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Contact(int cId, String name, String nickName, String description, String phone, String work, String email,
			String imageUrl, User user) {
		super();
		this.cId = cId;
		this.name = name;
		this.nickName = nickName;
		this.description = description;
		this.phone = phone;
		this.work = work;
		this.email = email;
		this.imageUrl = imageUrl;
		this.user = user;
	}
	
	public Contact() {}

	@Override
	public String toString() {
		return "Contact [cId=" + cId + ", name=" + name + ", nickName=" + nickName + ", description=" + description
				+ ", phone=" + phone + ", work=" + work + ", email=" + email + ", imageUrl=" + imageUrl + ", user="
				+ user + "]";
	}
	
	

}
