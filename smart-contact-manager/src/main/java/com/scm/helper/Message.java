package com.scm.helper;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Message 
{
	private String content;
	
	private String type;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Message(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}

	public Message() {
		super();
	}

	@Override
	public String toString() {
		return "Message [content=" + content + ", type=" + type + "]";
	}
	
	public void removeVerificationMessageFromSession() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            session.removeAttribute("verificationMessage");
        } catch (RuntimeException ex) {
            ex.printStackTrace();        }
    }

}
