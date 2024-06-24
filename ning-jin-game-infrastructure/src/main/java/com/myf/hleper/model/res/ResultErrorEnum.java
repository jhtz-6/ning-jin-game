package com.myf.hleper.model.res;

import lombok.AllArgsConstructor;

/**
 * @author myf
 */

@AllArgsConstructor
public enum ResultErrorEnum {
    invite_fail_gaming("invite_fail_gaming","进入邀请失败,对方正在游戏中"),
    invite_fail_off_line("invite_fail_off_line","对方已离线,进入邀请失败"),
    method_not_support("method_not_support", "该方法暂不支持"),
    login_failed_try_again("login_failed_try_again", "登陆失败,请稍后重试"),
    invitation_expired("Invitation expired", "邀请已过期");

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
