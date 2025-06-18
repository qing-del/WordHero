package UI.ShowUI;

import Classes.Element;
import Classes.Heroes.*;
import Classes.Maps.ChangeMapPanel;

import javax.swing.*;
import java.awt.*;

/**
 * 展示传入英雄的所有基本信息
 */
public class HeroDetailedInformationUI extends JFrame {
    private final int informationWidth = 280;
    private final int informationHeight = 40;
    private final int informationX = 0;
    private final int informationY = 0;
    private final int btnX = informationWidth + informationX + 10;
    Hero hero;
    Boolean gm;
    JPanel heroBaseInformationPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    // 将标签提升为成员变量以便刷新
    private JLabel nameLabel;
    private JLabel levelLabel;
    private JLabel hpLabel;
    private JLabel mpLabel;
    private JLabel expLabel;
    private JLabel attackLabel;
    private JLabel defenseLabel;
    private JLabel growthRateLabel;
    private JLabel elementLabel;

    public HeroDetailedInformationUI(Hero hero, Boolean gm) {
        this.hero=hero;
        this.gm=gm;
        initWindow();
        initLabel();    //加载英雄信息
        initBtn();
        if(gm) initGMBtn();
        this.add(buttonPanel);
        this.setVisible(true);
    }

    private void initWindow() {
        this.setLayout(new BorderLayout());   //外框架
        this.setSize(450,420);
        heroBaseInformationPanel.setSize(280,400);
        heroBaseInformationPanel.setLayout(null);
        buttonPanel.setSize(150,400);
        buttonPanel.setLayout(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(hero.getName() + "--信息：");
        this.setLocationRelativeTo(null);   //设置居中打开
        this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);  //设置这个出现的时候不可以操作下层窗口
    }

    /**
     * 加载英雄基本信息
     */
    private void initLabel() {
        nameLabel = new JLabel("英雄名字：" + hero.getName());
        levelLabel = new JLabel("等级：" + hero.getLevel());
        hpLabel = new JLabel("Hp：" + hero.getCurrentHp() + "/" + hero.getMaxHp());
        mpLabel = new JLabel("Mp：" + hero.getCurrentMp() + "/" + hero.getMaxMp());
        expLabel = new JLabel("Exp：" + hero.getExp() + "/" + hero.getMaxExp());
        attackLabel = new JLabel("攻击力：" + hero.getAttack());
        defenseLabel = new JLabel("防御力：" + hero.getDefense());
        growthRateLabel = new JLabel("成长值：" + hero.getGrowthRate());
        elementLabel = new JLabel("五行属性：" + hero.getElement().name());

        nameLabel.setBounds(informationX,informationY,informationWidth,informationHeight);
        levelLabel.setBounds(informationX,informationY + 40,informationWidth,informationHeight);
        hpLabel.setBounds(informationX,informationY + 80,informationWidth,informationHeight);
        mpLabel.setBounds(informationX,informationY + 120,informationWidth,informationHeight);
        expLabel.setBounds(informationX,informationY + 160,informationWidth,informationHeight);
        attackLabel.setBounds(informationX,informationY + 200,informationWidth,informationHeight);
        defenseLabel.setBounds(informationX,informationY + 240,informationWidth,informationHeight);
        growthRateLabel.setBounds(informationX,informationY + 280,informationWidth,informationHeight);
        elementLabel.setBounds(informationX,informationY + 320,informationWidth,informationHeight);

        heroBaseInformationPanel.add(nameLabel);
        heroBaseInformationPanel.add(levelLabel);
        heroBaseInformationPanel.add(hpLabel);
        heroBaseInformationPanel.add(mpLabel);
        heroBaseInformationPanel.add(expLabel);
        heroBaseInformationPanel.add(attackLabel);
        heroBaseInformationPanel.add(defenseLabel);
        heroBaseInformationPanel.add(growthRateLabel);
        heroBaseInformationPanel.add(elementLabel);

        this.add(heroBaseInformationPanel);
    }

    /**
     * 刷新所有标签内容
     */
    private void refreshLabels() {
        nameLabel.setText("英雄名字：" + hero.getName());
        levelLabel.setText("等级：" + hero.getLevel());
        hpLabel.setText("Hp：" + hero.getCurrentHp() + "/" + hero.getMaxHp());
        mpLabel.setText("Mp：" + hero.getCurrentMp() + "/" + hero.getMaxMp());
        expLabel.setText("Exp：" + hero.getExp() + "/" + hero.getMaxExp());
        attackLabel.setText("攻击力：" + hero.getAttack());
        defenseLabel.setText("防御力：" + hero.getDefense());
        growthRateLabel.setText("成长值：" + hero.getGrowthRate());
        elementLabel.setText("五行属性：" + hero.getElement().name());
        this.setTitle(hero.getName() + "--信息："); // 更新窗口标题

        // 重新绘制界面
        heroBaseInformationPanel.revalidate();
        heroBaseInformationPanel.repaint();
    }

    /**
     * 加载正常按钮
     */
    private void initBtn () {
        //先添加修改名字的按钮
        JButton name = new JButton("修改名字");
        name.setBounds(btnX, informationY, 140, informationHeight);
        name.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(this,
                    "请输入英雄的新名字",
                    "修改名字",
                    JOptionPane.INFORMATION_MESSAGE);
            if(newName == null) return;
            hero.setName(newName);
            System.out.println("英雄名字修改成功！");
            refreshLabels(); // 修改后刷新
        });
        buttonPanel.add(name);
    }

    /**
     * 加载管理员按钮
     */
    private void initGMBtn () {
        initUpLevelBtn();   //初始化升级按钮
        initHpBtn();    //初始化修改血量按钮
        initMpBtn();    //初始化修改蓝量按钮
        initExpBtn();   //初始化修改经验值按钮
        initAttackBtn();    //攻击力
        initDefenseBtn();   //防御力
        initGrowthRateBtn();    //成长值
        initElementBtn();   //五行属性（未完成）
    }

    private void initElementBtn() {
        JButton elementBtn = new JButton("修改");
        elementBtn.addActionListener(e -> {
            new ChangeElementPanel(this, hero);
        });
        elementBtn.setBounds(btnX,
                informationY + informationHeight * 8,
                140,
                informationHeight);
        buttonPanel.add(elementBtn);
    }

    private void initGrowthRateBtn() {
        //成长率修改
        JButton growthRateBtn = new JButton("修改");
        growthRateBtn.addActionListener(e -> {
            String growthRate = JOptionPane.showInputDialog(this,
                    "请输入要修改的值：",
                    "当前成长值修改",
                    JOptionPane.INFORMATION_MESSAGE);
            if(growthRate==null) return;
            hero.setGrowthRate(Integer.parseInt(growthRate));
            refreshLabels(); // 修改后刷新
        });
        growthRateBtn.setBounds(btnX,
                informationY + informationHeight * 7,
                140,
                informationHeight);
        buttonPanel.add(growthRateBtn);
    }

    private void initDefenseBtn() {
        //修改防御力按钮
        JButton defenseBtn = new JButton("修改");
        defenseBtn.addActionListener(e -> {
            String defense = JOptionPane.showInputDialog(this,
                    "请输入要修改的值：",
                    "当前防御力修改",
                    JOptionPane.INFORMATION_MESSAGE);
            if(defense==null) return;
            hero.setDefense(Integer.parseInt(defense));
            refreshLabels(); // 修改后刷新
        });
        defenseBtn.setBounds(btnX,
                informationY + informationHeight * 6,
                140,
                informationHeight);
        buttonPanel.add(defenseBtn);
    }

    private void initAttackBtn() {
        //修改攻击力按钮
        JButton attackBtn = new JButton("修改");
        attackBtn.addActionListener(e -> {
            String attack = JOptionPane.showInputDialog(this,
                    "请输入要修改的值：",
                    "当前攻击力修改",
                    JOptionPane.INFORMATION_MESSAGE);
            if(attack==null) return;
            hero.setAttack(Integer.parseInt(attack));
            refreshLabels(); // 修改后刷新
        });
        attackBtn.setBounds(btnX,
                informationY + informationHeight * 5,
                140,
                informationHeight);
        buttonPanel.add(attackBtn);
    }

    private void initExpBtn() {
        //修改经验值按钮
        JButton expBtn = new JButton("修改");
        expBtn.addActionListener(e -> {
            String exp = JOptionPane.showInputDialog(this,
                    "请输入要修改的值：",
                    "当前经验值修改",
                    JOptionPane.INFORMATION_MESSAGE);
            if(exp==null) return;
            hero.setExp(Integer.parseInt(exp));
            refreshLabels(); // 修改后刷新
        });
        expBtn.setBounds(btnX,
                informationY + informationHeight * 4,
                140,
                informationHeight);
        buttonPanel.add(expBtn);
    }

    private void initMpBtn() {
        //初始化修改蓝量按钮
        JButton currentMpBtn = new JButton("修改");
        currentMpBtn.addActionListener(e -> {
            String currentMp = JOptionPane.showInputDialog(this,
                    "请输入要修改的值：",
                    "当前蓝量修改",
                    JOptionPane.INFORMATION_MESSAGE);
            if(currentMp==null) return;
            hero.setCurrentMp(Integer.parseInt(currentMp));
            refreshLabels(); // 修改后刷新
        });
        currentMpBtn.setBounds(btnX,
                informationY + informationHeight * 3,
                70,
                informationHeight);
        buttonPanel.add(currentMpBtn);

        //添加修改修量按钮
        JButton maxMpBtn = new JButton("修改");
        maxMpBtn.addActionListener(e -> {
            String maxMp = JOptionPane.showInputDialog(this,
                    "请输入要修改的值：",
                    "最大蓝量修改",
                    JOptionPane.INFORMATION_MESSAGE);
            if(maxMp==null) return;
            hero.setMaxMp(Integer.parseInt(maxMp));
            refreshLabels(); // 修改后刷新
        });
        maxMpBtn.setBounds(btnX + 70,
                informationY + informationHeight * 3,
                70,
                informationHeight);
        buttonPanel.add(maxMpBtn);
    }

    /**
     * 初始化修改血量的按钮
     */
    private void initHpBtn() {
        //添加修改修量按钮
        JButton currentHpBtn = new JButton("修改");
        currentHpBtn.addActionListener(e -> {
            String currentHp = JOptionPane.showInputDialog(this,
                    "请输入要修改的值：",
                    "当前血量修改",
                    JOptionPane.INFORMATION_MESSAGE);
            if(currentHp==null) return;
            hero.setCurrentHp(Integer.parseInt(currentHp));
            refreshLabels(); // 修改后刷新
        });
        currentHpBtn.setBounds(btnX,
                informationY + informationHeight * 2,
                70,
                informationHeight);
        buttonPanel.add(currentHpBtn);

        //添加修改修量按钮
        JButton maxHpBtn = new JButton("修改");
        maxHpBtn.addActionListener(e -> {
            String maxHp = JOptionPane.showInputDialog(this,
                    "请输入要修改的值：",
                    "最大血量修改",
                    JOptionPane.INFORMATION_MESSAGE);
            if(maxHp==null) return;
            hero.setMaxHp(Integer.parseInt(maxHp));
            refreshLabels(); // 修改后刷新
        });
        maxHpBtn.setBounds(btnX + 70,
                informationY + informationHeight * 2,
                70,
                informationHeight);
        buttonPanel.add(maxHpBtn);
    }

    /**
     * 初始化升级按钮
     */
    private void initUpLevelBtn() {
        //添加升级按钮
        JButton upLevel = new JButton("升级");
        upLevel.addActionListener(e -> {
            String needUpNum = JOptionPane.showInputDialog(this,
                    "请输入要升多少级：",
                    "升级模式",
                    JOptionPane.INFORMATION_MESSAGE);
            if(needUpNum==null) return;
            int need = Integer.parseInt(needUpNum);
            while(need-- != 0) {
                hero.gainExp(hero.getMaxExp());
            }
            refreshLabels(); // 升级后刷新
        });
        upLevel.setBounds(btnX,
                informationY + informationHeight,
                140,
                informationHeight);
        buttonPanel.add(upLevel);
    }

    // 提供公共刷新方法供其他类调用
    public void refresh() {
        refreshLabels();
    }
}

class ChangeElementPanel extends JFrame {
    HeroDetailedInformationUI heroUi;
    Hero hero;
    ChangeElementPanel(HeroDetailedInformationUI heroUi ,Hero hero) {
        this.heroUi=heroUi;
        this.hero=hero;
        initWindow();
        initBtn();
        this.setVisible(true);
    }

    private void initWindow() {
        this.setLayout(new FlowLayout());   //采用横向排布
        this.setSize(340,85);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("请选择要更改为的属性：");
        this.setLocationRelativeTo(heroUi);
        this.setAlwaysOnTop(true);
    }

    private void initBtn() {
        JButton Metal = createChangeElementBtn("金", Element.METAL);
        JButton Wood = createChangeElementBtn("木", Element.WOOD);
        JButton Water = createChangeElementBtn("水", Element.WATER);
        JButton Fire = createChangeElementBtn("火", Element.FIRE);
        JButton Earth = createChangeElementBtn("地", Element.EARTH);

        this.add(Metal);
        this.add(Wood);
        this.add(Water);
        this.add(Fire);
        this.add(Earth);
    }

    private JButton createChangeElementBtn(String name, Element ele) {
        JButton res = new JButton(name);
        res.setSize(40,40);
        res.addActionListener(e -> {
            hero.setElement(ele);
            heroUi.refresh(); // 通知主界面刷新
            dispose();
            JOptionPane.showMessageDialog(this,
                    "修改成功","通知,",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        return res;
    }
}