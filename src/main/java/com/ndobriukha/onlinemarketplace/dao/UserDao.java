package com.ndobriukha.onlinemarketplace.dao;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

import com.ndobriukha.onlinemarketplace.domain.User;

public interface UserDao<T, PK extends Serializable> extends GenericDao<T, PK> {
	
	@Transactional
	User getUserByLogin(String login);
}
