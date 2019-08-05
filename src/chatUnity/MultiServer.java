package chatUnity;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer {
    private ArrayList<MultiServerThread> list;
    private Socket socket;

    public MultiServer() throws IOException {

        list = new ArrayList<MultiServerThread>();//사용자 목록 객체
        ServerSocket serverSocket = new ServerSocket(1771);//서버(소켓) 생성
        MultiServerThread mst = null;

        boolean isStop = false;
        while (!isStop) {//true
            System.out.println("Server ready...");
            socket = serverSocket.accept();//서버 받음
            System.out.println("start");
            mst = new MultiServerThread(this);
            list.add(mst);
            Thread t = new Thread(mst);
            t.start();
        }
    }

    public ArrayList<MultiServerThread> getList() {
        return list;
    }

    public Socket getSocket() {
        return socket;
    }

    public static void main(String arg[]) throws IOException {
        new MultiServer();
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 밑에는 서버쓰레드
class MultiServerThread implements Runnable {
    private Socket socket;
    private MultiServer ms;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;


    public MultiServerThread(MultiServer ms) {
        this.ms = ms;//서버 정보 받아옴
    }


    public synchronized void run() {
        boolean isStop = false;
        try {
            socket = ms.getSocket();
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());//i/o stream 객체생성

            String message = null;
            while (!isStop) {
                message = (String) ois.readObject();
                String[] str = message.split("#");
                if (str[1].equals("exit")) {
                    broadCasting(message);
                }else {
                    broadCasting(message);
                }

            }

            ms.getList().remove(this);
            System.out.println(socket.getInetAddress() + "정상적으로 종료하셨습니다");
            System.out.println("list size : " + ms.getList().size());
        } catch (Exception e) {
            ms.getList().remove(this);
            System.out.println(socket.getInetAddress() + "비정상적으로 종료하셨습니다");
            System.out.println("list size : " + ms.getList().size());
        }
    }

    public void broadCasting(String message) throws IOException {//모두에게 뿌려주는
        for (MultiServerThread ct : ms.getList()) {
            ct.send(message);
        }
    }

    public void send(String message) throws IOException {//출력용
        oos.writeObject(message);
    }

}