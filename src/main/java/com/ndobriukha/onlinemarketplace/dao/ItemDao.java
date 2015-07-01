package com.ndobriukha.onlinemarketplace.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.ndobriukha.onlinemarketplace.domain.Bid;

public interface ItemDao<T, PK extends Serializable> extends GenericDao<T, PK> {
	
	@Transactional
	Bid getBestBidByItem(T item);
	
	@Transactional
	List<Bid> getAllBidsByItem(T item);
	
	@Transactional
	List<T> getByParameters(Map<String, String[]> map);
}
