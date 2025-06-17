package Classes.Maps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import UI.MainUI.GameJFrame;

public class ChangeMapPanel extends JFrame {
    private final GameJFrame gameFrame;

    public ChangeMapPanel(GameJFrame gameFrame) {
        this.gameFrame = gameFrame;
        initWindow();
        initMapButtons();
        this.setVisible(true);
    }

    private void initWindow() {
        this.setSize(400, 300);
        this.setLayout(new GridLayout(0, 1)); // 垂直排列按钮
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("请选择地图：");
        this.setLocationRelativeTo(gameFrame);
        this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
    }

    private void initMapButtons() {
        // 创建地图按钮
        JButton mainCityBtn = createMapButton("主城", new MainCity());
        JButton noviceVillageBtn = createMapButton("新手村", new Novice_Village());
        JButton darkForestBtn = createMapButton("黑暗森林", new DarkForest());

        // 添加按钮到面板
        this.add(mainCityBtn);
        this.add(noviceVillageBtn);
        this.add(darkForestBtn);

        // 添加取消按钮
        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> dispose());
        this.add(cancelBtn);
    }

    private JButton createMapButton(String mapName, Map mapInstance) {
        JButton btn = new JButton(mapName);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.changeMap(mapInstance);
                dispose(); // 切换后自动关闭窗口
            }
        });
        return btn;
    }
}