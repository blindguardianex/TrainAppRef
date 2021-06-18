package com.smartru.hibernate.DAO;

import java.util.Collection;
import java.util.List;

public interface GenericDAO<T, ID> {

    public T findByID(ID id);
    public List<T> findAll();
    public Long getCount();
    public T save(T entity);
    public T update(T entity);
}
