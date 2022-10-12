package com.wejuai.console.controller.dto.response;

import com.wejuai.dto.response.UserIntegralInfo;
import com.wejuai.entity.mysql.Accounts;
import com.wejuai.entity.mysql.Performance;
import com.wejuai.entity.mysql.Sex;
import com.wejuai.entity.mysql.User;
import com.wejuai.util.MediaUtils;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.wejuai.dto.Constant.DATE_FORMAT2;
import static com.wejuai.util.MediaUtils.DEF_HEAD_IMAGE;

/**
 * @author ZM.Wang
 */
public class UserInfo {

    @ApiModelProperty("用户id")
    private final String id;
    @ApiModelProperty("昵称")
    private final String nickName;
    @ApiModelProperty("年龄")
    private final String age;
    @ApiModelProperty("生日，时间戳long型")
    private final String birthday;
    @ApiModelProperty("性别")
    private final Sex sex;
    @ApiModelProperty("简介")
    private final String inShort;
    @ApiModelProperty("可以随便填的所在地")
    private final String location;
    @ApiModelProperty("头像")
    private final String headImage;
    @ApiModelProperty("封面")
    private final String cover;
    @ApiModelProperty("作为买家的评分")
    private final String buyerScore;
    @ApiModelProperty("作为卖家的评分")
    private final String sellerScore;
    @ApiModelProperty("积分")
    private final long integral;
    @ApiModelProperty("账号id")
    private final String accountsId;
    @ApiModelProperty("爱好数量")
    private final long hobbyNum;
    @ApiModelProperty("用户手机性能")
    private final Performance performance;
    private final String phone;
    private final String email;
    @ApiModelProperty("最后一次登录ip")
    private final String ip;
    @ApiModelProperty("是否封号")
    private boolean ban;
    @ApiModelProperty("消息数量")
    private long msgNum;

    @ApiModelProperty("微信账号信息")
    private final OtherAccountsInfo weixin;
    @ApiModelProperty("qq账号信息")
    private final OtherAccountsInfo qq;
    @ApiModelProperty("微博账号信息")
    private final OtherAccountsInfo weibo;
    @ApiModelProperty("用户积分详情")
    private UserIntegralInfo userIntegralInfo;

    public UserInfo(User user) {
        this.id = user.getId();
        this.nickName = user.getNickName();
        this.age = user.getBirthday() == null ? "" : LocalDate.now().getYear() - user.getBirthday().getYear() + "";
        this.birthday = user.getBirthday() == null ? "" : user.getBirthday().format(DateTimeFormatter.ofPattern(DATE_FORMAT2));
        this.sex = user.getSex();
        this.inShort = user.getInShort();
        this.location = user.getLocation();
        this.headImage = user.getHeadImage() == null ? MediaUtils.buildUrlByOssKey(DEF_HEAD_IMAGE) : MediaUtils.buildUrl(user.getHeadImage());
        this.cover = MediaUtils.buildUrl(user.getCover());
        this.sellerScore = user.getSeller();
        this.hobbyNum = user.getHobbyNum();
        this.buyerScore = user.getBuyer();
        this.integral = user.getIntegral();
        this.ban = user.getBan();
        this.performance = user.getPerformance() == null ? Performance.NEW : user.getPerformance();
        this.msgNum = user.getMsgNum();
        Accounts accounts = user.getAccounts();
        this.accountsId = accounts.getId();
        this.phone = accounts.getPhone();
        this.email = accounts.getEmail();
        this.ip = accounts.getIp();
        this.ban = user.getBan();
        this.weixin = new OtherAccountsInfo(accounts.getWeixinUser());
        this.qq = new OtherAccountsInfo(accounts.getQqUser());
        this.weibo = new OtherAccountsInfo(accounts.getWeiboUser());
    }

    public UserIntegralInfo getUserIntegralInfo() {
        return userIntegralInfo;
    }

    public UserInfo setUserIntegralInfo(UserIntegralInfo userIntegralInfo) {
        this.userIntegralInfo = userIntegralInfo;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
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

    public String getInShort() {
        return inShort;
    }

    public String getLocation() {
        return location;
    }

    public String getHeadImage() {
        return headImage;
    }

    public String getCover() {
        return cover;
    }

    public String getBuyerScore() {
        return buyerScore;
    }

    public String getSellerScore() {
        return sellerScore;
    }

    public long getIntegral() {
        return integral;
    }

    public String getAccountsId() {
        return accountsId;
    }

    public long getHobbyNum() {
        return hobbyNum;
    }

    public Performance getPerformance() {
        return performance;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public OtherAccountsInfo getWeixin() {
        return weixin;
    }

    public OtherAccountsInfo getQq() {
        return qq;
    }

    public OtherAccountsInfo getWeibo() {
        return weibo;
    }

    public String getIp() {
        return ip;
    }

    public boolean getBan() {
        return ban;
    }

    public long getMsgNum() {
        return msgNum;
    }

}
