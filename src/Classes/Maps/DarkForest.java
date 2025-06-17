// 文件：Classes/Maps/DarkForest.java
package Classes.Maps;

import Classes.Monsters.FireWolf;
import Classes.Monsters.RockGollum;

import java.io.Serial;

/**
 * 黑暗森林 - 野生怪物出没的区域
 * 非安全区，地图等级5，随机出现火焰狼和岩石魔像
 */
public class DarkForest extends Map {

    @Serial
    private static final long serialVersionUID = 8670013289084868847L;

    public DarkForest() {
        this.safe = false;
        this.level = 5;    // 地图基础等级
    }

    @Override
    public String getName() {
        return "黑暗森林";
    }

    /**
     * 随机生成怪物数量：3～6只
     */
    @Override
    public void generateMonsterNum() {
        this.monsterNum = rd.nextInt(3, 7);
    }

    /**
     * 每次调用随机生成一种怪物（火焰狼或岩石魔像）
     */
    @Override
    public void generateMonster() {
        int choice = rd.nextInt(2);
        if (choice == 0) {
            this.monsters.add(FireWolf.generateFireWolf(this.level));
        } else {
            this.monsters.add(RockGollum.generateRockGolem(this.level));
        }
    }
}
