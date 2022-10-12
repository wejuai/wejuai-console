package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mysql.OauthType;
import com.wejuai.entity.mysql.OtherUser;
import com.wejuai.entity.mysql.WeixinUser;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class OtherAccountsInfo {

    private String id;
    private String createdAt;
    @ApiModelProperty("是否有该第三方帐号")
    private boolean has = true;
    @ApiModelProperty("第三方帐号类型")
    private OauthType type;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("第三方用户id，微信是开放平台id")
    private String openId;
    @ApiModelProperty("公众号openId")
    private String offiOpenId;
    @ApiModelProperty("小程序openId")
    private String appOpenId;
    @ApiModelProperty("省")
    private String province;
    @ApiModelProperty("市")
    private String city;
    @ApiModelProperty("国家")
    private String country;
    private String unionId;

    public OtherAccountsInfo(OtherUser otherUser) {
        if (otherUser == null) {
            this.has = false;
            return;
        }
        this.id = otherUser.getId();
        this.createdAt = DateFormatUtils.format(otherUser.getCreatedAt(), DATE_FORMAT5);
        this.type = otherUser.getOauthType();
        this.avatar = otherUser.getAvatar();
        this.nickName = otherUser.getNickName();
        this.openId = otherUser.getId();
        if (otherUser instanceof WeixinUser) {
            WeixinUser weixinUser = (WeixinUser) otherUser;
            this.offiOpenId = weixinUser.getOffiOpenId();
            this.appOpenId = weixinUser.getAppOpenId();
            this.province = weixinUser.getProvince();
            this.city = weixinUser.getCity();
            this.country = weixinUser.getCountry();
            this.unionId = weixinUser.getUnionId();
        }
    }

    public OauthType getType() {
        return type;
    }

    public boolean getHas() {
        return has;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getOpenId() {
        return openId;
    }

    public String getOffiOpenId() {
        return offiOpenId;
    }

    public String getAppOpenId() {
        return appOpenId;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getUnionId() {
        return unionId;
    }
}
