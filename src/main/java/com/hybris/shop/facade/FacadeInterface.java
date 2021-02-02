package com.hybris.shop.facade;

public interface FacadeInterface <T, U, V> {
    T save(U object);

    T findById(V id);

    T update(V id, U newDataObject);

    boolean existsById(V id);

    void deleteById(V id);
}
