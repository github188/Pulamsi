package com.pulamsi.base.baseUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class MapUtil {

    /**
     * 遍历 hrefParams 里的参数，组装成一个map
     *
     * @param hrefParamJson
     * @return
     */
    public static HashMap<String, String> buildParamMap(JSONObject hrefParamJson) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        Iterator it = hrefParamJson.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = hrefParamJson.optString(key);
            paramMap.put(key, value);
        }

        return paramMap;
    }
}
