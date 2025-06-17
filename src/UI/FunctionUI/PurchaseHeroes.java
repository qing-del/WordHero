package UI.FunctionUI;

import Players.Player;
import Players.PlayerDataManager;
import UI.MainUI.GameJFrame;
import Classes.Heroes.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * 购买英雄的界面
 */
public class PurchaseHeroes extends JFrame{
    Player player;
    public PurchaseHeroes(Player player) {
        this.player=player;
        initWindow();   //初始化窗口
        initButton();
        this.setVisible(true);
    }

    private void initWindow() {
        this.setSize(400, 300);
        this.setLayout(new GridLayout(0, 1)); // 垂直排列按钮
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("请选择需要购买的英雄：");
        this.setLocationRelativeTo(null);
        this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
    }

    private void initButton() {
        JButton EarthGuardian = createHeroButton("大地守护", new EarthGuardian("大地守护"));
        JButton ForestDruid = createHeroButton("森林德鲁伊", new ForestDruid("森林德鲁伊"));
        JButton Gunslinger = createHeroButton("神枪手", new Gunslinger("神枪手"));
        JButton Mage = createHeroButton("魔法师", new Mage("魔法师"));
        JButton SwordImmortal = createHeroButton("剑仙", new SwordImmortal("剑仙"));

        this.add(EarthGuardian);
        this.add(ForestDruid);
        this.add(Gunslinger);
        this.add(Mage);
        this.add(SwordImmortal);
    }

    private JButton createHeroButton(String heroName, Hero heroInstance) {
        int neededCoin = (int)(3000 * new Random().nextDouble(0.6,1.8));
        JButton btn = new JButton(heroName + "  需要：" + neededCoin);
        btn.addActionListener(e->{
            int confirm = JOptionPane.showConfirmDialog(this,
                    "是否要购买这个英雄？",
                    "二次确认",
                    JOptionPane.YES_NO_OPTION);

            if(confirm == JOptionPane.YES_OPTION)
                if(player.getCoins() >= neededCoin) {
                    JOptionPane.showMessageDialog(this,
                            "购买成功！",
                            "通知：",
                            JOptionPane.INFORMATION_MESSAGE);

                    player.addHero(heroInstance);
                    player.gainCoin(-neededCoin);
                    player.gameJFrame.updatePanel();    //更新玩家指定的面板
                    //自动保存玩家信息
                    try { PlayerDataManager.savePlayer(player); } catch (IOException ex) { ex.printStackTrace(); }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "购买失败：硬币不足！",
                            "通知：",
                            JOptionPane.INFORMATION_MESSAGE);
                }
        });
        return btn;
    }
}
