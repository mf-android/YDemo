package com.morefun.ysdk.sample.utils;

import android.util.Log;

public class StringUitls {

    private String transformAmount(String amount) {
        try {
            long lAmount = Long.parseLong(amount);
            amount = String.valueOf(lAmount * 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }
}
