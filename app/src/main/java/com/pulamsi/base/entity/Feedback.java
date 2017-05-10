package com.pulamsi.base.entity;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-05-19
 * Time: 11:15
 * FIXME
 */
public class Feedback {
    private boolean successful;
    private String message;
    private String type;
    private Object obj;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
