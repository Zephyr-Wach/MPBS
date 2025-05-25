package com.zephyr.mpbscommon.utils;

/**
 * 统一响应结果封装类，用于接口返回的标准格式。
 *
 * @param <T> 返回的数据类型
 */
public class Result<T> {

    /**
     * 状态码，通常0表示成功，非0表示失败
     */
    private int code;

    /**
     * 返回的信息描述
     */
    private String message;

    /**
     * 返回的数据主体
     */
    private T data;

    public Result() {}

    /**
     * 带参构造器
     *
     * @param code    状态码
     * @param message 返回信息
     * @param data    返回数据
     */
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功且带数据的响应结果
     *
     * @param data 返回数据
     * @param <T>  数据类型
     * @return Result对象，code=0，message="success"
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "success", data);
    }

    /**
     * 成功但无数据的响应结果
     *
     * @param <T> 数据类型
     * @return Result对象，code=0，message="success"，data=null
     */
    public static <T> Result<T> success() {
        return new Result<>(0, "success", null);
    }

    /**
     * 失败的响应结果
     *
     * @param code    错误状态码
     * @param message 错误描述信息
     * @param <T>     数据类型
     * @return Result对象，data为null
     */
    public static <T> Result<T> failure(int code, String message) {
        return new Result<>(code, message, null);
    }

    // getter/setter

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
}
