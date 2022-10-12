package com.wejuai.console.support;

import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.ServerException;
import com.wejuai.dto.request.ArticleRevokeRequest;
import com.wejuai.dto.request.SaveArticleRequest;
import com.wejuai.dto.request.SaveCelestialBodyRequest;
import com.wejuai.dto.response.ChargeListInfo;
import com.wejuai.dto.response.IdBaseResponse;
import com.wejuai.dto.response.OrderAppealInfo;
import com.wejuai.dto.response.OrdersInfo;
import com.wejuai.dto.response.RefreshUserIntegralInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.dto.response.UserIntegralInfo;
import com.wejuai.dto.response.WithdrawalInfo;
import com.wejuai.entity.mongo.trade.TradeStatus;
import com.wejuai.entity.mysql.ApplyStatus;
import com.wejuai.entity.mysql.ChannelQueryType;
import com.wejuai.entity.mysql.OrdersPageType;
import com.wejuai.entity.mysql.OrdersType;
import com.wejuai.entity.mysql.WithdrawalType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author ZM.Wang
 */
public class WejuaiCoreClient {

    private final RestTemplate restTemplate;
    private final String url;

    public WejuaiCoreClient(RestTemplate restTemplate, String url) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public UserIntegralInfo sumUserWithdrawableIntegral(String userId) {
        try {
            return restTemplate.getForObject(url + "/orders/userIntegral/" + userId, UserIntegralInfo.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public RefreshUserIntegralInfo syncUserPoint(String userId) {
        try {
            return restTemplate.postForObject(url + "/celestialBody/user/{userId}/point/sync", null, RefreshUserIntegralInfo.class, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<ChargeListInfo> getChargeInfos(String userId, TradeStatus status, ChannelQueryType channelType, Long start, Long end, int page, int size) {
        try {
            return restTemplate.exchange(url + "/orders/charge?userId={userId}&status={status}&channelType={channelType}&page={page}&size={size}&start={start}&end={end}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<ChargeListInfo>>() {
                    }, userId, status, channelType, page, size, start, end).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<WithdrawalInfo> getWithdrawals(String id, String userId, ApplyStatus status, WithdrawalType channelType, Long start, Long end, int page, int size) {
        try {
            return restTemplate.exchange(url + "/orders/withdrawal?id={id}&userId={userId}&status={status}&channelType={channelType}&page={page}&size={size}&start={start}&end={end}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<WithdrawalInfo>>() {
                    }, id, userId, status, channelType, page, size, start, end).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<OrderAppealInfo> getOrderAppeals(String userId, String start, String end, ApplyStatus status, OrdersPageType type, int page, int size) {
        try {
            return restTemplate.exchange(url + "/orders/orderAppeal?userId={userId}7start={start}&end={end}&status={status}&type={type}&page={page}&size={size}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<OrderAppealInfo>>() {
                    }, userId, start, end, status == null ? "" : status, type == null ? "" : type, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<OrdersInfo> getOrders(String userId, OrdersType type, Boolean income, boolean notZero, Long start, Long end, int page, int size) {
        try {
            return restTemplate.exchange(url + "/orders?userId={userId}&type={type}&income={income}&notZero={notZero}&start={start}&end={end}&page={page}&size={size}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<Slice<OrdersInfo>>() {
                    }, userId, type, income == null ? "" : income, notZero, start, end, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void revokeArticle(String id, String reason) {
        try {
            ArticleRevokeRequest request = new ArticleRevokeRequest(null, true, reason);
            restTemplate.postForObject(url + "/app/article/{id}/revoke", request, Void.class, id);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public IdBaseResponse saveArticle(String id, SaveArticleRequest request) {
        try {
            id = StringUtils.isBlank(id) ? "" : id;
            return restTemplate.postForObject(url + "/app/article?id={id}", request, IdBaseResponse.class, id);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void rewardDemandExtension(String id) {
        try {
            restTemplate.put(url + "/app/rewardDemand/{id}/extension?console=true", null, id);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void revokeRewardDemand(String id, String reason) {
        try {
            ArticleRevokeRequest request = new ArticleRevokeRequest(null, true, reason);
            restTemplate.postForObject(url + "/app/rewardDemand/rewardSubmission/{id}/revoke", request, Void.class, id);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void deleteArticle(String id, String reason) {
        try {
            restTemplate.delete(url + "/app/article/{id}?console=true&reason={reason}", id, reason);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public IdBaseResponse saveCelestialBody(SaveCelestialBodyRequest request) {
        try {
            return restTemplate.postForObject(url + "/celestialBody", request, IdBaseResponse.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

}
