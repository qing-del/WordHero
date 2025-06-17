package Classes.NPC;

import Classes.Heroes.Hero;
import Players.Player;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class FatCat{
    static String name = "哆啦";
    static String state = "是主城的一直懒猫\n" +
            "由于平时特爱吃\n" +
            "所以看起来非常肥胖\n" +
            "但是它拥有前所未有的治疗能力\n" +
            "可以治疗你的所有英雄！";


    private FatCat(){}
    public static void use(Player player) {
        ArrayList<Hero> team = player.getTeam();
        if(team != null)
            for(Hero hero : team) {
                hero.setCurrentHp(hero.getMaxHp());
                hero.setCurrentMp(hero.getMaxMp());
            }
        JOptionPane.showMessageDialog(new JOptionPane(), "治疗完毕", "通知", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 获取npc图片的路径
     * SchoolHome\src\Classes\NPC\photo\FatCat\0.png
     * @return 返还置路径的文件类
     */
    public static String getPhoto() {
        return "src\\Classes\\NPC\\photo\\FatCat\\" + new Random().nextInt(0,2) + ".png";
    }

    public static String getState() {
        return state;
    }
}