package com.ting.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by liu on 15/8/3.
 */
public class UtilFloat {

    public static String getValue(int n, double value) {

        DecimalFormat df = null;
        switch (n) {
            case 1:
                df = new DecimalFormat("#0.0");
                break;

            case 2:
                df = new DecimalFormat("#0.00");
                break;
        }

        return df.format(value);
    }


}
