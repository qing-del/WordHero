package ChatRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatJFrame extends JFrame implements ActionListener {
    Client client;
    JButton sendMessage = new JButton("发送");
    JTextArea chatArea; // 提升为成员变量
    JTextField inputField; // 提升为成员变量

    public ChatJFrame(Client client) {
        this.client = client;
        initWindow();
        initChatUI();
        sendMessage.addActionListener(this);
        this.setVisible(true);
    }

    private void initChatUI() {
        // 聊天显示区域
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBounds(0, 0, 600, 400);
        this.add(scrollPane);

        // 输入框
        inputField = new JTextField();
        inputField.setBounds(0, 400, 600, 50);
        this.add(inputField);

        // 发送按钮
        sendMessage.setBounds(500, 450, 80, 40);
        this.add(sendMessage);
    }

    private void initWindow() {
        this.setSize(600,530);
        this.setLayout(null);
        this.setTitle("聊天室");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //放入玩家点击之后 广播该玩家退出聊天窗的信息
                System.out.println(client.player.getName() + "退出了聊天室！");
                client.interrupt(); //强制进入中断状态
                client.exitChat();
                dispose();
            }
        });
    }

    // 添加消息到聊天区域
    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            // 自动滚动到底部
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendMessage) {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                client.sendMessage(message); // 发送用户输入
                inputField.setText(""); // 清空输入框
            }
        }
    }

}


