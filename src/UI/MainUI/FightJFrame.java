package UI.MainUI;

import Classes.Heroes.Hero;
import Classes.Monsters.Monster;
import Players.*;
import Classes.Skills.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.*;

public class FightJFrame extends JFrame {
    GameJFrame gameJFrame;
    private Player player;
    private Hero playerHero;
    private Monster monster;
    private final JTextArea logArea = new JTextArea();
    private boolean playerTurn = true;  //表示是否为玩家回合

    public FightJFrame(ArrayList<Hero> team, Monster monster, Player player, GameJFrame gameJFrame) {
        this.gameJFrame=gameJFrame;
        //记录玩家
        this.player=player;
        // 选择出战英雄
        Hero selectedHero = selectHero(team);
        if (selectedHero == null) {
            JOptionPane.showMessageDialog(null, "必须选择出战英雄！");
            return;
        }

        this.playerHero = selectedHero;
        this.monster = monster;

        initWindow();
        initHeroInfo();
        initMonsterInfo();
        initSkillButtons();
        initLogArea();
        this.setVisible(true);
    }

    /**
     * 选择英雄出战
     * @param team 传入玩家拥有的英雄
     * @return 返回选择的英雄，或null
     */
    private Hero selectHero(ArrayList<Hero> team) {
        // 只保留当前HP>0的英雄
        List<Hero> aliveHeroes = new ArrayList<>();
        for (Hero h : team) {
            if (h.getCurrentHp() > 0) {
                aliveHeroes.add(h);
            }
        }
        if (aliveHeroes.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "没有可出战的英雄（所有英雄已濒死）！",
                    "提示",
                    JOptionPane.WARNING_MESSAGE
            );
            //gameJFrame.setVisible(true);
            return null;
        }

        Object[] options = aliveHeroes.stream()
                .map(h -> h.getName() + " (Lv." + h.getLevel() + ")")
                .toArray();
        int choice = JOptionPane.showOptionDialog(
                null,
                "选择出战英雄：",
                "英雄选择",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if(choice != -1) gameJFrame.setVisible(false);
        return choice == -1 ? null : aliveHeroes.get(choice);
    }

    private void initWindow() {
        this.setSize(800, 600);
        this.setLayout(null);
        this.setTitle("战斗界面");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                gameJFrame.updatePanel();
                gameJFrame.setVisible(true);
            }
        });
    }

    /**
     * 初始化展示英雄信息的界面
     */
    private void initHeroInfo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBounds(20, 20, 300, 150);

        panel.add(new JLabel("【"+playerHero.getName()+"】"));
        panel.add(new JLabel("等级: " + playerHero.getLevel()));
        panel.add(new JLabel("HP: " + playerHero.getCurrentHp() + "/" + playerHero.getMaxHp()));
        panel.add(new JLabel("MP: " + playerHero.getCurrentMp() + "/" + playerHero.getMaxMp()));

        this.add(panel);
    }

    /**
     * 初始化怪物信息的展示界面
     */
    private void initMonsterInfo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBounds(480, 20, 300, 150);

        panel.add(new JLabel("【"+monster.getName()+"】"));
        panel.add(new JLabel("等级: " + monster.getLevel()));
        panel.add(new JLabel("HP: " + monster.getCurrentHp()));

        this.add(panel);
    }

    /**
     * 初始化英雄技能的按钮
     */
    private void initSkillButtons() {
        JPanel skillPanel = new JPanel();
        skillPanel.setBounds(20, 200, 400, 300);

        // 普通攻击按钮
        JButton attackBtn = new JButton("普通攻击");
        attackBtn.setBounds(0, 0, 120, 30);
        attackBtn.addActionListener(this::handleAttack);
        skillPanel.add(attackBtn);

        // 动态生成技能按钮
        int y = 40;
        for (SkillAdapter skill : playerHero.getSkills()) {
            if (playerHero.getLevel() >= getUnlockLevel(skill)) {
                JButton btn = new JButton(skill.skillName);
                btn.setBounds(0, y, 120, 30);
                btn.addActionListener(e -> useSkill(skill));
                skillPanel.add(btn);
                y += 40;
            }
        }

        this.add(skillPanel);
    }

    /**
     * 获取技能使用等级的
     * @param skill 需要使用的技能
     * @return 返回解锁技能的等级
     */
    private int getUnlockLevel(SkillAdapter skill) {
        return skill.level;
    }

    private void initLogArea() {
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBounds(450, 200, 300, 300);
        this.add(scrollPane);
    }

    private void handleAttack(ActionEvent e) {
        if (!playerTurn) return;

        int damage = playerHero.attack(monster);
        log("玩家使用普通攻击，造成" + damage + "伤害！");
        if(checkBattleEnd()) return;
        playerTurn = false;
        monsterAttack();
        reLoadPanel();  //重载界面
    }

    private void useSkill(SkillAdapter skill) {
        if (!playerTurn) return;

        if (skill.use(playerHero, monster)) {
            log("玩家使用["+skill.skillName+"]，消耗"+skill.costMp+"MP");
            if(checkBattleEnd()) return;
            playerTurn = false;
            monsterAttack();
            reLoadPanel();  //重载界面
        } else {
            JOptionPane.showMessageDialog(this, "MP不足或等级不够！"
                    + (playerHero.getLevel() < getUnlockLevel(skill) ? "\n使用该技能需要" + skill.level : ""));
        }
    }

    private void monsterAttack() {
        int damage = monster.attack(monster, playerHero);
        log(monster.getName() + "反击，造成" + damage + "伤害！");
        if(checkBattleEnd()) return;
        playerTurn = true;
    }

    private boolean checkBattleEnd() {
        if (!playerHero.isAlive()) {
            JOptionPane.showMessageDialog(this, "战斗失败！");
            dispose();
        } else if (!monster.isAlive()) {
            int exp = Math.max(monster.getLevel() * new Random().nextInt(4,9) - playerHero.getLevel(), 1);
            long coin = monster.getLevel() * new Random().nextLong(5, 8);
            JOptionPane.showMessageDialog(this, "恭喜你\n" +
                    "战斗胜利！\n" +
                    "你的英雄获得了" + exp + "点经验\n" +
                    "你获得了" + coin + "个硬币");
            //玩家获得硬币
            player.gainCoin(coin);
            //调取获取经验函数
            playerHero.gainExp(exp);
            dispose();
        }

        if(!playerHero.isAlive() || !monster.isAlive())
            try {
                PlayerDataManager.savePlayer(player);
                return true;
            } catch (IOException e) {
                throw new RuntimeException("战斗保存玩家数据异常！");
            }

        return false;
    }

    /**
     * 重新加载面板
     */
    private void reLoadPanel() {
        this.getContentPane().removeAll();
        initHeroInfo();
        initMonsterInfo();
        initSkillButtons();
        initLogArea();
        this.revalidate();
        this.repaint();
    }

    private void log(String message) {
        logArea.append("[回合记录] " + message + "\n");
    }
}