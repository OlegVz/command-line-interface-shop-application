package com.hybris.shop.service;

import org.springframework.util.ReflectionUtils;

import java.util.Arrays;

public interface ServiceInterface<T> {
    T save(T object);

    T findById(Long id);

    default T update(Long id, T newDataObject) {
        T objectToUpdate = findById(id);

        Class<?> userClass = newDataObject.getClass();

        Arrays.stream(userClass.getDeclaredFields())
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

    void deleteById(Long id);
}
