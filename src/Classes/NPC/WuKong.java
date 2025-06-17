package Classes.NPC;

import Players.Player;
import UI.FunctionUI.PurchaseHeroes;

import javax.swing.*;
import java.util.Random;

public class WuKong {
    static String name = "悟空";
    static String state = "是主城里面一直高颜值小猫\n" +
            "爱吃黑虎虾和羊肉\n" +
            "它被广泛的认识\n" +
            "你可以从它这里获取新的英雄！";

    private WuKong(){}

    public static void use(Player player) {
        new PurchaseHeroes(player);
    }


    public static String getPhoto() {
        return "src\\Classes\\NPC\\photo\\Wukong\\" + new Random().nextInt(1,3) + ".jpg";
    }

    public static String getState () { return state; }
}
