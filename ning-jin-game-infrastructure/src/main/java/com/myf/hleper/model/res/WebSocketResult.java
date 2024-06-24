package com.myf.hleper.model.res;

import com.myf.common.enmus.WebSocketMessageType;
import lombok.Data;
import lombok.ToString;

/**
 * @author myf
 */
@Data
@ToString(callSuper = true)
public class WebSocketResult<T> extends Result{

    private WebSocketMessageType type;


    public static <T> WebSocketResult<T> success(T data,WebSocketMessageType type) {
        WebSocketResult result = new WebSocketResult();
        result.setType(type);
        result.setSuccess(true);
        result.setData(data);
        return result;
    }
    public static <T> WebSocketResult<T> failure(String message,WebSocketMessageType type) {
        WebSocketResult result = new WebSocketResult();
        result.setType(type);
        result.setSuccess(false);
        result.setErrorCode(400);
        result.setErrorMsg(message);
        return result;
    }

}
