package com.wejuai.console.config;

import com.wejuai.console.repository.mongo.ConsoleOperationRecordRepository;
import com.wejuai.entity.mongo.ConsoleOperationRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author YQ.Huang
 */
@Configuration
public class SecurityConfig {
    public static final String SESSION_LOGIN = "login";

    private final ConsoleOperationRecordRepository consoleOperationRecordRepository;

    public SecurityConfig(ConsoleOperationRecordRepository consoleOperationRecordRepository) {
        this.consoleOperationRecordRepository = consoleOperationRecordRepository;
    }

    @Bean
    FilterRegistrationBean<ApiSecurityFilter> apiSecurityFilterRegistration() {
        FilterRegistrationBean<ApiSecurityFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ApiSecurityFilter(consoleOperationRecordRepository));
        registration.addUrlPatterns("/api/*");
        registration.setOrder(5);
        return registration;
    }

    @Bean
    FilterRegistrationBean<BodyReaderFilter> bodyReaderFilterRegistration() {
        FilterRegistrationBean<BodyReaderFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new BodyReaderFilter());
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1);
        return registration;
    }

    public static class ApiSecurityFilter implements Filter {

        private static final Logger logger = LoggerFactory.getLogger(ApiSecurityFilter.class);

        private final ConsoleOperationRecordRepository consoleOperationRecordRepository;

        public ApiSecurityFilter(ConsoleOperationRecordRepository consoleOperationRecordRepository) {
            this.consoleOperationRecordRepository = consoleOperationRecordRepository;
        }

        @Override
        public void init(FilterConfig filterConfig) {
            logger.info("api安全框架启动");
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            String ip = servletRequest.getHeader("x-real-ip");
            ip = StringUtils.isBlank(ip) ? servletRequest.getRemoteAddr() : ip;
            if (servletRequest.getSession().getAttribute(SESSION_LOGIN) == null) {
                if (!servletRequest.getContextPath().contains("validateLogin")) {
                    logger.debug("Session中不存在[{}]属性，用户未登录，ip：[{}]", SESSION_LOGIN, ip);
                }
                HttpServletResponse servletResponse = (HttpServletResponse) response;
                servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String method = servletRequest.getMethod();
            if (!StringUtils.equals("GET", method)) {
                String contextPath = servletRequest.getServletPath();
                String sessionId = servletRequest.getSession().getId();
                String content = StreamUtils.copyToString(servletRequest.getInputStream(), StandardCharsets.UTF_8);
                consoleOperationRecordRepository.save(new ConsoleOperationRecord(contextPath, ip, sessionId, method, content));
            }
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
        }
    }

    public static class BodyReaderFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            ServletRequest requestWrapper = null;
            if (request instanceof HttpServletRequest) {
                requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
            }
            if (requestWrapper == null) {
                chain.doFilter(request, response);
            } else {
                chain.doFilter(requestWrapper, response);
            }
        }

        @Override
        public void destroy() {
        }

    }
}
