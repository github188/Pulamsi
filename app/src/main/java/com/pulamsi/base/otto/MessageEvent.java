package com.pulamsi.base.otto;

import java.util.HashMap;

/**
 * Created by DaiDingKang
 * Date: 2016-03-07
 * Time: 17:25
 *
 * 通用的Event，包括TAG和MSG
 */
public class MessageEvent {
    private String mMsg;
    private String mTag;
    HashMap<String,String> mMap;
    public MessageEvent(String msg,String tag) {
        mMsg = msg;
        mTag = tag;
    }
    public MessageEvent(String tag) {
        mTag = tag;
    }
    public MessageEvent(HashMap<String,String> map,String tag) {
        mMap = map;
        mTag = tag;
    }

    public HashMap<String, String> getMap() {
        return mMap;
    }

    public String getMsg(){
        return mMsg;
    }
    public String getTag(){
        return mTag;
    }

}
