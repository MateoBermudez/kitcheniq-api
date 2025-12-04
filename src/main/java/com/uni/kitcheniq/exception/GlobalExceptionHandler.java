package com.uni.kitcheniq.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage("An unexpected error occurred");
        errorMessage.setCause(ex.getCause() != null ? ex.getCause().toString() : null);
        errorMessage.setStatusCode(500);
        errorMessage.setTimestamp(System.currentTimeMillis());
        errorMessage.setPath(request.getRequestURI());
        errorMessage.setErrorCode("INTERNAL_SERVER_ERROR");

        return ResponseEntity.status(500).body(errorMessage);
    }

    @ExceptionHandler(InsufficientInventory.class)
    public ResponseEntity<ErrorMessage> handleInsufficientInventoryException(InsufficientInventory ex, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setCause(ex.getCause() != null ? ex.getCause().toString() : null);
        errorMessage.setStatusCode(400);
        errorMessage.setTimestamp(System.currentTimeMillis());
        errorMessage.setPath(request.getRequestURI());
        errorMessage.setErrorCode("INSUFFICIENT_INVENTORY");

        return ResponseEntity.status(400).body(errorMessage);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleInvalidCredentialsException(InvalidCredentialsException ex, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setCause(ex.getCause() != null ? ex.getCause().toString() : null);
        errorMessage.setStatusCode(401);
        errorMessage.setTimestamp(System.currentTimeMillis());
        errorMessage.setPath(request.getRequestURI());
        errorMessage.setErrorCode(ex.getMessage());

        return ResponseEntity.status(401).body(errorMessage);
    }

    @ExceptionHandler(NoItemFoundException.class)
    public ResponseEntity<ErrorMessage> handleNoItemFoundException(NoItemFoundException ex, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setCause(ex.getCause() != null ? ex.getCause().toString() : null);
        errorMessage.setStatusCode(404);
        errorMessage.setTimestamp(System.currentTimeMillis());
        errorMessage.setPath(request.getRequestURI());
        errorMessage.setErrorCode("NO_ITEM_FOUND");

        return ResponseEntity.status(404).body(errorMessage);
    }

    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleSupplierNotFoundException(SupplierNotFoundException ex, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setCause(ex.getCause() != null ? ex.getCause().toString() : null);
        errorMessage.setStatusCode(404);
        errorMessage.setTimestamp(System.currentTimeMillis());
        errorMessage.setPath(request.getRequestURI());
        errorMessage.setErrorCode("SUPPLIER_NOT_FOUND");

        return ResponseEntity.status(404).body(errorMessage);
    }

    @ExceptionHandler(NotEmployees.class)
    public ResponseEntity<ErrorMessage> handleNotEmployeesException(NotEmployees ex, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setCause(ex.getCause() != null ? ex.getCause().toString() : null);
        errorMessage.setStatusCode(403);
        errorMessage.setTimestamp(System.currentTimeMillis());
        errorMessage.setPath(request.getRequestURI());
        errorMessage.setErrorCode("NOT_EMPLOYEES");
        return ResponseEntity.status(403).body(errorMessage);
    }
}
