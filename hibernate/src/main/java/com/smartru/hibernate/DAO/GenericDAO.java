package com.smartru.hibernate.DAO;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {

    public Optional<T> findByID(ID id);
    public List<T> findAll();
    public Long getCount();
    public T save(T entity);
    public T update(T entity);
}
