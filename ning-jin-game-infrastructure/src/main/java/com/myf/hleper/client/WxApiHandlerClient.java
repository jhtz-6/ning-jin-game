package com.myf.hleper.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myf.common.util.HttpUtils;
import com.myf.hleper.model.dto.WxLoginDTO;
import com.myf.hleper.model.res.Result;
import com.myf.hleper.model.res.ResultErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2024-05-01  17:14
 * @Description: WxApiHandlerClient
 */
@Slf4j
@Component
public class WxApiHandlerClient {

    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    private static final String APPID_KEY = "appid";
    private static final String APPID_VALUE = "wx222666b3a402852c";

    private static final String SECRET_KEY = "secret";
    private static final String SECRET_VALUE = "4069668ff75f145c142e09e34b74ed1d";

    private static final String JS_CODE_KEY = "js_code";

    private static final String GRANT_TYPE_KEY = "grant_type";
    private static final String GRANT_TYPE_VALUE = "authorization_code";

    private static final String ERR_MSG_KEY = "errmsg";


    public Result<WxLoginDTO> loginByJsCode(String jsCode) {
        if (StringUtils.isBlank(jsCode)) {
            return null;
        }
        Map<String, String> headerMap = new HashMap<>(8);
        headerMap.put(APPID_KEY, APPID_VALUE);
        headerMap.put(SECRET_KEY, SECRET_VALUE);
        headerMap.put(JS_CODE_KEY, jsCode);
        headerMap.put(GRANT_TYPE_KEY, GRANT_TYPE_VALUE);
        String result = HttpUtils.sendGetWithMap(WX_LOGIN_URL, headerMap);
        if(Objects.isNull(result)){
            return Result.failure(ResultErrorEnum.login_failed_try_again.getMessage());
        }
        JSONObject resObject = JSON.parseObject(result);
        Object errMsg = resObject.get(ERR_MSG_KEY);
        if (Objects.nonNull(errMsg)) {
            log.error("loginByJsCode.error.result:{},jsCode:{}", result, jsCode);
            return Result.failure((String) errMsg);
        }
        try {
            WxLoginDTO wxLoginDTO = JSON.parseObject(result, WxLoginDTO.class);
            return Result.success(wxLoginDTO);
        } catch (Exception e) {
            log.error("loginByJsCode.e.result:{},jsCode:{}", result, jsCode, e);
            return Result.failure("解析微信登陆返回结果失败:" + e.getMessage());
        }
    }

}
