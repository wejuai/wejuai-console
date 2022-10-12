package com.wejuai.console.config;

import com.wejuai.console.support.WxServiceClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
@Configuration
@EnableConfigurationProperties({WxServiceConfig.Properties.class, WxServiceConfig.Msg.class, WxServiceConfig.Page.class})
public class WxServiceConfig {

    private final Properties weixin;
    private final Msg msg;
    private final Page page;

    public WxServiceConfig(Properties weixin, Msg msg, Page page) {
        this.weixin = weixin;
        this.msg = msg;
        this.page = page;
    }

    @Bean
    WxServiceClient wxServiceClient(RestTemplate restTemplate) {
        return new WxServiceClient(weixin.getGateway(), restTemplate);
    }

    @Validated
    @ConfigurationProperties(prefix = "weixin.page")
    public static class Page {
        @NotBlank
        private String withdrawal;

        public String getWithdrawal() {
            return withdrawal;
        }

        public Page setWithdrawal(String withdrawal) {
            this.withdrawal = withdrawal;
            return this;
        }
    }

    @Validated
    @ConfigurationProperties(prefix = "weixin.msg")
    public static class Msg {
        @NotBlank
        private String appAudit;

        public String getAppAudit() {
            return appAudit;
        }

        public Msg setAppAudit(String appAudit) {
            this.appAudit = appAudit;
            return this;
        }

    }

    @Validated
    @ConfigurationProperties(prefix = "weixin")
    public static class Properties {
        @NotBlank
        private String gateway;

        public String getGateway() {
            return gateway;
        }

        public Properties setGateway(String gateway) {
            this.gateway = gateway;
            return this;
        }
    }


    public Properties getWeixin() {
        return weixin;
    }

    public Msg getMsg() {
        return msg;
    }

    public Page getPage() {
        return page;
    }


}
