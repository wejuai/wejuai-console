package com.wejuai.console.controller.dto.request;

import com.wejuai.entity.mysql.Sex;

/**
 * @author ZM.Wang
 */
public class UpdateUserInfoRequest {

    private String nickname;

    private String inShort;

    private String location;

    private long birthday;

    private Sex sex;

    public String getNickname() {
        return nickname;
    }

    public String getInShort() {
        return inShort;
    }

    public String getLocation() {
        return location;
    }

    public long getBirthday() {
        return birthday;
    }

    public Sex getSex() {
        return sex;
    }
}
