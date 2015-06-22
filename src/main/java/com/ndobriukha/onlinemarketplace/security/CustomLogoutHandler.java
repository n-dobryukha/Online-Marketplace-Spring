package com.ndobriukha.onlinemarketplace.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class CustomLogoutHandler implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest req, HttpServletResponse res,
			Authentication auth) {
		
	}

}
