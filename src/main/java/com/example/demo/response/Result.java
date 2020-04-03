package com.example.demo.response;

import java.io.Serializable;

/**
 * @version V1.0.0
 * @ClassName: {@link Result}
 * @Description: 后台返回统一DTO类
 * @author: 兰州
 * @date: 2019/7/16 9:22
 * @Copyright: @2019 All rights reserved.
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -4539213916903487309L;

    private Integer code;

    private String message;

    private T data;

    private Result(Integer code) {
        this.code = code;
    }

    private Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * success response
     *
     * @return Result
     */
    public static Result success() {
        return new Result(ServerResponseEnum.SUCCESS.getCode());
    }

    public static Result success(String message) {
        return new Result(ServerResponseEnum.SUCCESS.getCode(), message);
    }

    public static <T> Result success(T data) {
        return new Result<>(ServerResponseEnum.SUCCESS.getCode(), data);
    }

    /**
     * fail response
     *
     * @return Result
     */
    public static Result fail() {
        return new Result(ServerResponseEnum.FAIL.getCode());
    }

    public static Result fail(String message) {
        return new Result(ServerResponseEnum.FAIL.getCode(), message);
    }

    public static <T> Result fail(T data) {
        return new Result<>(ServerResponseEnum.FAIL.getCode(), data);
    }

    /**
     * not found response
     *
     * @return Result
     */
    public static Result notFound() {
        return new Result(ServerResponseEnum.NOT_FOUND.getCode());
    }

    public static Result notFound(String message) {
        return new Result(ServerResponseEnum.NOT_FOUND.getCode(), message);
    }

    public static <T> Result notFound(T data) {
        return new Result<>(ServerResponseEnum.NOT_FOUND.getCode(), data);
    }

    /**
     * error response
     *
     * @return Result
     */
    public static Result error() {
        return new Result(ServerResponseEnum.ERROR.getCode());
    }

    public static Result error(String message) {
        return new Result(ServerResponseEnum.ERROR.getCode(), message);
    }

    public static <T> Result error(T data) {
        return new Result<>(ServerResponseEnum.ERROR.getCode(), data);
    }

    /**
     * unauthorized response
     *
     * @return ss
     */
    public static Result unauthorized() {
        return new Result(ServerResponseEnum.UNAUTHORIZED.getCode());
    }

    public static Result unauthorized(String message) {
        return new Result(ServerResponseEnum.UNAUTHORIZED.getCode(), message);
    }

    enum ServerResponseEnum {
        /**
         * 状态枚举
         */
        SUCCESS(200, "成功"),
        FAIL(201, "失败"),
        NOT_FOUND(404, "未找到"),
        ERROR(500, "系统错误"),
        UNAUTHORIZED(403, "未授权");

        private int code;
        private String message;

        ServerResponseEnum(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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
