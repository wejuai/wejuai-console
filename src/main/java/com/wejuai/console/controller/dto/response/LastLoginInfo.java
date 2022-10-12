package com.wejuai.console.controller.dto.response;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

import static com.wejuai.dto.Constant.DATE_FORMAT5;

/**
 * @author ZM.Wang
 */
public class LastLoginInfo {

    private final String date;
    private final String address;

    public LastLoginInfo(Date date, String address) {
        this.date = DateFormatUtils.format(date, DATE_FORMAT5);
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }
}
