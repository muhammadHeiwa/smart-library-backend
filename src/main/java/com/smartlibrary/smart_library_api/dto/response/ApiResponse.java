package com.smartlibrary.smart_library_api.dto.response;

public class ApiResponse<T> {

    private int status;
    private T data;
    private String error;

    public ApiResponse(int status, T data, String error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(int status, T data) {
        return new ApiResponse<>(status, data, null);
    }

    public static <T> ApiResponse<T> error(int status, T data, String error) {
        return new ApiResponse<>(status, data, error);
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}