package CS;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class SocketThread implements Runnable {
private Socket socket;
private boolean isOffline;
//public static

public SocketThread(){}

public SocketThread(Socket socket){
    this.socket=socket;
}

    @Override
    public void run() {
    while(!this.isOffline) {
        try {
            InputStream is = this.socket.getInputStream();
            String str=readClientMessage(is);
            if(!str.equals(new String())) {
                for (int i = 0; i < Server.als.size(); i++) {
                    Socket tmpSocket = Server.als.get(i);
                    if (!tmpSocket.equals(this.socket))
                        sendMessage(tmpSocket.getOutputStream(), str);
                }
                System.out.println("服务端发送完毕");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    public String readClientMessage(InputStream is){
        System.out.println("进入服务端的readClientMessage方法");
        byte b[]=new byte[64];
        int len;
        StringBuffer sb=new StringBuffer();
        try {
            while ((len = is.read(b)) != -1) {
                sb.append(new String(b, 0, len));
                if (sb.indexOf("*||*") != -1) break;
            }
            System.out.println("服务端接收完毕");
        }catch (SocketException e){
            System.out.println("进行下线处理");
            proceedOffline();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return sb.toString();
        }
    }

    public void sendMessage(OutputStream os,String str){
    System.out.println("进入服务端的sendMessage方法");
        try {
            os.write(str.getBytes(StandardCharsets.UTF_8));
            os.flush();
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    public void proceedOffline(){
        Server.als.remove(this.socket);
        this.isOffline=true;
    }

}
