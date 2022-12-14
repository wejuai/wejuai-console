package com.wejuai.console.config;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author ZM.Wang
 * 替换spring单次流请求为复用流
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] requestBody;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        requestBody = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {

        final ByteArrayInputStream byteInputStream = new ByteArrayInputStream(requestBody);

        return new ServletInputStream() {

            @Override
            public int read() {
                return byteInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}