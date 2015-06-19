package com.ndobriukha.onlinemarketplace.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthFailureHandler implements AuthenticationFailureHandler {

	
	@Override
	public void onAuthenticationFailure(HttpServletRequest req,
			HttpServletResponse res, AuthenticationException e)
			throws IOException, ServletException {
		ObjectMapper mapper = new ObjectMapper();		
		Map<String, String> data = new HashMap<String, String>();
		switch (e.getCause().getClass().getSimpleName()) {
		case "BadCredentialsException":
			data.put("status", "FAIL");
			data.put("field", e.getMessage());
			data.put("errorMsg", e.getCause().getMessage());
			break;
		default:
			data.put("status", "EXCEPTION");
			data.put("errorMsg", e.getMessage());
			break;
		}		
		res.getWriter().println( mapper.writeValueAsString(data) );
		res.setContentType(MediaType.APPLICATION_JSON_VALUE);
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
