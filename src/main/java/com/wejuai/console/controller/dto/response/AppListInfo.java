package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mysql.App;
import com.wejuai.entity.mysql.Article;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class AppListInfo {

    @ApiModelProperty("id")
    private final String id;
    @ApiModelProperty("创建时间，yyyy-MM-dd HH:mm:ss")
    private final String createdAt;
    @ApiModelProperty("用户id")
    private final String userId;
    @ApiModelProperty("爱好id")
    private final String hobbyId;
    @ApiModelProperty("标题")
    private final String title;
    @ApiModelProperty("是否系统删除")
    private final boolean del;
    @ApiModelProperty("是否作者删除")
    private final boolean authorDel;
    @ApiModelProperty("积分")
    private final long integral;

    public AppListInfo(App<?> app) {
        this.id = app.getId();
        this.createdAt = DateFormatUtils.format(app.getCreatedAt(), DATE_FORMAT5);
        this.userId = app.getUser().getId();
        this.hobbyId = app.getHobby().getId();
        this.title = app.getTitle();
        this.del = app.getDel();
        this.integral = app.getIntegral();
        this.authorDel = app instanceof Article && ((Article) app).getAuthorDel();
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getHobbyId() {
        return hobbyId;
    }

    public String getTitle() {
        return title;
    }

    public boolean getDel() {
        return del;
    }

    public long getIntegral() {
        return integral;
    }

    public boolean getAuthorDel() {
        return authorDel;
    }
}
