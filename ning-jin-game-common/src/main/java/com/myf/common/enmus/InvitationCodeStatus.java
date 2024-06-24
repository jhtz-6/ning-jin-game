package com.myf.common.enmus;

import lombok.AllArgsConstructor;

/**
 * @author myf
 */

public enum InvitationCodeStatus {

    PENDING("0","pending"),
    ACCEPTED("1","accepted"),
    REJECTED("2","rejected"),
    EXPIRED("3","expired");

    private String code;
    private String status;


    InvitationCodeStatus(String code, String status) {
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
