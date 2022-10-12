package com.wejuai.console.controller.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author ZM.Wang
 */
public class SaveHobbyRequest {

    @NotBlank
    @ApiModelProperty("id，一般是爱好的英文单词")
    private String id;

    @NotBlank
    @ApiModelProperty("名称")
    private String name;

    @NotBlank
    @ApiModelProperty("图片id")
    private String avatar;

    @NotNull
    @Size(min = 1)
    @ApiModelProperty("搜索标签")
    private Set<String> tabs;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public Set<String> getTabs() {
        return tabs;
    }

    @Override
    public String toString() {
        return "SaveHobbyRequest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", tabs=" + tabs +
                '}';
    }
}
