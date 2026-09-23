package com.example.shoppingcart.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String orderNo(Integer userId) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + userId;
    }
}
