package UI.FunctionUI;

import Data.AccountData;
import UI.MainUI.LoginJFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterJFrame extends JFrame implements ActionListener {
    private final LoginJFrame loginFrame; // 保存登录窗口的引用
    JTextField userField = new JTextField();
    JPasswordField passField = new JPasswordField();
    JPasswordField confirmPassField = new JPasswordField();
    JButton registerButton = new JButton("注册");

    // 添加密码显示状态标识
    private boolean isPasswordVisible = false;
    private JButton togglePasswordBtn = new JButton("显示");

    /**
     * 创建注册窗口
     * @param loginFrame 传入登录窗口的引用地址
     */
    public RegisterJFrame(LoginJFrame loginFrame) {
        this.loginFrame = loginFrame;
        initRegisterJFrame();
        initComponents();
        this.setVisible(true);
    }

    private void initRegisterJFrame() {
        this.setSize(640, 380);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setTitle("注册界面");

        // 添加窗口监听器
        this.addWindowListener(new WindowAdapter() {@Override public void windowClosed(WindowEvent e) {loginFrame.setVisible(true); }});    // 关闭注册窗口时显示登录窗口
    }

    /**
     * 初始化注册窗口框架内的内容
     */
    private void initComponents() {
        // 用户名标签和输入框
        JLabel userLabel = new JLabel("用户名:");
        userLabel.setBounds(100, 50, 100, 30);
        userField.setBounds(230, 50, 280, 30);
        this.add(userLabel);
        this.add(userField);

        // 密码标签和输入框
        JLabel passLabel = new JLabel("密码:");
        passLabel.setBounds(100, 100, 100, 30);
        passField.setBounds(230, 100, 280, 30);
        this.add(passLabel);
        this.add(passField);

        // 确认密码标签和输入框
        JLabel confirmPassLabel = new JLabel("确认密码:");
        confirmPassLabel.setBounds(100, 150, 100, 30);
        confirmPassField.setBounds(230, 150, 280, 30);
        this.add(confirmPassLabel);
        this.add(confirmPassField);

        // 注册按钮
        registerButton.setBounds(250, 220, 140, 40);
        registerButton.addActionListener(this);
        this.add(registerButton);

        //新增密码显示按钮
        // 添加密码显示切换按钮
        togglePasswordBtn.setBounds(510, 100, 70, 30);
        togglePasswordBtn.addActionListener(e -> {
            // 切换密码显示状态
            isPasswordVisible = !isPasswordVisible;
            passField.setEchoChar(isPasswordVisible ? (char)0 : '•'); // 使用0显示明文
            confirmPassField.setEchoChar(isPasswordVisible ? (char)0 : '•');
            togglePasswordBtn.setText(isPasswordVisible ? "隐藏" : "显示");

            System.out.println(isPasswordVisible ? "显示密码" : "隐藏密码");

            // 保持输入焦点在密码框
            passField.requestFocusInWindow();
        });
        this.add(togglePasswordBtn);
    }

    /**
     * 提示弹窗
     * @param message 需要提示的内容
     */
    private void showPrompt(String message) {
        JOptionPane.showMessageDialog(this, message, "提示", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = userField.getText();
        String password = new String(passField.getPassword());
        String confirmPassword = new String(confirmPassField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showPrompt("用户名和密码不能为空！");
        } else if (!password.equals(confirmPassword)) {
            showPrompt("两次输入的密码不一致！");
        } else if (AccountData.find(username) != -1) {
            showPrompt("用户名已存在！");
        } else if (username.length() < 6) {
            showPrompt("用户名长度不能小于6！");
        } else if (password.length() < 6) {
            showPrompt("用户密码长度不能小于6！");
        }
        else {
            AccountData.addAccount(username, password); //将账号密码添加进账号管数据中
            AccountData.saveFile(); //调用保存文件的函数（放在这里防止程序崩溃时账号数据丢失）
            showPrompt("注册成功！");
            this.dispose();
        }
    }
}