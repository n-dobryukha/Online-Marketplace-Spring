package com.ndobriukha.onlinemarketplace.dao;

import java.util.List;

public interface GenericDao<T> {
	/** Create or update dao-object*/
    public void save(T object);

    /** Returns object by id */
    public T get(Long id);

    /** Returns all objects */
    public List<T> getAll();
    
    /** Delete object */
    public void delete(T object);
    
}
