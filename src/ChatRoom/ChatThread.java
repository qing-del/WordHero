package ChatRoom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDate;

/**
 * 专门给服务器使用的客户端线程监听
 */
public class ChatThread implements Runnable {
    Socket socket;
    BufferedWriter bw;
    String name;
    public ChatThread(Socket socket, BufferedWriter bw) {
        this.socket = socket;
        this.bw = bw;
    }


    @Override
    public void run() {
        try {
            System.out.println("已有用户连接" + socket.getInetAddress());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            name = br.readLine();

            Server.broadcast(name + " 加入了聊天室！", bw);

            String mess;
//            while(true) {
//                if(Thread.currentThread().isInterrupted()) throw new IOException(name + "退出！");
//                if((mess = br.readLine()).equals("/exit")) throw new IOException(name + "退出！");
//                if(mess == null) {
//                    Thread.sleep(500);
//                    continue;
//                }
//
//                LocalDate now = LocalDate.now();
//                String line = "[" + name + "]: " + mess + "   ---------" + now;
//                Server.broadcast(line, null);
//            }

            while((mess=br.readLine())!=null) {
                if(Thread.currentThread().isInterrupted()) throw new IOException(name + "退出！");
                if(mess.equals("/exit")) break;

                LocalDate now = LocalDate.now();
                String line = "[" + name + "]: " + mess + "   ---------" + now;
                Server.broadcast(line, null);
            }
        } catch (IOException e) {
            // 连接异常时也会广播退出消息
            System.out.println(name + " 连接服务器异常！");
        } /*catch (InterruptedException e) {
            System.out.println(name + " 进程睡眠异常！");
        }*/ finally {
            Server.broadcast(name + " 退出了聊天室", null);
            System.out.println("正在从服务器移除" + name + "的接口！");
            Server.remove(bw);
            System.out.println("移除成功！");

            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Thread.currentThread().interrupt(); //中断进程
        }
    }
}