package com.myf.zouding.service;

import com.myf.hleper.model.dto.WxLoginDTO;
import com.myf.hleper.model.res.Result;

/**
 * @author myf
 */
public interface WxApiHandlerService {

    /**
     * 通过jsCode登陆
     *
     * @param jsCode
     * @return
     */
    Result<WxLoginDTO> loginByJsCode(String jsCode);
}
