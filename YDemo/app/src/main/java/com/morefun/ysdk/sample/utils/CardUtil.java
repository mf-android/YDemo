package com.morefun.ysdk.sample.utils;

import java.util.HashMap;
import java.util.Map;

public class CardUtil {

    public static String getCardTypFromAid(String aid) {
        if (cardType.containsKey(aid.substring(0, 10))) {
            return cardType.get(aid.substring(0, 10));
        }
        return "";
    }

    private static Map<String, String> cardType = new HashMap<String, String>();

    static {
        cardType.put("A000000004", "MASTER");
        cardType.put("A000000003", "VISA");
        cardType.put("A000000025", "AMEX");
        cardType.put("A000000065", "JCB");
        cardType.put("A000000152", "DISCOVER");
        cardType.put("A000000324", "DISCOVER");
        cardType.put("A000000333", "PBOC");
        cardType.put("A000000524", "RUPAY");
    }

}
