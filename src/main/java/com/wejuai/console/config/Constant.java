package com.wejuai.console.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

public interface Constant {

    ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    Random RANDOM = new Random();

    String CELESTIAL_BODY_STATISTICS_ID = "CelestialBodyStatistics";
    String ORDERS_STATISTICS_ID = "OrdersStatistics";
    String CHARGE_STATISTICS_ID = "ChargeStatistics";
    String USER_STATISTICS_ID = "userStatistics";
    String HOBBY_TOTAL_HOT_ID = "hobbyTotalHot";

}
