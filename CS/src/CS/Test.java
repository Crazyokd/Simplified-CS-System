package CS;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        ClientThread ct=new ClientThread();
        ct.begin();

        }
}
//class TestThread extends Thread{
//    @Override
//    public void run(){
//        ClientThread ct=new ClientThread();
//        ct.begin();
//    }
//}