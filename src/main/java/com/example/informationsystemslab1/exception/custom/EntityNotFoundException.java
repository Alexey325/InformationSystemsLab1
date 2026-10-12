package com.example.informationsystemslab1.exception.custom;

import java.util.Collection;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> entityType) {
        super("No " + entityType.getSimpleName() + " found");
    }

    public EntityNotFoundException(Class<?> entityType, Object id) {
        super(entityType.getSimpleName() + " with id " + id + " not found");
    }

    public EntityNotFoundException(Class<?> entityType, Collection<?> missingIds) {
        super(entityType.getSimpleName() + "(s) with ids " + missingIds + " not found");
    }
}
