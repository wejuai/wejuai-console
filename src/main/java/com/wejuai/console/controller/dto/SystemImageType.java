package com.wejuai.console.controller.dto;

public enum SystemImageType {

    HOBBY("SYS_IMAGE/hobby/"),
    DEF_AVATAR("SYS_IMAGE/defAvatar/"),
    BACKGROUND("SYS_IMAGE/background/");

    private final String ossKeyPath;

    SystemImageType(String ossKeyPath) {
        this.ossKeyPath = ossKeyPath;
    }

    public String getOssKeyPath() {
        return ossKeyPath;
    }
}
