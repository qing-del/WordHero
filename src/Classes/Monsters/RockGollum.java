// 文件：Classes/Monsters/RockGolem.java
package Classes.Monsters;

import Classes.Element;
import Rule.Versus;

import java.io.Serial;
import java.util.Random;

/**
 * 岩石魔像 - 坚固的石质守卫
 * 元素属性：土
 * 等级范围：areaLevel 到 areaLevel+1
 */
public class RockGollum extends MonsterAdapter {
    @Serial
    private static final long serialVersionUID = 7840534747777892581L;
    static double CriticalHitRate = 0.1;         // 暴击率较低
    static double CriticalHitMultiplier = 2.0;   // 暴击伤害较高

    public RockGollum(int level) {
        super("岩石魔像", level, Element.EARTH);
    }

    @Override
    public int attack(Versus attacker, Versus target) {
        double elementMultiplier = Element.getMultiplier(this.element, target.getElement());
        // 岩石魔像偏重防御型攻击
        int damage = (int)((this.attack * 0.5 + level * 1.0) * elementMultiplier)
                - target.getDefense()/2;
        damage = (int)(damage * (Trigger(CriticalHitRate) ? CriticalHitMultiplier : 1.0));
        damage = Math.max(damage, 1);
        target.takeDamage(damage);
        return damage;
    }

    /**
     * 生成随机等级的岩石魔像
     * @param areaLevel 区域等级
     * @return RockGolem 实例
     */
    public static RockGollum generateRockGolem(int areaLevel) {
        int min = Math.max(1, areaLevel);
        int max = areaLevel + 1;
        int lvl = new Random().nextInt(min, max + 1);
        return new RockGollum(lvl);
    }
}
