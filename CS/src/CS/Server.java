package CS;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static ArrayList<Socket> als=new ArrayList<>();

    public static void main(String[] args) {
        //开启一个服务端的ServerSocket
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            Socket socket= null;
            try {
                socket = ss.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Server.als.add(socket);
            new Thread(new SocketThread(socket)).start();
        }
    }
}
