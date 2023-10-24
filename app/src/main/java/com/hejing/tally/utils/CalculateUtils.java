package com.hejing.tally.utils;

import java.math.BigDecimal;

public class CalculateUtils {
    /* 进行除法运算，保留四位小数 */
    public static double div(double d1, double d2) {
        double d3 = d1/d2;
        BigDecimal b1 = new BigDecimal(d3);
        double val = b1.setScale(4, 4).doubleValue();
        return val;
    }

}
























