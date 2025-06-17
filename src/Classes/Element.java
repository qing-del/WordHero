package Classes;

import java.io.Serializable;

// 五行属性枚举
public enum Element implements Serializable {
    METAL, WOOD, WATER, FIRE, EARTH;

    /**
     * 属性克制计算
     * @param attacker 攻击方属性
     * @param defender 防御方属性
     * @return 克制倍数 (2.0=双倍 0.5=半伤 1.0=无克制)
     */
    public static double getMultiplier(Element attacker, Element defender) {
        // 金克木，木克土，土克水，水克火，火克金
        if ((attacker == METAL && defender == WOOD) ||
                (attacker == WOOD && defender == EARTH) ||
                (attacker == EARTH && defender == WATER) ||
                (attacker == WATER && defender == FIRE) ||
                (attacker == FIRE && defender == METAL)) {
            return 2.0;
        }
        return 1.0;
    }
}
