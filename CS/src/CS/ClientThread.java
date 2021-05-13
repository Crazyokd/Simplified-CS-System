package CS;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ClientThread implements Runnable{
    private boolean flag;
    private Socket socket;
    private String userName;
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private boolean isOffLine=true;

    public void begin(){
        //获取一个与服务器对应的socket
        try {
            //10.161.59.209   192.168.43.215
            socket = new Socket(InetAddress.getByName("127.0.0.1"),8888);
            this.isOffLine=false;
        } catch (IOException e) {
            System.out.println("未连接服务器，Socket获取失败，将强制下线");
            //e.printStackTrace();
        }

        try {
            //写入自己要发送的群聊消息
            new Thread(this).start();
            while (!this.isOffLine) {
                //读取服务器消息并打印在控制台上
                readOtherMessage();
            }
        }finally {
            try {
                if(socket!=null)
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (!this.isOffLine) {
            //System.out.println("进入run方法");
            Scanner scanner = new Scanner(System.in);
            //指定一个用户名(用户名中不允许有空格)
            if(!flag){
                System.out.println("请输入您的用户名(用户名中不允许有空格)");
                flag=true;
            }else {
                //将分隔符设为'\n'
                scanner.useDelimiter("\n");
            }
            //获取客户端从键盘输入的消息
            String str = scanner.next();
            sendMessage(str);
        }
    }

    public void readOtherMessage(){
        InputStream is= null;
        byte b[]=new byte[64];
        int len;
        StringBuffer sb=new StringBuffer();
        try {
            is = socket.getInputStream();
            while((len=is.read(b))!=-1) {
                sb.append(new String(b, 0, len));
                if(sb.indexOf("*||*")!=-1)break;
            }
            System.out.println(sb.delete(sb.length()-4,sb.length()));
        } catch (SocketException e) {
            System.out.println("服务器未开启，将强制下线");
            this.isOffLine=true;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String str){
        if(this.userName!=null) {
            String s= sdf.format(new Date())+" "+userName+":"+str+"*||*";
            OutputStream os=null;
            try {
                os=this.socket.getOutputStream();
                os.write(s.getBytes(StandardCharsets.UTF_8));
                os.flush();
            } catch (IOException e) {
                System.out.println("已断开与服务端的连接，无法发送消息(仅提示一次！)");
            }
        }else{
            userName=str;
        }
    }
}
