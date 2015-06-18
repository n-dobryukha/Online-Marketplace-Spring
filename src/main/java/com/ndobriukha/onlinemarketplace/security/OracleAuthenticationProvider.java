package com.ndobriukha.onlinemarketplace.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ndobriukha.onlinemarketplace.dao.UserDao;
import com.ndobriukha.onlinemarketplace.domain.User;
import com.ndobriukha.onlinemarketplace.util.PasswordHash;

public class OracleAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDao<User, Long> userDao;
	
	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		String login = auth.getName();
		String password = auth.getCredentials().toString();
		System.out.println(login);
		System.out.println(password);
		User user = userDao.getUserByLogin(login);
		System.out.println(user);
		if (user != null) {
			try {
				if (PasswordHash.validatePassword(password, user.getPassword())) {
					List<GrantedAuthority> grantedAuths = new ArrayList<>();
		            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		            return new UsernamePasswordAuthenticationToken(login, password, grantedAuths);
				}
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace(System.err);
			}
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}

}
