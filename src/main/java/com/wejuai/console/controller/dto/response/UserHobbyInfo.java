package com.wejuai.console.controller.dto.response;

import com.wejuai.dto.response.HobbyInfo;
import com.wejuai.entity.mysql.UserHobby;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ZM.Wang
 */
public class UserHobbyInfo {

    private Set<HobbyInfo> hobbies;
    private Set<HobbyInfo> openHobbies;

    public UserHobbyInfo(UserHobby userHobby) {
        this.hobbies = userHobby.getHobbies().stream().map(HobbyInfo::new).collect(Collectors.toSet());
        this.openHobbies = userHobby.getOpenHobbies().stream().map(HobbyInfo::new).collect(Collectors.toSet());
    }

    public Set<HobbyInfo> getHobbies() {
        return hobbies;
    }

    public Set<HobbyInfo> getOpenHobbies() {
        return openHobbies;
    }
}
