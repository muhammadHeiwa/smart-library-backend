package com.smartlibrary.smart_library_api.exception;

import com.smartlibrary.smart_library_api.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<ArrayList<Object>> handleValidationError(MethodArgumentNotValidException ex) {
        String field = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getField();

        return ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                new ArrayList<>(),
                field + " empty!"
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<ArrayList<Object>> handleRuntimeException(RuntimeException ex) {
        return ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                new ArrayList<>(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<ArrayList<Object>> handleGeneralException(Exception ex) {
        return ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ArrayList<>(),
                "Internal server error"
        );
    }
}