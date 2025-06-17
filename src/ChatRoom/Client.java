package ChatRoom;

import Players.Player;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client extends Thread{
    class ReceiveMess implements Runnable{
        Socket socket;
        BufferedReader br;

        public ReceiveMess(Socket socket, BufferedReader br) {
            this.socket = socket;
            this.br = br;
        }

        @Override
        public void run() {
            try {
                String mess;
                //修改循环条件
                while(!Thread.currentThread().isInterrupted() && (mess=br.readLine()) != null) {
                    chatJFrame.appendMessage(mess);
                }
            } catch (IOException e) {
                System.err.println("读取信息失效！\n这个程序已退出服务器 故无法读取信息！");
            } finally {
                Thread.currentThread().interrupt();
            }
        }
    }

    ExecutorService pool = Executors.newFixedThreadPool(2);
    Player player;
    ChatJFrame chatJFrame;
    BufferedWriter bw;
    Socket socket;
    BufferedReader br;
    public Client(Player player) throws IOException {
        this.player=player;
        socket = new Socket("127.0.0.1", 10000);
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        chatJFrame = new ChatJFrame(this);

        bw.write(player.getName());
        bw.newLine();
        bw.flush();

        pool.submit(new ReceiveMess(socket, br));
    }


    public void exitChat() {
        try {
            //先发送关闭指令
            if (bw != null) {
                bw.write("/exit");
                bw.newLine();
                bw.flush();
            }

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            pool.shutdown();

            if (br != null) br.close();

            pool.close();
        } catch (IOException ex) {
            System.err.println("关闭客户端资源时出错: " + ex.getMessage());
        } finally {
            bw = null;
            br = null;
            socket = null;
        }
    }

    public void sendMessage(String message) {
        try {
            bw.write(message);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            System.out.println(player.getName() + "发送信息失败！");
            chatJFrame.appendMessage("[" + message + "] 该信息发送失败！");
            chatJFrame.dispose();
            JOptionPane.showMessageDialog(
                    chatJFrame,
                    "你已断开连接，发送信息失败！",
                    "错误",
                    JOptionPane.ERROR_MESSAGE
            );
            exitChat();
        }
    }
}