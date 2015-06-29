package com.ndobriukha.onlinemarketplace.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ndobriukha.onlinemarketplace.dao.UserDao;
import com.ndobriukha.onlinemarketplace.domain.User;

@RestController
@RequestMapping("/rest/user")
public class RestUserController {

	@Autowired
	private UserDao<User, Long> userDao;
	
	private Map<String, String> buildConstraintViolationsMap(ConstraintViolationException e) {
		Map<String, String> constraintViolations = new HashMap<String,String>();
		for (ConstraintViolation<?> entity: e.getConstraintViolations()) {
			constraintViolations.put(entity.getPropertyPath().toString(), entity.getMessage());
		}
		return constraintViolations;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void test(HttpServletRequest req, HttpServletResponse res) {
		res.setStatus(HttpServletResponse.SC_CREATED);
		res.addHeader("Location", String.format("%s/rest/items/all", req.getContextPath()));
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> registration(@RequestBody User user, HttpServletRequest req, HttpServletResponse res) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			user.setHashedPassword(user.getPassword());
			Long id = userDao.create(user);
			res.setStatus(HttpServletResponse.SC_CREATED);
			res.addHeader("Location", String.format("%s/rest/user/%s", req.getContextPath(), id));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace(System.err);
		} catch (ConstraintViolationException e) {
			result.put("fieldErrors", buildConstraintViolationsMap(e));
			result.put("status","WRONGPARAM");
			return result;
		} catch (DataIntegrityViolationException e) {
			org.hibernate.exception.ConstraintViolationException cause = (org.hibernate.exception.ConstraintViolationException) e.getCause();
			if ("MARKETPLACE.USERS_LOGIN_IDX".equals(cause.getConstraintName())) {
				res.setStatus(HttpServletResponse.SC_CONFLICT);
			}
		}
		return null;
    }
}
