package Data;

import Players.PlayerDataManager;
import UI.MainUI.LoginJFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdminPanelJFrame extends JFrame implements ActionListener {
    //保留启动该窗口的索引地址
    LoginJFrame loginJFrame;

    private JTable userTable;
    private final JButton refreshBtn = new JButton("刷新");
    private final JButton deleteBtn = new JButton("删除用户");
    private final JButton resetPassBtn = new JButton("重置密码");

    public AdminPanelJFrame(LoginJFrame loginJFrame) {
        this.loginJFrame = loginJFrame;
        initUI();
        loadUserData();
        this.setVisible(true);
    }

    private void initUI() {
        this.setSize(600, 400);
        this.setTitle("管理员面板");
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // 表格
        userTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(20, 20, 560, 250);
        this.add(scrollPane);

        // 按钮
        refreshBtn.setBounds(50, 300, 80, 30);
        deleteBtn.setBounds(250, 300, 100, 30);
        resetPassBtn.setBounds(400, 300, 100, 30);

        refreshBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        resetPassBtn.addActionListener(this);

        this.add(refreshBtn);
        this.add(deleteBtn);
        this.add(resetPassBtn);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                back();
            }
        });
    }

    /**
     * 用于返回登录界面
     */
    private void back() {
        this.dispose();
        System.out.println("正在返回登录界面...");
//        new LoginJFrame();
        loginJFrame.setVisible(true);   //重新设置可见
    }

    private void loadUserData() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"用户名", "密码（加密）"}, 0
        );

        for(int i=0; i<AccountData.getSize(); i++){
            model.addRow(new Object[]{
                    AccountData.getUser(i),
                    AccountData.getPass(i)
            });
        }
        userTable.setModel(model);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refreshBtn){
            loadUserData();
        }
        else if(e.getSource() == deleteBtn){
            int selectedRow = userTable.getSelectedRow();
            if(selectedRow == -1) return;

            JDialog jDialog = new JDialog();
            jDialog.setTitle("请二次确认");
            jDialog.setSize(300,200);
            jDialog.setLocationRelativeTo(null);
            jDialog.setLayout(null);    //取消默认布局
            jDialog.setAlwaysOnTop(true);   //设置默认置顶
            jDialog.setModal(true); //设置在上层不能对下层窗口进行操作
            jDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            JLabel tipText = new JLabel("是否确认删除这个账号数据？");
            tipText.setBounds(20,50,260,30);
            jDialog.add(tipText);

            JButton Yes = new JButton("确认");
            JButton No = new JButton("取消");
            Yes.setBounds(50,100,80,40);
            No.setBounds(150,100,80,40);
            Yes.addActionListener(ee -> {
                //先删除其玩家数据
                PlayerDataManager.delete(AccountData.getUser(selectedRow));

                //在数据账号数据仓库进行删除
                AccountData.removeUser(selectedRow);
                AccountData.removePass(selectedRow);
                AccountData.saveFile();
                loadUserData();
                //关闭窗口
                jDialog.dispose();
            });

            No.addActionListener(ee->{
                jDialog.dispose();
            });

            jDialog.add(Yes);
            jDialog.add(No);
            jDialog.setVisible(true);
        }
        else if(e.getSource() == resetPassBtn){
            int selectedRow = userTable.getSelectedRow();
            if(selectedRow == -1) return;

            String newPass = JOptionPane.showInputDialog("输入新密码");
            if(newPass != null && newPass.length() >= 6){
                AccountData.setPASS(selectedRow, newPass);
                AccountData.saveFile();
                loadUserData();
            }
        }
    }
}