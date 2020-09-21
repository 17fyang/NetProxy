package nioDemo;

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
 * ClassName: demo
 * Description:
 * date: 2020/9/20 23:19
 *
 * @author :乌鸦坐飞机亠
 * @version:
 */
public class demo {
    public static void main(String[] args) throws Exception {
        new Thread(()->new Server().go()).start();
        Thread.sleep(1000);
        new Thread(()->new BioClient().go()).start();
    }
}

class Server{
    public static int PORT=8156;

    Selector selector=null;

    public Server() {
        try {
            // 创建channel并设置成非阻塞
            ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            //获取serverSocket并绑定端口
            ServerSocket serverSocket=serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(PORT));
            //注册channel到选择器,选择监听连接事件
            selector=Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT );
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void go() {
        try{
            // 开始检测和处理事件
            while(selector.select() > 0){
                Iterator<SelectionKey> it=selector.selectedKeys().iterator();

                //开始轮询注册的服务器端channel（其实只注册了一个）
                while(it.hasNext()){
                    SelectionKey key=it.next();

                    //remove是手动把该channel从已选择的key中清掉，以便被再次选择，结合selector.select()原理
                    it.remove();

                    //有连接请求了
                    if(key.isAcceptable()){
                        System.out.println("服务端收到一个连接请求");
                        ServerSocketChannel serverSocketChannel= (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel=serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                    }
                    //有消息发过来了
                    if(key.isReadable()){
                        System.out.println("服务端收到客户端一个消息");
                        SocketChannel socketChannel= (SocketChannel) key.channel();
                        ByteBuffer byteBuffer=ByteBuffer.allocateDirect(1024);
                        byteBuffer.clear();
                        socketChannel.read(byteBuffer);
                        byteBuffer.flip();

                        String receiveData= StandardCharsets.UTF_8.decode(byteBuffer).toString();
                        System.out.println("receiveData:"+receiveData);
                    }


                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}


class BioClient{
    public void go(){
        try {
            SocketChannel clientSocketChannel=SocketChannel.open();
            clientSocketChannel.connect(new InetSocketAddress("127.0.0.1",Server.PORT));
            ByteBuffer byteBuffer=ByteBuffer.allocateDirect(1024);
            byteBuffer.put("123456".getBytes());
            byteBuffer.flip();
            clientSocketChannel.write(byteBuffer);
//            byteBuffer.compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}