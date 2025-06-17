package ChatRoom;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;


public class Server {
    //采用带有同步锁的集合
    static Set<BufferedWriter> set = ConcurrentHashMap.newKeySet();
//    static HashSet<BufferedWriter> set = new HashSet<>();
    static ThreadPoolExecutor pool = new ThreadPoolExecutor(
            8,
            16,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(8),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    //老旧的播报
    /*public static synchronized void broadcast(String mess, BufferedWriter excludeWriter) {
        Iterator<BufferedWriter> it = writers.iterator();
        while (it.hasNext()) {
            BufferedWriter writer = it.next();
            try {
                // 跳过已关闭的写入器
                if (writer == null) {
                    it.remove();
                    continue;
                }

                if (writer != excludeWriter) {
                    writer.write(mess);
                } else {
                    writer.write("你加入了聊天室！");
                }
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.out.println("广播失败！开始清除无效接口");
                it.remove();
                System.out.println("清除成功！");

                // 尝试关闭无效的写入器
                try {
                    writer.close();
                } catch (IOException ex) {
                    // 忽略关闭异常
                }
            }
        }
    }*/

    public static void broadcast(String mess, BufferedWriter exclude) {
        set.parallelStream().forEach(writer -> {
            try {
                if (writer != null) {
                    if (writer != exclude) {
                        writer.write(mess);
                    } else {
                        writer.write("你加入了聊天室！");
                    }
                    writer.newLine();
                    writer.flush();
                }
            } catch (IOException e) {
                set.remove(writer); // 自动移除失效连接
                try { writer.close(); } catch (IOException ignored) {}
            }
        });
    }

    public static synchronized void remove(BufferedWriter bufferedWriter) {
        set.remove(bufferedWriter);
    }

    public static void run() throws IOException {
        ServerSocket ss = new ServerSocket(10000);
        System.out.println("服务器已开启 等待用户连接...");

        try {
            while (true) {
                Socket socket = ss.accept();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                synchronized (set) {
                    set.add(bw);
                }
                pool.submit(new ChatThread(socket, bw));
            }
        } finally {
            ss.close();
            pool.shutdown();
        }
    }
}