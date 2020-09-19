package netProxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * ClassName: ProxyDemo
 * Description:
 * date: 2020/9/19 18:13
 *
 * @author :乌鸦坐飞机亠
 * @version:
 */
public class ProxyDemo {
    public static void main(String[] args) throws IOException {
        ServerSocket proxyServer=new ServerSocket(8084);
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        while (true){
            Socket socket=proxyServer.accept();
             cachedThreadPool.execute(()->{
                try {
                    InputStream  clientIn = socket.getInputStream();
                    OutputStream clientOut=socket.getOutputStream();

                    //获取本次请求的信息
                    byte[] buf=new byte[1024];
                    int len=clientIn.read(buf);
                    if (len==-1)    return;
                    String[] targetInfo=getTarget(buf,len);
                    String method=targetInfo[0];
                    String host=targetInfo[1];
                    int port=Integer.parseInt(targetInfo[2]);

                    //代理操作
                    clientOut=new BlackNetFilter().filteByHost(clientOut,host);


                    if (clientOut!=null){
                        //代理连接
                        InetAddress netAddress = InetAddress.getByName(host);
                        Socket targetSocket=new Socket(netAddress.getHostAddress(),port);
                        InputStream targetIn=targetSocket.getInputStream();
                        OutputStream targetOut=targetSocket.getOutputStream();

                        //如果是https则先建立隧道，http直接连接
                        if ("CONNECT".equalsIgnoreCase(method)) {
                            clientOut.write("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
                            clientOut.flush();
                        } else {
                            targetOut.write(buf,0,len);
                        }

                        cachedThreadPool.execute(new CommunicationThread(targetIn,clientOut));
                        cachedThreadPool.execute(new CommunicationThread(clientIn,targetOut));

                    }
                } catch (IOException e) {
                    System.out.println("---error 连接超时");
//                    e.printStackTrace();
                }
            });
        }
    }

    public static String[] getTarget(byte[] buf,int len){
        String tcpInfo=new String(buf,0,len, StandardCharsets.UTF_8);
        String firstLine=tcpInfo.split("\n")[0];
        String method=firstLine.split(" ")[0];
        String host=firstLine.split(" ")[1];

        //提取网站地址和端口
        String[] temp=host.split("/");
        String domainAndPort=Stream.of(temp).filter(s->s.contains(".")).findFirst().get();

        String[] result=new String[3];
        result[0]=method;

        String arr[]=domainAndPort.split(":");
        if (arr.length==2){
            result[1]=arr[0];
            result[2]=arr[1];
        }else{
            result[1]=domainAndPort;
            result[2]="80";
        }
        return result;

    }
}