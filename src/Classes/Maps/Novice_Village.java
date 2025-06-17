package Classes.Maps;

import Classes.Monsters.Goblin;

import java.io.Serial;
import java.util.Random;

/**
 * 新手村
 */
public class Novice_Village extends Map {
    @Serial
    private static final long serialVersionUID = 8149004459726342591L;

    public Novice_Village() {
        this.safe = false;  //设置为非安全区
        this.level=2;   //地图等级为2
    }

    @Override
    public String getName() { return "新手村";}


    /**
     * 随机生成一个怪物数量
     * 新手村最多有5只怪物
     */
    @Override
    public void generateMonsterNum() {
        this.monsterNum = rd.nextInt(1,5);
    }

    @Override
    public void generateMonster() {
        this.monsters.add(Goblin.generateGoblin(this.level));
    }
}