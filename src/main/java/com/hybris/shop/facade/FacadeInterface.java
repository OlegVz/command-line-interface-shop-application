package com.hybris.shop.facade;

import java.util.List;

public interface FacadeInterface <T, U, V> {
    T save(U object);

    T findById(V id);

    T update(V id, U newDataObject);

    boolean existsById(V id);

    void deleteById(V id);

    List<T> findAll();
}
