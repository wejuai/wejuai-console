package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mongo.CelestialBody;
import com.wejuai.entity.mongo.HobbyTab;
import com.wejuai.entity.mongo.statistics.HobbyHot;
import com.wejuai.entity.mysql.Hobby;
import com.wejuai.util.MediaUtils;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

/**
 * @author ZM.Wang
 */
public class HobbyDetailedInfo {

    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("图片")
    private String avatar;

    @ApiModelProperty("阅读数")
    private long watched;

    @ApiModelProperty("评论数")
    private long commented;

    @ApiModelProperty("应用数")
    private long created;

    @ApiModelProperty("粉丝数")
    private long followed;

    @ApiModelProperty("积分")
    private long point;

    @ApiModelProperty("大小")
    private long size;

    @ApiModelProperty("横坐标")
    private double x;

    @ApiModelProperty("纵坐标")
    private double y;

    @ApiModelProperty("星球背景图")
    private String planetBackground;

    @ApiModelProperty("星球花纹图")
    private String planetTexture;

    @ApiModelProperty("花纹选装角度")
    private int angle;

    @ApiModelProperty("搜索tab")
    private Set<HobbyTab> tabs;

    public HobbyDetailedInfo(Hobby hobby, HobbyHot hobbyHot, CelestialBody celestialBody, Set<HobbyTab> tabs) {
        this.id = hobby.getId();
        this.name = hobby.getName();
        this.avatar = MediaUtils.buildUrl(hobby.getAvatar());
        this.tabs = tabs;
        if (hobbyHot != null) {
            this.watched = hobbyHot.getWatched();
            this.commented = hobbyHot.getCommented();
            this.created = hobbyHot.getCreated();
            this.followed = hobbyHot.getFollowed();
        }
        if (celestialBody != null) {
            this.point = celestialBody.getPoint();
            this.size = celestialBody.getSize();
            this.x = celestialBody.getX();
            this.y = celestialBody.getY();
            this.angle = celestialBody.getAngle();
            this.planetTexture = MediaUtils.buildUrlByOssKey(celestialBody.getTexture().replaceAll("big", "small"));
            this.planetBackground = MediaUtils.buildUrlByOssKey(celestialBody.getBackground().replaceAll("big", "small"));

        }
    }

    HobbyDetailedInfo() {
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

    public long getPoint() {
        return point;
    }

    public long getSize() {
        return size;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getPlanetBackground() {
        return planetBackground;
    }

    public String getPlanetTexture() {
        return planetTexture;
    }

    public int getAngle() {
        return angle;
    }

    public Set<HobbyTab> getTabs() {
        return tabs;
    }

}
