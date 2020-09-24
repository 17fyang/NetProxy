package nioProxy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * ClassName: Server
 * Description:
 * date: 2020/9/21 23:46
 *
 * @author :乌鸦坐飞机亠
 * @version:
 */
public class Server extends Thread {
    public static final int PORT = 8087;
    public static final int BUFFER_SIZE = 1500;

    private Selector selector = null;

    public Server() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(PORT));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (selector.select() > 0) {
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        serverSocketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        System.out.println("注册连接channel成功");
                    }
                    if (key.isReadable()) {
                        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        socketChannel.read(byteBuffer);
                        byteBuffer.flip();
                        String content = StandardCharsets.UTF_8.decode(byteBuffer).toString();

                        //TODO 从客户端拿到的内容解析
                        System.out.println(content);
                    }
                    if (key.isWritable()) {
                        System.out.println("可写");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
