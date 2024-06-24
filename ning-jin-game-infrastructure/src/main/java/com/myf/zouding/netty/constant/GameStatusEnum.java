package com.myf.zouding.netty.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author myf
 */
@Getter
@AllArgsConstructor
public enum GameStatusEnum {


    /**
     * 匹配
     */
    MATCH("match"),
    STAND_ALONE("standAlone"),
    /**
     * 接受邀请
     */
    APPOINT_INVITE("appointInvite"),
    ON_CLOSE("onclose"),
    ON_OPEN("onopen"),
    HEART_BEAT("heartbeat");

    private String name;


    public static GameStatusEnum fromName(String name) {
        for (GameStatusEnum status : GameStatusEnum.values()) {
            if (status.name.equalsIgnoreCase(name)) {
                return status;
            }
        }
        return null;
    }
}
