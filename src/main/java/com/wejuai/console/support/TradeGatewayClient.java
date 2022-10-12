package com.wejuai.console.support;

import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.ServerException;
import com.wejuai.dto.request.WithdrawalTradeRequest;
import com.wejuai.dto.response.MchTradeResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author ZM.Wang
 */
public class TradeGatewayClient {

    private final RestTemplate restTemplate;
    private final String url;

    public TradeGatewayClient(RestTemplate restTemplate, String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public MchTradeResponse merchantPay(WithdrawalTradeRequest request, String ip) {
        try {
            return restTemplate.postForObject(url + "/trade/merchantPay?ip={ip}", request, MchTradeResponse.class, ip);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

}
