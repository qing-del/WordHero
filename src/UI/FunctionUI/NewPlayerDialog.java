package UI.FunctionUI;

import Classes.Heroes.*;
import Players.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 新手引导对话框
 */
public class NewPlayerDialog extends JDialog {
    private final JTextField nameField = new JTextField(15);
    private final JComboBox<String> heroSelect = new JComboBox<>();
    private final Player newPlayer;

    public NewPlayerDialog(Player player) {
        this.newPlayer = player;
        initUI();
    }

    private void initUI() {
        setTitle("新手引导");
        setSize(300, 200);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setModal(true);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        // 玩家名称输入
        add(new JLabel("请输入你的冒险者名字:"));
        add(nameField);

        // 英雄选择
        add(new JLabel("选择你的初始英雄:"));
        heroSelect.addItem("剑仙（金属性近战）");
        heroSelect.addItem("神枪手（火属性远程）");
        heroSelect.addItem("魔法师（水属性法术）");
        heroSelect.addItem("大地守卫（土属性防御）");
        heroSelect.addItem("森林德鲁伊（木属性治疗）");
        add(heroSelect);

        // 确认按钮
        JButton confirmBtn = new JButton("开始冒险");
        confirmBtn.addActionListener(this::onConfirm);
        add(confirmBtn);
    }

    private void onConfirm(ActionEvent e) {
        newPlayer.setName(nameField.getText());
        Hero selectedHero = createSelectedHero();
        newPlayer.initializeNewPlayer(selectedHero);
        dispose();
    }

    private Hero createSelectedHero() {
        int index = heroSelect.getSelectedIndex();
        return switch (index) {
            case 0 -> new SwordImmortal("无名剑客");
            case 1 -> new Gunslinger("见习枪手");
            case 2 -> new Mage("魔法学徒");
            case 3 -> new EarthGuardian("岩石守卫");
            case 4 -> new ForestDruid("自然之子");
            default -> throw new IllegalArgumentException("无效选择");
        };
    }
}