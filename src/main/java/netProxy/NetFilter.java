package netProxy;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * ClassName: NetFilter
 * Description:
 * date: 2020/9/19 21:39
 *
 * @author :乌鸦坐飞机亠
 * @version:
 */
public interface NetFilter {
    public OutputStream filteByHost(OutputStream out,String host);
    public OutputStream filteByInputStream(OutputStream out, InputStream in);
}
