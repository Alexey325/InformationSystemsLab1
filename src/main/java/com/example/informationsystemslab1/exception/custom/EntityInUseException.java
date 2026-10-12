package com.example.informationsystemslab1.exception.custom;

public class EntityInUseException extends RuntimeException {

    public EntityInUseException(Class<?> entityType, Object id) {
        super(entityType.getSimpleName() + " with id " + id + " is still referenced by other records and cannot be deleted");
    }
}
