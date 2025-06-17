package UI.MainUI;

import Data.AccountData;
import Data.AdminAccount;
import Data.AdminPanelJFrame;
import Players.Player;
import Players.PlayerDataManager;
import UI.FunctionUI.ChangePasswordJFrame;
import UI.FunctionUI.NewPlayerDialog;
import UI.FunctionUI.RegisterJFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

/**
 * 登录界面
 * 启动界面进入的第一界面
 */
public class LoginJFrame extends JFrame implements ActionListener{
    //创建登录界面的文本框
    JTextField userField = new JTextField();    //创建用户名输入框
    JPasswordField passField = new JPasswordField();    //创建用户密码输入框

    //创建登录和注册按钮
    JButton loginButton = new JButton("登录");
    JButton registerButton = new JButton("注册");

    // 添加密码显示状态标识
    boolean isPasswordVisible = false;
    JButton togglePasswordBtn = new JButton("显示");

    //添加修改密码的按钮
    JButton changePassword = new JButton("修改密码");

    public LoginJFrame() {
        LoginJFrameInit();  //登录框架初始化
        initTextField();    //初始化输入框
        initButton();   //初始化按钮和功能

        this.setVisible(true);
    }

    /**
     * 初始化登录和注册的功能
     */
    private void initButton() {
        //设置按钮大小
        loginButton.setSize(140,40);
        registerButton.setSize(140,40);
        //设置按钮位置和占地
        loginButton.setBounds(120,260,140,40);
        registerButton.setBounds(340,260,140,40);

        //初始化按钮的功能
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        //将按钮加入框架中
        this.add(loginButton);
        this.add(registerButton);

        //新增密码显示按钮
        // 添加密码显示切换按钮
        togglePasswordBtn.setBounds(510, 210, 70, 30);
        togglePasswordBtn.addActionListener(e -> {
            // 切换密码显示状态
            isPasswordVisible = !isPasswordVisible;
            passField.setEchoChar(isPasswordVisible ? (char)0 : '•'); // 使用0显示明文
            togglePasswordBtn.setText(isPasswordVisible ? "隐藏" : "显示");

            System.out.println(isPasswordVisible ? "显示密码" : "隐藏密码");

            // 保持输入焦点在密码框
            passField.requestFocusInWindow();
        });
        this.add(togglePasswordBtn);

        //新增进入修改密码的按钮
        changePassword.setBounds(510,160,100,30);
        changePassword.addActionListener(e -> {
            System.out.println("进入密码修改界面...");
            //进入修改密码的界面
            new ChangePasswordJFrame(this);
            //隐藏当前界面
            this.setVisible(false);
        });
        this.add(changePassword);
    }

    private void initTextField() {

        JLabel gameName = new JLabel(new ImageIcon("src\\ui\\source_material\\LoginTitle.jpg"));
        gameName.setSize(400,140);
        gameName.setBounds(100,0,400,140);
        this.add(gameName);

        //创建文字提示文本
        JLabel userLabel = new JLabel("User");
        JLabel passLabel = new JLabel("PassWord");
        //设置文本的位置和占用空间
        userLabel.setBounds(100,160,280,30);
        passLabel.setBounds(100,210,280,30);
        //将文本加入到框架中
        this.add(userLabel);
        this.add(passLabel);

        //设置输入框大小
        userField.setSize(300,30);
        passField.setSize(300,30);
        //设置输入框位置和占地空间
        userField.setBounds(230,160,280,30);
        passField.setBounds(230,210,280,30);

        //将输入框放入界面中
        this.add(userField);
        this.add(passField);
    }

    /**
     * 登录窗口基本属性初始化
     */
    private void LoginJFrameInit() {
        this.setSize(640,380); //设置登录界面的大小
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);   //设置登录界面的关闭模式
        this.setLocationRelativeTo(null);   //设置居中出现
        this.setLayout(null);   //取消默认布局
        this.setAlwaysOnTop(true);  //设置处于上层显示
        this.setTitle("登录界面");
    }

    /**
     * 创建提示框 提示用户哪里有问题
     * @param context 提示的内容
     */
    private void prompt(String context) {
        JDialog jDialog = new JDialog();
        jDialog.setSize(120,100);   //设置弹窗的大小
        jDialog.setTitle("警告：");    //设置弹窗的标题
        jDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); //设置弹窗的关闭模式
        jDialog.setModal(true); //设置弹窗出现时无法对下层界面进行操作
        jDialog.setAlwaysOnTop(true);   //设置永远置顶
        jDialog.setLocationRelativeTo(null);    //设置居中出现
        //设置提示文本内容
        JLabel tip = new JLabel(context);
        //将文本加入至提示框中
        jDialog.add(tip);
        //展示提示框
        jDialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == loginButton) {
            String user = userField.getText();
            String pass = new String(passField.getPassword());  //改用更安全的读取方式

            if(AdminAccount.check(user, pass)) {
                this.setVisible(false); //设置为不可见
                System.out.println("检测账号为管理员账号 正在进入管理模式...");
                JOptionPane.showMessageDialog(this,"欢迎进入账号管理界面","提示",JOptionPane.INFORMATION_MESSAGE);
                userField.setText("");  //清空用户输入内容
                passField.setText("");  //清空用户输入内容
                new AdminPanelJFrame(this);
                return;
            }

            int index;
            if(user.isEmpty() && pass.isEmpty())
                prompt("请输入账号和密码!");
            else if (user.isEmpty())
                prompt("请输入账号");
            else if (pass.isEmpty())
                prompt("请输入密码");
            else if((index= AccountData.find(user)) == -1) prompt("账号不存在");
            else if(!AccountData.match(index, pass)) prompt("密码错误");
            else {
                Player currentPlayer;
                try {
                    currentPlayer = PlayerDataManager.loadPlayer(user);
                } catch (IOException | ClassNotFoundException | ParseException ex) {
                    throw new RuntimeException(ex);
                }
                if (currentPlayer == null) {
                    currentPlayer = new Player(user, "");
                    new NewPlayerDialog(currentPlayer).setVisible(true);
                    try {
                        PlayerDataManager.savePlayer(currentPlayer);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                this.dispose();     //关闭当前窗口
                new GameJFrame(currentPlayer);
            }
        }
        else if (source == registerButton) {
            //先关闭此界面
            this.setVisible(false);
            System.out.println("进入注册界面...");
            //打开注册界面
            new RegisterJFrame(this);
        }
    }
}