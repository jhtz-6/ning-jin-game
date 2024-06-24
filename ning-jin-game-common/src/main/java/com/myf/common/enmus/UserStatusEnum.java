package com.myf.common.enmus;



/**
 * @author myf
 */

public enum UserStatusEnum {


    NEW("NEW","连接已建立"),
    MATCHING("MATCHING","匹配中"),
    GAMEING("GAMEING","游戏中");

    private String name;
    private String value;

    UserStatusEnum(String name,String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
