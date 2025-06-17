package UI.FunctionUI;

import Data.AccountData;
import UI.MainUI.LoginJFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChangePasswordJFrame extends JFrame implements ActionListener {
    private final LoginJFrame loginJFrame;
    JTextField usernameField = new JTextField();
    JPasswordField oldPassField = new JPasswordField();
    JPasswordField newPassField = new JPasswordField();
    JPasswordField confirmPassField = new JPasswordField();
    JButton confirmButton = new JButton("确认修改");

    // 添加密码显示状态标识
    boolean isPasswordVisible = false;
    JButton togglePasswordBtn = new JButton("显");

    public ChangePasswordJFrame(LoginJFrame loginJFrame) {
        initUI();   //初始化界面
        this.loginJFrame = loginJFrame;
        this.setVisible(true);
    }

    private void initUI() {
        this.setSize(405, 340);
        this.setTitle("修改密码");
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //输入用户名
        JLabel username = new JLabel("用户名");
        username.setBounds(50,30,80,30);
        usernameField.setBounds(140,30,200,30);
        this.add(username);
        this.add(usernameField);

        // 旧密码
        JLabel oldLabel = new JLabel("旧密码:");
        oldLabel.setBounds(50, 80, 80, 30);
        oldPassField.setBounds(140, 80, 200, 30);
        this.add(oldLabel);
        this.add(oldPassField);

        // 新密码
        JLabel newLabel = new JLabel("新密码:");
        newLabel.setBounds(50, 130, 80, 30);
        newPassField.setBounds(140, 130, 200, 30);
        this.add(newLabel);
        this.add(newPassField);

        // 确认密码
        JLabel confirmLabel = new JLabel("确认密码:");
        confirmLabel.setBounds(50, 180, 80, 30);
        confirmPassField.setBounds(140, 180, 200, 30);
        this.add(confirmLabel);
        this.add(confirmPassField);

        // 确认按钮
        confirmButton.setBounds(150, 250, 100, 30);
        confirmButton.addActionListener(this);
        this.add(confirmButton);

        // 添加密码显示切换按钮
        togglePasswordBtn.setBounds(340, 80, 50, 30);
        togglePasswordBtn.addActionListener(e -> {
            // 切换密码显示状态
            isPasswordVisible = !isPasswordVisible;
            oldPassField.setEchoChar(isPasswordVisible ? (char)0 : '•'); // 使用0显示明文
            newPassField.setEchoChar(isPasswordVisible ? (char)0 : '•'); // 使用0显示明文
            confirmPassField.setEchoChar(isPasswordVisible ? (char)0 : '•'); // 使用0显示明文
            togglePasswordBtn.setText(isPasswordVisible ? "隐" : "显");

            System.out.println(isPasswordVisible ? "显示密码" : "隐藏密码");
        });
        this.add(togglePasswordBtn);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loginJFrame.setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String oldPass = new String(oldPassField.getPassword());
        String newPass = new String(newPassField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());

        int index = AccountData.find(username);

        if(index == -1) {
            JOptionPane.showMessageDialog(this,"用户名不存在","错误",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!AccountData.match(index, oldPass)) {
            JOptionPane.showMessageDialog(this, "旧密码错误", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "两次输入的新密码不一致", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newPass.length() < 6) {
            JOptionPane.showMessageDialog(this, "密码长度不能小于6位", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(newPass.equals(oldPass)) {
            JOptionPane.showMessageDialog(this,"新密码和旧密码一致","错误",JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 更新密码
        AccountData.setPASS(index, newPass);
        AccountData.saveFile();
        JOptionPane.showMessageDialog(this, "密码修改成功");
        this.dispose();
    }
}