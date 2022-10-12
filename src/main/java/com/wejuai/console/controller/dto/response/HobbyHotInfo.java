package com.wejuai.console.controller.dto.response;

import com.wejuai.entity.mongo.statistics.HobbyHotByDay;
import com.wejuai.entity.mongo.statistics.HobbyTotalHotByDay;

/**
 * @author ZM.Wang
 */
public class HobbyHotInfo {

    private final long watched;
    private final long commented;
    private final long created;
    private final long followed;
    private final long unfollowed;
    private final String date;

    public HobbyHotInfo(HobbyTotalHotByDay data) {
        this.watched = data.getWatched();
        this.commented = data.getCommented();
        this.created = data.getCreated();
        this.followed = data.getFollowed();
        this.unfollowed = data.getUnfollowed();
        this.date = data.getDate().toString();
    }

    public HobbyHotInfo(HobbyHotByDay data) {
        this.watched = data.getWatched();
        this.commented = data.getCommented();
        this.created = data.getCreated();
        this.followed = data.getFollowed();
        this.unfollowed = data.getUnfollowed();
        this.date = data.getDate().toString();
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

    public String getDate() {
        return date;
    }

    public long getUnfollowed() {
        return unfollowed;
    }
}
