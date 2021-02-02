package com.hybris.shop.service;

import org.springframework.util.ReflectionUtils;

import java.util.Arrays;

public interface ServiceInterface<T, U> {
    T save(T object);

    T findById(U id);

    default T update(U id, T newDataObject) {
        T objectToUpdate = findById(id);

        Class<?> objectClass = newDataObject.getClass();

        Arrays.stream(objectClass.getDeclaredFields())
                .forEach(field -> {
                    try {
                        field.setAccessible(true);

                        Object fieldValue = field.get(newDataObject);

                        if (fieldValue != null) {
                            ReflectionUtils.setField(field, objectToUpdate, fieldValue);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        return save(objectToUpdate);
    }

    void deleteById(U id);
}
