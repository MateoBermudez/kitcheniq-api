package com.uni.kitcheniq.exception;

public class InsufficientInventory extends RuntimeException {
    public InsufficientInventory(String message) {
        super(message);
    }
}
