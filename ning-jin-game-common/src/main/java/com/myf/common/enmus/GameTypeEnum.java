package com.myf.common.enmus;

import lombok.AllArgsConstructor;

/**
 * @author myf
 */

@AllArgsConstructor
public enum GameTypeEnum {

    SINGLE("SINGLE","单机"),
    MATCH("MATCH","匹配"),
    INVITE("INVITE","邀请");


    private String name;
    private String value;


}
