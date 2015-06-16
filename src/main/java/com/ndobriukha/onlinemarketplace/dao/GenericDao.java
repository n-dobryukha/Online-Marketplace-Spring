package com.ndobriukha.onlinemarketplace.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, PK extends Serializable> {
	/** Create object */
    PK create(T object);

    /** Returns object by id */
    T get(PK id);

    /** Returns all objects */
    List<T> get();
    
    /** Update object */
    void update(T object);
    
    /** Delete object */
    void delete(T object);    
}
