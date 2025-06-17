package UI.ShowUI;

import Classes.Heroes.*;
import Players.Player;
import javax.swing.*;
import java.awt.*;

public class HeroInformationPanel extends JFrame {
    Boolean gm;
    Player player;
    int JFrameHigh;
    public HeroInformationPanel(Player player) {
        this(player, false);
    }

    public HeroInformationPanel(Player player, Boolean gm) {
        this.player = player;
        this.gm=gm;
        JFrameHigh = 40 * player.getTeam().size() + 40;
        initWindow();
        initHeroButton();
        this.setVisible(true);
    }

    private void initWindow() {
        this.setSize(300, JFrameHigh);
        this.setLayout(new GridLayout());  //采用垂直排布
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(player.getName() + "--团队信息：");
        this.setLocationRelativeTo(null);   //设置居中打开
        this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);  //设置这个出现的时候不可以操作下层窗口
    }

    private void initHeroButton() {
        for(int i=0;i<player.getTeam().size();i++) {
            JButton heroBtn = createHeroButton(player.getTeam().get(i));
            this.add(heroBtn);
        }
    }

    private JButton createHeroButton(Hero hero) {
        JButton jbn = new JButton(hero.getName() + " Lv." + hero.getLevel());
        jbn.addActionListener(e->{
            dispose();
            new HeroDetailedInformationUI(hero, gm);
        });
        return jbn;
    }
}
