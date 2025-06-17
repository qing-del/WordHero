package Classes.Maps;


import Classes.Monsters.Monster;
import Players.Player;
import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/*所有场景的父类
包含场景的基本属性*/
public abstract class Map implements Serializable {
    @Serial
    private static final long serialVersionUID = -79891805386210046L;

    public boolean safe;   //是否为安全区
    static Random rd = new Random();    //用于获取地图随机怪物
    static ArrayList<String> npc = new ArrayList<>();   //地图所拥有的npc
    int level;
    int monsterNum; //怪物的数量
    public ArrayList<Monster> monsters = new ArrayList<>(); //地图里面有的怪物

    public String getName() { return "地图"; }
    public void generateMonsterNum() {}
    public int getMonsterNum() { return this.monsterNum; }
    public int getLevel() { return this.level; }
    abstract public void generateMonster();  //生成怪物函数
    public void useNpc(int op, Player player) {}
    public String getNpcState(int op){ return null; }
    public String[] getNpcList(){
        return null;
    }
    public String getNpcPhotoPath(int op) { return null; }

    /**
     * npc：
     * 0：哆啦
     * 1：悟空
     */
    static {
        npc.add("哆啦");
        npc.add("悟空");
    }
}