package com.pulamsi.base.baseUtil;

import android.text.TextUtils;

/**
 * URL过滤工具
 *
 * @author cangfei.hgy 2014-3-6
 */
public class UrlFilterUtil {

    private static final String HTTP_PRE = "http://";

    private static final String HTTPS_PRE = "https://";

    private static final String TAOBAO_DOMAIN = ".taobao.com";

    private static final String TMALL_DOMAIN = ".tmall.com";

    private static final String ETAO_DOMAIN = ".etao.com";

    /**
     * 链接是否合法，判断是否为淘系URL（暂支持淘宝、天猫、一淘）
     *
     * @return
     */
    public static boolean isLegalUrl(String url) {
        String domain = parseDomain(url);

        // 只有当域名是以淘宝、天猫、一淘根域名结尾的才属于合法URL
        if (domain.endsWith(TAOBAO_DOMAIN) || domain.endsWith(TMALL_DOMAIN) || domain.endsWith(ETAO_DOMAIN)) {
            return true;
        }
        return false;
    }

    /**
     * 解析域名
     *
     * @param url
     * @return
     */
    public static String parseDomain(String url) {
        String domain = "";

        if (TextUtils.isEmpty(url)) {
            return domain;
        }

        if (url.startsWith(HTTP_PRE)) {

            url = StringUtil.substringAfter(url, HTTP_PRE);
        } else if (url.startsWith(HTTPS_PRE)) {
            url = StringUtil.substringAfter(url, HTTPS_PRE);
        } else {
            return domain;
        }

        domain = StringUtil.substringBefore(url, "/");
        HaiLog.d("Etao JsBridge Message", "domain:" + domain);

        return domain;
    }
}
