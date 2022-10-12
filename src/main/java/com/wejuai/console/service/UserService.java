package com.wejuai.console.service;

import com.endofmaster.commons.util.DateUtil;
import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.console.controller.dto.request.UpdateUserInfoRequest;
import com.wejuai.console.controller.dto.response.CancelAccountInfo;
import com.wejuai.console.controller.dto.response.UserHobbyInfo;
import com.wejuai.console.controller.dto.response.UserInfo;
import com.wejuai.console.controller.dto.response.UserListInfo;
import com.wejuai.console.repository.mongo.CelestialBodyRepository;
import com.wejuai.console.repository.mysql.*;
import com.wejuai.console.support.WejuaiCoreClient;
import com.wejuai.dto.response.UserIntegralInfo;
import com.wejuai.entity.mongo.CelestialBody;
import com.wejuai.entity.mysql.Accounts;
import com.wejuai.entity.mysql.CancelAccount;
import com.wejuai.entity.mysql.Hobby;
import com.wejuai.entity.mysql.Image;
import com.wejuai.entity.mysql.OauthType;
import com.wejuai.entity.mysql.Sex;
import com.wejuai.entity.mysql.User;
import com.wejuai.entity.mysql.UserHobby;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZM.Wang
 */
@Service
public class UserService {

    private final static String SYSTEM_USER_ID = "system";

    private final UserRepository userRepository;
    private final HobbyRepository hobbyRepository;
    private final ImageRepository imageRepository;
    private final QqUserRepository qqUserRepository;
    private final ArticleRepository articleRepository;
    private final AccountsRepository accountsRepository;
    private final WeiboUserRepository weiboUserRepository;
    private final UserHobbyRepository userHobbyRepository;
    private final WeixinUserRepository weixinUserRepository;
    private final RewardDemandRepository rewardDemandRepository;
    private final CancelAccountRepository cancelAccountRepository;
    private final ArticleDraftRepository articleDraftRepository;
    private final CelestialBodyRepository celestialBodyRepository;

    private final WejuaiCoreClient wejuaiCoreClient;

    public UserService(AccountsRepository accountsRepository, UserRepository userRepository, ImageRepository imageRepository, QqUserRepository qqUserRepository, ArticleRepository articleRepository, RewardDemandRepository rewardDemandRepository, WeiboUserRepository weiboUserRepository, UserHobbyRepository userHobbyRepository, HobbyRepository hobbyRepository, WeixinUserRepository weixinUserRepository, CancelAccountRepository cancelAccountRepository, ArticleDraftRepository articleDraftRepository, CelestialBodyRepository celestialBodyRepository, WejuaiCoreClient wejuaiCoreClient) {
        this.userRepository = userRepository;
        this.accountsRepository = accountsRepository;
        this.imageRepository = imageRepository;
        this.qqUserRepository = qqUserRepository;
        this.articleRepository = articleRepository;
        this.rewardDemandRepository = rewardDemandRepository;
        this.weiboUserRepository = weiboUserRepository;
        this.userHobbyRepository = userHobbyRepository;
        this.hobbyRepository = hobbyRepository;
        this.weixinUserRepository = weixinUserRepository;
        this.cancelAccountRepository = cancelAccountRepository;
        this.articleDraftRepository = articleDraftRepository;
        this.celestialBodyRepository = celestialBodyRepository;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    public Page<UserListInfo> getUsers(String id, String phone, String email, String nickname, LocalDate start, LocalDate end, Pageable pageable) {
        Specification<User> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(id)) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (StringUtils.isNotBlank(phone)) {
                predicates.add(cb.equal(root.get("accounts").get("phone"), phone));
            }
            if (StringUtils.isNotBlank(email)) {
                predicates.add(cb.equal(root.get("accounts").get("email"), email));
            }
            if (StringUtils.isNotBlank(nickname)) {
                predicates.add(cb.like(root.get("nickName"), "%" + nickname + "%"));
            }
            if (start != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), DateUtil.getAnyDayStart(start)));
            }
            if (end != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), DateUtil.getAnyDayEnd(end)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return userRepository.findAll(specification, pageable).map(UserListInfo::new);
    }

    public UserInfo getUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该用户: " + id));
        UserIntegralInfo userIntegralInfo = wejuaiCoreClient.sumUserWithdrawableIntegral(user.getId());
        return new UserInfo(user).setUserIntegralInfo(userIntegralInfo);
    }

    public UserHobbyInfo getUserHobby(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该用户: " + id));
        UserHobby userHobby = getUserHobby(user);
        return new UserHobbyInfo(userHobby);
    }

    @Transactional
    public void addHobby(String id, String hobbyId) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该用户: " + id));
        Hobby hobby = hobbyRepository.findById(hobbyId).orElseThrow(() -> new BadRequestException("没有该爱好: " + hobbyId));
        UserHobby userHobby = getUserHobby(user);
        userHobbyRepository.save(userHobby.addHobby(hobby));
        userRepository.save(user.addHobby());
    }

    @Transactional
    public void subHobby(String id, String hobbyId) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该用户: " + id));
        Hobby hobby = hobbyRepository.findById(hobbyId).orElseThrow(() -> new BadRequestException("没有该爱好: " + hobbyId));
        UserHobby userHobby = getUserHobby(user);
        if (userHobby.getHobbies().contains(hobby)) {
            userHobbyRepository.save(userHobby.reduceHobby(hobby));
        }
        if (userHobby.getOpenHobbies().contains(hobby)) {
            userHobbyRepository.save(userHobby.reduceOpenHobby(hobby));
        }
    }

    @Transactional
    public void resetOtherUser(String id, OauthType type) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该用户: " + id));
        Accounts accounts = user.getAccounts();
        if (type == OauthType.WEIXIN && accounts.getWeixinUser() != null) {
            weixinUserRepository.delete(accounts.getWeixinUser());
            accountsRepository.save(accounts.setWeixinUser(null));
        }
        if (type == OauthType.WEIBO && accounts.getWeiboUser() != null) {
            weiboUserRepository.delete(accounts.getWeiboUser());
            accountsRepository.save(accounts.setWeiboUser(null));
        }
        if (type == OauthType.QQ && accounts.getQqUser() != null) {
            qqUserRepository.delete(accounts.getQqUser());
            accountsRepository.save(accounts.setQqUser(null));
        }
    }

    public void updatePhone(String id, String phone) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该用户: " + id));
        Accounts accounts = user.getAccounts();
        accountsRepository.save(accounts.setPhone(phone));
    }

    /**
     * 封禁或者解封用户
     *
     * @param userId 用户id
     * @param ban    是否封禁
     */
    public void ban(String userId, boolean ban) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("没有该用户: " + userId));
        userRepository.save(user.setBan(ban));
    }

    public Page<CancelAccountInfo> getCancelAccounts(String userId, LocalDate start, LocalDate end, Pageable pageable) {
        Specification<CancelAccount> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(userId)) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (start != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), DateUtil.getAnyDayStart(start)));
            }
            if (end != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), DateUtil.getAnyDayEnd(end)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return cancelAccountRepository.findAll(specification, pageable).map(CancelAccountInfo::new);
    }

    @Transactional
    public void cancelAccount(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("没有该用户: " + userId));
        Accounts accounts = user.getAccounts();
        if (accounts.getWeiboUser() != null) {
            weiboUserRepository.delete(accounts.getWeiboUser());
        }
        if (accounts.getWeixinUser() != null) {
            weixinUserRepository.delete(accounts.getWeixinUser());
        }
        if (accounts.getQqUser() != null) {
            qqUserRepository.delete(accounts.getQqUser());
        }
        accountsRepository.save(accounts.cancelAccount());
        userRepository.save(user.cancelAccount());
        articleRepository.delByUser(userId);
        rewardDemandRepository.delByUser(userId);
        articleDraftRepository.deleteByUser(user);
        CelestialBody celestialBody = celestialBodyRepository.findByUser(userId);
        if (celestialBody != null) {
            celestialBodyRepository.save(celestialBody.cancelAccount());
        }
    }

    public void updateSystemUserAvatar(String imageId) {
        User user = getSystemUser();
        Image avatar = imageRepository.findById(imageId).orElseThrow(() -> new BadRequestException("没有该图片: " + imageId));
        userRepository.save(user.setHeadImage(avatar));
    }

    public void updateSystemUser(UpdateUserInfoRequest request) {
        User user = getSystemUser();
        LocalDate birthday = request.getBirthday() == 0 ? user.getBirthday() : DateUtil.date2LocalDate(new Date(request.getBirthday()));
        String inShort = StringUtils.isBlank(request.getInShort()) ? user.getInShort() : request.getInShort();
        String location = StringUtils.isBlank(request.getLocation()) ? user.getLocation() : request.getLocation();
        Sex sex = request.getSex() == null ? user.getSex() : request.getSex();
        String nickname = StringUtils.isBlank(request.getNickname()) ? user.getNickName() : request.getNickname();
        userRepository.save(user.updateInfo(nickname, birthday, sex, inShort, location));
    }

    private User getSystemUser() {
        return userRepository.findById(SYSTEM_USER_ID).orElseThrow();
    }

    private UserHobby getUserHobby(User user) {
        UserHobby userHobby = userHobbyRepository.findByUser(user);
        if (userHobby == null) {
            userHobby = userHobbyRepository.save(new UserHobby(user));
        }
        return userHobby;
    }

}
