package netProxy;

import java.io.*;
import java.util.regex.Pattern;

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
        //还没写完，这里只是实现了主要逻辑，还没写输入用来匹配的域名，以及完成输出字节流
        //域名有两种形式，www.baidu.com或者.baidu.com，两种都要做匹配
        String dm = ".baid.com";
        String wwwdm = "www" + dm;
        BufferedInputStream bis = new BufferedInputStream(in);
        byte[] buffer = new byte[1024];
        int count, cnt = 0;
        try {
        while (true) {
            cnt++;
            count = bis.read(buffer);
            if (count <= 5) {
                break;
            }
            String str = new String(buffer, 0, count, "utf-8");
            if (cnt < 2) {
                String[] strArray = str.split("\r\n");
                for (String s : strArray) {
                    if (Pattern.matches("Set-Cookie:.*?", s)) {
                        if ((s.indexOf(dm) == -1) || (s.indexOf(wwwdm) == -1)) {
                            s = "Set-Cookie: NULL";
                        }
                    }
                    System.out.println("s: "  + s);
                    //s写进outPutStream里
                }
            } else {
                //str直接写outPutStream
                //System.out.println();
            }
        }
        bis.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
