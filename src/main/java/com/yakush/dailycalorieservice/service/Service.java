package com.yakush.dailycalorieservice.service;


import java.util.List;

public interface Service<T> {
    T save(T t);

    T update(T t);

    void delete(T t);

    List<T> getAll();

    T getById(Long id);
}
