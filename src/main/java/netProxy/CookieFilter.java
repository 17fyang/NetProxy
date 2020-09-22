package netProxy;

import java.io.InputStream;
import java.io.OutputStream;
/*
这里是做一个过滤器，过滤掉响应中的第三方cookie，防止第三方使用cookie收集其他信息。、
目前可能有的bug是：转跳时不同域名的网站时，授权登录之类的cookie会被过滤掉
因为过滤的条件是根据请求的网站域名以及cookie的域名是否匹配
 */
public class CookieFilter implements NetFilter {

    public OutputStream filteByHost(OutputStream out, String host) {
        return null;
    }

    public OutputStream filteByInputStream(OutputStream out, InputStream in) {
        return null;
    }
}
