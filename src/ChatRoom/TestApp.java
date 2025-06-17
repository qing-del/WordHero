package ChatRoom;

import Players.Player;

import javax.swing.*;
import java.io.IOException;

public class TestApp {
    public static void main(String[] args) {
        try {
            Player player = new Player("Test", "Test");
            new Client(player);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    JOptionPane.getRootFrame(),
                    "无法连接至服务器！",
                    "错误",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
        }
    }
}
