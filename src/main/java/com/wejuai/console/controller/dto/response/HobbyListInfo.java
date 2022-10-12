package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mongo.statistics.HobbyHot;
import com.wejuai.entity.mysql.Hobby;
import com.wejuai.util.MediaUtils;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class HobbyListInfo {

    private final String id;

    @ApiModelProperty("名称")
    private final String name;

    @ApiModelProperty("图片")
    private final String avatar;

    @ApiModelProperty("阅读数")
    private long watched;

    @ApiModelProperty("评论数")
    private long commented;

    @ApiModelProperty("应用数")
    private long created;

    @ApiModelProperty("粉丝数")
    private long followed;

    public HobbyListInfo(Hobby hobby, HobbyHot hobbyHot) {
        this.id = hobby.getId();
        this.name = hobby.getName();
        this.avatar = MediaUtils.buildUrl(hobby.getAvatar());
        if (hobbyHot != null) {
            this.watched = hobbyHot.getWatched();
            this.commented = hobbyHot.getCommented();
            this.created = hobbyHot.getCreated();
            this.followed = hobbyHot.getFollowed();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public long getWatched() {
        return watched;
    }

    public long getCommented() {
        return commented;
    }

    public long getCreated() {
        return created;
    }

    public long getFollowed() {
        return followed;
    }
}
