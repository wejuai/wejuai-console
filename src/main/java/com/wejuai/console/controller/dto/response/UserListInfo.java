package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mysql.Sex;
import com.wejuai.entity.mysql.User;
import com.wejuai.util.MediaUtils;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.LocalDate;

import static com.wejuai.dto.Constant.DATE_FORMAT5;


/**
 * @author ZM.Wang
 */
public class UserListInfo {

    private final String id;
    private final String createdAt;
    @ApiModelProperty("昵称")
    private final String nickname;
    @ApiModelProperty("年龄")
    private String age;
    @ApiModelProperty("生日")
    private String birthday;
    @ApiModelProperty("性别")
    private final Sex sex;
    @ApiModelProperty("头像")
    private final String headImage;
    @ApiModelProperty("积分")
    private final long integral;
    @ApiModelProperty("邮箱")
    private final String email;
    @ApiModelProperty("手机")
    private final String phone;
    @ApiModelProperty("账号id")
    private final String accountsId;

    public UserListInfo(User user) {
        this.id = user.getId();
        this.createdAt = DateFormatUtils.format(user.getCreatedAt(), DATE_FORMAT5);
        this.nickname = user.getNickName();
        if (user.getBirthday() != null) {
            this.age = LocalDate.now().compareTo(user.getBirthday()) + "";
            this.birthday = user.getBirthday().toString();
        }
        this.sex = user.getSex();
        if (user.getHeadImage() == null) {
            this.headImage = MediaUtils.buildUrlByOssKey(MediaUtils.DEF_HEAD_IMAGE);
        } else {
            this.headImage = MediaUtils.buildUrl(user.getHeadImage());
        }
        this.integral = user.getIntegral();
        this.email = user.getAccounts().getEmail();
        this.phone = user.getAccounts().getPhone();
        this.accountsId = user.getAccounts().getId();
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAge() {
        return age;
    }

    public String getBirthday() {
        return birthday;
    }

    public Sex getSex() {
        return sex;
    }

    public String getHeadImage() {
        return headImage;
    }

    public long getIntegral() {
        return integral;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAccountsId() {
        return accountsId;
    }
}
