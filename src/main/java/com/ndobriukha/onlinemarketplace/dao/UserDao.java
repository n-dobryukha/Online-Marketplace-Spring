package com.ndobriukha.onlinemarketplace.dao;

import java.io.Serializable;

import com.ndobriukha.onlinemarketplace.domain.User;

public interface UserDao<T, PK extends Serializable> extends GenericDao<T, PK> {
	public User getUserByLogin(String login);
}
