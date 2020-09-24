package nioProxy;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * ClassName: Client
 * Description:
 * date: 2020/9/21 23:58
 *
 * @author :乌鸦坐飞机亠
 * @version:
 */
public class Client extends Thread {
    Selector selector = null;

    public Client(String ip, int port) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.bind(new InetSocketAddress(ip, port));

            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
