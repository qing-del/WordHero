package Classes.Monsters;

import Classes.Element;
import Rule.Versus;

import java.io.Serial;
import java.util.Random;

/**
 * 哥布林 - 新手村常见怪物
 * 元素属性：土
 * 等级范围：1-3级
 */
public class Goblin extends MonsterAdapter {
    @Serial
    private static final long serialVersionUID = 5069321754012795658L;
    static double CriticalHitRate = 0.2;    //哥布林默认暴击率
    static double CriticalHitMultiplier = 1.6;  //哥布林默认暴击倍率

    public Goblin(int level) {
        super("哥布林", level, Element.EARTH);
    }


    @Override
    public int attack(Versus attacker, Versus target) {
        double elementMultiplier = Element.getMultiplier(this.element, target.getElement());
        int damage = (int)((this.attack * 0.6 + level * 1.5) * elementMultiplier)
                - target.getDefense() ;
        damage = (int)(damage * (Trigger(CriticalHitRate) ? CriticalHitMultiplier : 1.0));
        damage = Math.max(damage, 1); // 保底伤害
        target.takeDamage(damage);
        return damage;
    }

    /**
     * 生成随机等级的哥布林
     * @param areaLevel 区域等级（即为限制的最高等级）
     * @return 哥布林实例
     */
    public static Goblin generateGoblin(int areaLevel) {
        int minLevel = Math.max(1, areaLevel);
        int maxLevel = areaLevel + 2;
        return new Goblin(new Random().nextInt(minLevel, maxLevel + 1));
    }
}