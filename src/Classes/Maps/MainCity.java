package Classes.Maps;

import Classes.Monsters.Goblin;
import Classes.NPC.FatCat;
import Classes.NPC.WuKong;
import Players.Player;

import java.io.File;
import java.io.Serial;
import java.util.Arrays;
import java.util.Random;

public class MainCity extends Map{
    @Serial
    private static final long serialVersionUID = 4247352599250733796L;
    public MainCity(){
        safe = true;
        this.monsterNum = new Random().nextInt(3,5);
        this.level = 1; //设置地图等级
    }

    @Override
    public String getName() { return "主城"; }

    @Override
    public String[] getNpcList() {
        String[] res = new String[2];
        res[0]=npc.get(0);  //获取哆啦
        res[1]=npc.get(1);  //获取悟空
        return res;
    }

    @Override
    public void generateMonster() {
        System.out.println("主城不会生成怪物！");
    }

    @Override
    public String getNpcState(int op) {
        switch (op) {
            case 0 : return FatCat.getState();
            case 1 : return WuKong.getState();
            default:throw new RuntimeException("npc使用越界");
        }
    }

    /**
     * 返回路径的字符串形式
     * @param op 选择的npc序号
     * @return 返回字符串路径
     */
    @Override
    public String getNpcPhotoPath(int op) {
        switch (op) {
            case 0: return FatCat.getPhoto();
            case 1: return WuKong.getPhoto();
            default:throw new RuntimeException("npc使用越界");
        }
    }

    @Override
    public void useNpc(int op, Player player) {
        switch (op) {
            case 0:
                FatCat.use(player);
                break;
            case 1:
                WuKong.use(player);
                break;
            default:throw new RuntimeException("npc使用越界");
        }
    }
}
