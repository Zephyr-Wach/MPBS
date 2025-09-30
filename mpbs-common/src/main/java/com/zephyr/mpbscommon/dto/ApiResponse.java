package com.zephyr.mpbscommon.dto;

public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 成功返回（有数据）
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    // 成功返回（无数据）
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "success", null);
    }

    // 失败返回（带错误信息）
    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(500, message, null);
    }

    // 失败返回（自定义错误码和信息）
    public static <T> ApiResponse<T> failure(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
