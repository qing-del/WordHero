package Rule;

import Classes.Element;

/**
 * 接入这个接口表示可以进行战斗
 */
public interface Versus {
    int getAttack();
    int getDefense();
    Element getElement();
    void takeDamage(int damage);
    int attack(Versus attacker, Versus target);
    int getMaxHp();
    int getMaxMp();
    int getCurrentHp();
    int getCurrentMp();
    void setCurrentHp(int i);
    void setCurrentMp(int i);
    int getLevel();

//    //发动攻击函数
//    default int attack(Versus attacker, Versus attackered) {
//        return attacker.getAttack() - attacker.getDefence();
//    }

}
