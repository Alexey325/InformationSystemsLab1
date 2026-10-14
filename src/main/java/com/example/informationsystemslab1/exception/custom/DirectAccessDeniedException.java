package com.example.informationsystemslab1.exception.custom;

public class DirectAccessDeniedException extends RuntimeException {

    public DirectAccessDeniedException() {
        super("Прямой доступ к эндпоинту запрещен");
    }
}
