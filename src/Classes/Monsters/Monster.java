package Classes.Monsters;

import Classes.Element;
import Rule.Critical_Strike;
import Rule.Versus;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

/**
 * 野怪基类
 * 包含野外生物的基础战斗属性
 */
public abstract class Monster implements Versus, Critical_Strike, Serializable {
    @Serial
    private static final long serialVersionUID = -7378691338171881011L;

    static int BASE_ATTACK = 15;    // 基础攻击力
    static int BASE_DEFENSE = 8;    // 基础防御力
    static double LEVEL_MODIFIER = 1.2; // 等级成长系数

    protected final String name;    // 野怪名称
    protected final int level;      // 野怪等级
    protected int attack;           // 攻击力
    protected int defense;          // 防御力
    protected int currentHp;        // 当前生命值
    protected final Element element;// 五行属性

    /**
     * 野怪构造函数
     * @param name 名称
     * @param level 等级（决定强度）
     * @param element 五行属性
     */
    public Monster(String name, int level, Element element) {
        this.name = name;
        this.level = level;
        this.element = element;
        calculateAttributes(); // 根据等级计算属性
        //血量计算公式：80 * （1.1 ^ 等级) + （等级 ~ 等级 + 2） * 5；
        this.currentHp = (int)(80 * Math.pow(1.1, level)) + new Random().nextInt(this.level + 2) * 5 ; // 基础血量随等级增长
    }

    // 根据等级计算攻防属性
    private void calculateAttributes() {
        //攻击力计算公式：基础攻击力 * （等级成长系数 ^ 等级）
        this.attack = (int)(BASE_ATTACK * Math.pow(LEVEL_MODIFIER, level));
        //防御力计算公式：基础防御力 * （等级成长系数 ^ 等级）
        this.defense = (int)(BASE_DEFENSE * Math.pow(LEVEL_MODIFIER, level));
    }

    /*----- Versus接口实现 -----*/
    @Override
    public int getAttack() { return attack; }

    @Override
    public int getDefense() { return defense; }

    @Override
    public Element getElement() { return element; }

    @Override
    public void takeDamage(int damage) {
        currentHp = Math.max(currentHp - damage, 0);
    }

    @Override
    public int attack(Versus attacker, Versus target) {
        double elementMultiplier = Element.getMultiplier(this.element, target.getElement());
        int damage = (int)((this.attack * 0.6 + level * 1.5) * elementMultiplier)
                - target.getDefense();
        damage = Math.max(damage, 1); // 保底伤害
        target.takeDamage(damage);
        return damage;
    }

    /*----- 辅助方法 -----*/
    public String getName() { return name; }

    public int getLevel() { return level; }

    public int getCurrentHp() { return currentHp; }

    public boolean isAlive() { return currentHp > 0; }

    @Override
    public String toString() { return name + " Lv." + level; }


    /**
     * 生成随机野怪
     * @param areaLevel 区域等级
     * @return 生成的野怪实例
    public static Monster generateRandomMonster(int areaLevel) {
        Random rand = new Random();
        Element[] elements = Element.values();
        String[] names = {"岩石傀儡", "火焰狼", "水精灵", "树妖", "金属守卫"};

        return new Monster(
                names[rand.nextInt(names.length)],
                Math.max(1, areaLevel + rand.nextInt(3) - 1), // 浮动等级
                elements[rand.nextInt(elements.length)]
        );
    }*/
}