// 文件：Classes/Monsters/FireWolf.java
package Classes.Monsters;

import Classes.Element;
import Rule.Versus;

import java.io.Serial;
import java.util.Random;

/**
 * 火焰狼 - 野外常见怪物
 * 元素属性：火
 * 等级范围：areaLevel 到 areaLevel+2
 */
public class FireWolf extends MonsterAdapter {
    @Serial
    private static final long serialVersionUID = -8619251934207013511L;
    static double CriticalHitRate = 0.25;         // 暴击率
    static double CriticalHitMultiplier = 1.8;    // 暴击倍率

    public FireWolf(int level) {
        super("火焰狼", level, Element.FIRE);
    }

    @Override
    public int attack(Versus attacker, Versus target) {
        double elementMultiplier = Element.getMultiplier(this.element, target.getElement());
        int damage = (int)((this.attack * 0.7 + level * 2.0) * elementMultiplier)
                - target.getDefense();
        // 暴击判定
        damage = (int)(damage * (Trigger(CriticalHitRate) ? CriticalHitMultiplier : 1.0));
        damage = Math.max(damage, 1);
        target.takeDamage(damage);
        return damage;
    }

    /**
     * 生成随机等级的火焰狼
     * @param areaLevel 区域等级（即为最高等级限制）
     * @return FireWolf 实例
     */
    public static FireWolf generateFireWolf(int areaLevel) {
        int min = Math.max(1, areaLevel);
        int max = areaLevel + 2;
        int lvl = new Random().nextInt(min, max + 1);
        return new FireWolf(lvl);
    }
}