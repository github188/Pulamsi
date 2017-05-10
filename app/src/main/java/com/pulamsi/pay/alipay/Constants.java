package com.pulamsi.pay.alipay;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;

public class Constants {

    // 商户PID
    public static final String PARTNER = "2088911356733297";
    // 商户收款账号
    public static final String SELLER = "pulamsi@pulamsi.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALJZ04mRaYwt1ScwzF+vEPzZURkjGMJrFwiIzb0m0Lf8zH9dolx2xAJ9H8RwPL05Mc8B/uSE5D+n76pyNb7mh6Ewo1HFsjHNnD8o0827oN05M+xybh2fgv2Kci0nMhBI2Lu1i2AXNODNV4/9yEDX+16Frgotm0miAubV26hRTwCtAgMBAAECgYAuP0CtMXmvEctt6Cd2wEylZCy1WfZ3o5FLcz32SHRi4Tid5kfNtjZJduyf4YK+hnA1/4x68ULGhuOMzSTGpbyRTtD/lXutbs9ITnA8iiaQIEJsvyv1d3QEvTewIiDjT2NzF8fOrPH8DFtKV31RbRsQQIG2pxd9Pm3r3KvG/wLwOQJBANvW/wbjDOmGNmsWUmYB1InJrhMvUgdKyoWTgnPTIMinSqR6U4rJAq7oRQuvgWUKSTu9BC41bentn2r+QKK/9h8CQQDPr9Da5lv1R06kmuE48975rEUJVsSNrrDgWQ7Ca2rIEVkJMIa1FafaS2ZConsFcrwdl9s7xdeIT+8xYrKRovezAkEAjf8pzO39Gh1pqvPJF0BZYwKU4KlxB2rEs1DbMysNAu4jpWep7Hv3srguWOTs5DGnqeFmAN7b9vxYO0iswTLSqwJAFSjPeY3grpOuQnz5F0lZXUyc1/+8FMdIhALuywYQogOKc7I69zYWnNnzuDQ/nmV7HvS6xFM6y0uBjiGFGetlmwJBAIUwh1rjvPdnL4novgIsvzfRy2lcGkmY6qfyOhEQ2OyiRn7G1G0jsx1nR1SZQNTVKkAQNm7luGBri/JR2NE2D2o=";

    //回调地址
    public static final String NOTIFY_URL = PulamsiApplication.context.getString(R.string.serverbaseurl) + PulamsiApplication.context.getString(R.string.alipay_notify);

}

