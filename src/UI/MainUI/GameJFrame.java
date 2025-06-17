package UI.MainUI;

import ChatRoom.ChatJFrame;
import ChatRoom.Client;
import Players.*;
import Classes.Maps.*;
import Classes.Monsters.*;
import UI.ShowUI.HeroInformationPanel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;      //导入通用List容器
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameJFrame extends JFrame implements ActionListener {
    // 界面组件
    private JButton teamButton = new JButton("英雄团队");

    private List<JButton> dynamicButtons = new ArrayList<>();

    // 创建功能按钮
    JMenuItem changeMap = new JMenuItem("切换地图");
    JMenuItem saveGame = new JMenuItem("保存游戏");
    JMenuItem switchAccount = new JMenuItem("切换账号");
    JMenuItem logout = new JMenuItem("退出登录");
    JMenuItem exitGame = new JMenuItem("退出游戏");
    JMenuItem gmGame = new JMenuItem("GM模式");
    JMenuItem closeGM = new JMenuItem("关闭GM模式");
    JMenuItem changePlayerName = new JMenuItem("修改玩家名字");
    JMenuItem chatRoom = new JMenuItem("聊天室");

    //是否为管理员模式
    private Boolean gm;

    // 玩家数据
    Player player;
    Map currentMap;

    //线程池
    ExecutorService pool = Executors.newFixedThreadPool(3);


    public GameJFrame(Player player) {
        player.gameJFrame = this;
        this.player = player;
        this.currentMap = player.position; // 从玩家对象获取初始位置
        this.gm = false;    //默认为关闭
        initWindow();
        initPlayerInfo();
        initTeamButton();
        updateMapInfo();
        initMenuBar();          //初始化菜单
        initJButtonListener();  //提取出来添加功能
        this.setVisible(true);
        BootJFrame.addGameJFrame(this);   //添加至管理队列
    }

    private void initWindow() {
        this.setSize(1024, 800);
        this.setLayout(null);
        this.getContentPane().setLayout(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);    //后续更新了关闭模式
        this.setTitle("文字英雄（测试版） - 当前玩家：" + player.getName());
        this.setLocationRelativeTo(null);   //设置居中打开
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                savePlayerDate();
                dispose();
                if(source == switchAccount || source == logout){
                    source=null;
                    return;
                }
                System.exit(0);
            }
        });
    }

    //初始化玩家信息
    private void initPlayerInfo() {
        //先设置基本信息
        JLabel playerName = new JLabel("玩家名称：" + player.getName());
        JLabel playerDate = new JLabel("创建时间：" + new SimpleDateFormat("yyyy-MM-dd").format(player.getAccountCreationDate()));
        JLabel playerCoins = new JLabel("玩家硬币：" + player.getCoins());

        //设置位置和占地大小
        playerName.setBounds(24,20,300,30);
        playerDate.setBounds(24,60,300,30);
        playerCoins.setBounds(24,100,300,30);

        //加入到框架中
        this.add(playerName);
        this.add(playerDate);
        this.add(playerCoins);
    }

    // 英雄团队按钮
    private void initTeamButton() {
        teamButton.setBounds(20,680,200,40);
        this.add(teamButton);
    }

    private void initJButtonListener() {
        // 为功能按钮添加监听器
        changePlayerName.addActionListener(this);
        changeMap.addActionListener(this);  //切换地图
        saveGame.addActionListener(this);   //保存功能
        exitGame.addActionListener(this); // 直接退出程序
        switchAccount.addActionListener(this); // 占位功能
        logout.addActionListener(this);       // 占位功能
        teamButton.addActionListener(this);
        gmGame.addActionListener(this); //开启gm模式
        closeGM.addActionListener(this);
        chatRoom.addActionListener(this);
    }

    /**
     * 更新地图信息
     */
    private void updateMapInfo() {
        JLabel currentPosition = new JLabel("当前位置：" + currentMap.getName());
        JLabel isSateArea = new JLabel("是否安全：" + (currentMap.safe ? "是" : "否"));

        currentPosition.setBounds(512,20,300,30);
        isSateArea.setBounds(512,60,300,30);

        this.getContentPane().add(currentPosition);
        this.getContentPane().add(isSateArea);

        // 如果不是安全区就要加入怪物列表
        if (!currentMap.safe) {

            JLabel Monster_List = new JLabel("=== 怪物列表 ===");
            Monster_List.setBounds(610,100,250,30);
            this.getContentPane().add(Monster_List);

            currentMap.generateMonsterNum();
            int monsterNum = currentMap.getMonsterNum();
            for (int i = 0; i < monsterNum; i++) {
                currentMap.generateMonster();
            }

            //将活着的怪放入列表中
            List<Monster> aliveMonsters = new ArrayList<>();
            for (Monster m : currentMap.monsters) {
                if (m.isAlive()) {
                    aliveMonsters.add(m);
                }
            }

            int y = 130;
            for (int i = 0; i < aliveMonsters.size() && i < 10; i++) {
                Monster m = aliveMonsters.get(i);
                JButton btn = new JButton(m.toString());
                btn.setBounds(530, y + 30 * i, 250, 30);
                btn.addActionListener(ex -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "确定要挑战" + m.getName() + "（Lv." + m.getLevel() + "）吗？",
                            "战斗确认",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        startBattle(m);
                    }
                });
                dynamicButtons.add(btn);
                this.getContentPane().add(btn);
            }
        }
        //如果是安全区
        else {
            JLabel NPC_List = new JLabel("=== NPC列表 ===");

            NPC_List.setBounds(610,100,250,30);
            this.getContentPane().add(NPC_List);

            int y=130;
            String[] npcList = currentMap.getNpcList();
            for(int i=0;i<npcList.length;i++) {
                int op=i;
                ImageIcon npcPhoto = new ImageIcon(currentMap.getNpcPhotoPath(op));
                JButton npcButton = new JButton(npcList[i]);
                npcButton.setBounds(530,y + (30 * i),250,30);
                npcButton.addActionListener(ex->{
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            currentMap.getNpcState(op),
                            "功能说明",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            npcPhoto
                    );
                    if(confirm == JOptionPane.YES_OPTION)
                        currentMap.useNpc(op,player);
                });
                dynamicButtons.add(npcButton);
                this.getContentPane().add(npcButton);
            }
        }
    }

    /**
     * 切换地图函数
     * @param newMap 传入切换的地图
     */
    public void changeMap(Map newMap) {
        this.currentMap = newMap;
        this.player.position = newMap;

        updatePanel();
    }

    /**
     * 更新面板函数
     */
    public void updatePanel() {
        // 清理所有动态按钮的监听器
        for (JButton button : dynamicButtons) {
            for (ActionListener listener : button.getActionListeners()) {
                button.removeActionListener(listener);
            }
        }
        dynamicButtons.clear();

        this.getContentPane().removeAll(); // 清除所有组件
        initPlayerInfo();
        initTeamButton();
        updateMapInfo();
        initMenuBar();
        if(gm) initGM();    //启动gm部分的内容
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
        System.out.println("切换成功！");
    }

    /**
     * 旧版英雄团队信息展示
    private void showTeamDialog() {
        JDialog dialog = new JDialog(this, "英雄团队", true);
        dialog.setSize(400, 300);
        JTextArea teamInfo = new JTextArea();
        player.getTeam().forEach(hero -> {
            teamInfo.append("【"+hero.getName()+"】Lv."+hero.getLevel()+"\n");
            teamInfo.append("HP: "+hero.getCurrentHp()+"/"+hero.getMaxHp()+"\n");
            teamInfo.append("Exp: "+hero.getExp() + "/" + hero.getMaxExp()+"\n");
            teamInfo.append("战斗力: "+hero.getAttack()+"\n\n");
        });
        dialog.add(new JScrollPane(teamInfo));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }*/

    private void showTeamDialog() {
        new HeroInformationPanel(player, gm);
    }

    // 战斗启动方法（占位）
    private void startBattle(Monster monster) {
        JOptionPane.showMessageDialog(this,
                "即将进入战斗！\n对手：" + monster.getName() + " Lv." + monster.getLevel(),
                "战斗开始",
                JOptionPane.INFORMATION_MESSAGE
        );
        //this.setVisible(false);
        new FightJFrame(player.getTeam(), monster, player, this);
    }

    // 初始化菜单栏
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();           // 创建主菜单栏

        // 创建"功能"主菜单
        JMenu functionMenu = new JMenu("功能");
        JMenu mapMenu = new JMenu("地图");
        JMenu playerMenu = new JMenu("玩家");
        JMenu gmMenu = new JMenu("GM");
        JMenu chatMenu = new JMenu("频道");

        // 将功能按钮添加到子菜单
        mapMenu.add(changeMap);

        functionMenu.add(saveGame);
        functionMenu.add(switchAccount);
        functionMenu.add(logout);
        functionMenu.add(exitGame);

        playerMenu.add(changePlayerName);

        gmMenu.add(gmGame);
        gmMenu.add(closeGM);
        chatMenu.add(chatRoom);

        // 将主菜单添加到菜单栏
        menuBar.add(functionMenu);
        menuBar.add(playerMenu);
        menuBar.add(mapMenu);
        menuBar.add(gmMenu);
        menuBar.add(chatMenu);

        // 设置窗口的菜单栏
        this.setJMenuBar(menuBar);
    }

    public void savePlayerDate() {
        try {
            PlayerDataManager.savePlayer(player);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 启动GM模式的按钮
     */
    private void initGM() {
        //先添加一个修改硬币
        JButton changeCoins = new JButton("修改硬币");
        changeCoins.addActionListener(ex->{
            String newCoins = JOptionPane.showInputDialog(this,
                    "请输入要修改的硬币数：",
                    "修改硬币",
                    JOptionPane.INFORMATION_MESSAGE);
            if(newCoins==null) return;
            player.gainCoin(-player.getCoins());    //先清空玩家硬币
            player.gainCoin(Integer.parseInt(newCoins));    //再加载新硬币
            updatePanel();  //更新界面
            //自动保存玩家信息
            try { PlayerDataManager.savePlayer(player); } catch (IOException ee) { ee.printStackTrace(); }
        });
        changeCoins.setBounds(324,100,100,30);
        dynamicButtons.add(changeCoins);    //防止出现多个监听
        this.add(changeCoins);
    }

    Object source;
    @Override
    public void actionPerformed(ActionEvent e) {
        source = e.getSource();  //获取事件来源
        if(source == exitGame) {
            this.dispose(); //先关闭当前窗口
            System.out.println("正在关闭游戏！");
            System.exit(0); //再退出虚拟机
        }

        else if (source == switchAccount) {
            this.dispose(); //先关闭当前窗口
            System.out.println("正在切换账号...");
            new LoginJFrame();  //重新打开登录窗口
        }

        else if (source == logout) {
            this.dispose(); //先关闭当前窗口
            System.out.println("正在退出游戏并打开登录界面...");
            new LoginJFrame();  //打开登录界面
        }

        else if (source == saveGame) {
            savePlayerDate();
        }

        else if (source == changeMap) {
            new ChangeMapPanel(this);
        }

        else if (source == teamButton) {
            showTeamDialog();
        }

        else if (source == changePlayerName) {
            String newName = JOptionPane.showInputDialog(this,
                    "请输入要修改的名字：",
                    "修改玩家名字",
                    JOptionPane.INFORMATION_MESSAGE);
            if(newName==null) return;
            player.setName(newName);
            updatePanel();    //更新面板
            try { PlayerDataManager.savePlayer(player); } catch (IOException ex) { ex.printStackTrace(); }
        }

        else if (source == closeGM) {
            this.gm = false;
            updatePanel();  //更新面板
        }

        else if (source == gmGame) {
            String line = JOptionPane.showInputDialog(this,
                    "请输入GM作弊码",
                    "GM模式开关",
                    JOptionPane.INFORMATION_MESSAGE);
            if("Hpl love Yuke!".equals(line)) {
                this.gm=true;
                updatePanel();  //顺便更新盘的展示内容
            } else {
                JOptionPane.showMessageDialog(this,
                        "密语错误",
                        "警告",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        else if (source == chatRoom) {
            try {
                pool.submit(new Client(player));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        JOptionPane.getRootFrame(),
                        "无法连接至服务器！",
                        "错误",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}