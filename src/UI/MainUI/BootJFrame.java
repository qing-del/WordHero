package UI.MainUI;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * 用于表示游戏的后端窗口
 * 该窗口关闭会强制保存所有玩家的信息
 * 然后让所有游戏窗口都关闭
 */
public class BootJFrame extends JFrame {
    static ArrayList<GameJFrame> gameJFrames = new ArrayList<>(); //存储游戏窗口的地址

    public BootJFrame(){

        initWindow();
        initPoster();
        initJBtn();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                clear();
                System.out.println("强制退出成功！");
                System.exit(0); //强制退出虚拟机
            }
        });
        this.setVisible(true);
    }

    private void initJBtn() {
        JButton startGame = new JButton("开始游戏");
        startGame.addActionListener(e->{
            startGame();
        });
        startGame.setBounds(150,160,100,40);
        this.add(startGame);
    }

    private void initPoster() {
        JLabel poster = new JLabel(new ImageIcon("src\\ui\\source_material\\LoginTitle.jpg"));
        poster.setBounds(0,0,400,140);
        this.add(poster);
    }

    private void initWindow() {
        this.setSize(400,250);
        this.setLayout(null);   //采用自由布局
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);   //设置居中出现
        this.setTitle("启动界面");
    }

    public static void addGameJFrame(GameJFrame gameJFrame) {
        gameJFrames.add(gameJFrame);
    }

    /**
     * 先进行强制保存
     * 有概率导致游戏数据错乱
     */
    public void clear() {
        System.out.println("正在强制保存玩家的信息！");
        for (GameJFrame jFrame : gameJFrames) {
            jFrame.savePlayerDate();
            jFrame.dispose();
        }
        System.out.println("所有玩家数据保存成功！");
        System.out.println("正在进行强制退出！");
        dispose();
    }

    /**
     * 启动登录界面的函数
     */
    private void startGame() {
        new LoginJFrame();
    }

    public static void main(String[] args) {
        new BootJFrame();
    }
}
