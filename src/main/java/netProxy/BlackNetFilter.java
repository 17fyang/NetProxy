package netProxy;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * ClassName: BlackNetFilter
 * Description:
 * date: 2020/9/19 21:41
 *
 * @author :乌鸦坐飞机亠
 * @version:
 */
public class BlackNetFilter implements NetFilter {

    public static Set<String> blackNetSet=new HashSet<String>(){{
        add("www.baidu.com");
        add("baidu.com");
        add("activity.windows.com");
    }};

    @Override
    public OutputStream filteByHost(OutputStream out, String host) {
        if(blackNetSet.contains(host)) {
            System.out.println("检测到恶意网站 " + host);
            return null;
        }else
            return out;
    }

    @Override
    public OutputStream filteByInputStream(OutputStream out, InputStream in) {
        return null;
    }
}
