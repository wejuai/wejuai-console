package com.wejuai.console.controller.dto.request;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class LoginRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    @SuppressWarnings("unused")
    public LoginRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unused")
    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
