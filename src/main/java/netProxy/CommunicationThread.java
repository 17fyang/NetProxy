package netProxy;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * ClassName: CommunicationThread
 * Description:
 * date: 2020/9/19 19:06
 *
 * @author :乌鸦坐飞机亠
 * @version:
 */
public class CommunicationThread implements Runnable {
    InputStream in=null;
    OutputStream out=null;
//    Flag flag=null;

    public CommunicationThread(InputStream in,OutputStream out){
        this.in=in;
        this.out=out;
    }


    public void run(){
        try{
            while (true){
                out.write(in.read());
            }
//            byte[] buf=new byte[1024];
//            int len=0;
//            while((len=in.read(buf))!=-1){
//                out.write(buf);
//            }
        }catch (Exception e){
//            e.printStackTrace();
        }
    }
}
