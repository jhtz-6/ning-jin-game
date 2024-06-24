package com.myf.hleper.model.res;

import com.myf.common.constant.BoardConstant;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-12-13  20:33
 * @Description: TODO
 */
@Data
public class Result<T> {
    private boolean success;
    private int errorCode;
    private String errorMsg;
    private T data;

    public static <T> Result<T> success(T data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }


    public static <T> Result<T> failure(String errorMsg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setErrorCode(400);
        result.setErrorMsg(errorMsg);
        return result;
    }

}

