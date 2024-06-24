package com.myf.zouding.service.impl;

import com.myf.hleper.client.WxApiHandlerClient;
import com.myf.hleper.model.dto.WxLoginDTO;
import com.myf.hleper.model.res.Result;
import com.myf.zouding.service.WxApiHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: myf
 * @CreateTime: 2024-05-01  17:56
 * @Description: WxApiHandlerServiceImpl
 */
@Service
public class WxApiHandlerServiceImpl implements WxApiHandlerService {


    @Autowired
    WxApiHandlerClient wxApiHandlerClient;

    @Override
    public Result<WxLoginDTO> loginByJsCode(String jsCode) {
        return wxApiHandlerClient.loginByJsCode(jsCode);
    }
}
