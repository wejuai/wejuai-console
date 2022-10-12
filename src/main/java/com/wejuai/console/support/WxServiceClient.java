package com.wejuai.console.support;

import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.ServerException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author ZM.Wang
 */
public class WxServiceClient {

    private final RestTemplate restTemplate;
    private final String url;

    public WxServiceClient(String url,RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public void sendWxTemplateMsg(WxTemplateMsg msg) {
        try {
            restTemplate.postForObject(url + "/message/subscribe", msg, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

}
