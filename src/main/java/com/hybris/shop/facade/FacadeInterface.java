package com.hybris.shop.facade;

public interface FacadeInterface <T, U, V> {
    T save(U object);

    T findById(V id);

    T update(V id, U newDataObject);

    void deleteById(V id);
}
