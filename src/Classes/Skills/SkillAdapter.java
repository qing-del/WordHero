package Classes.Skills;

import Classes.Element;
import Rule.Versus;

import java.io.Serial;

/**
 * 作为适配器中转技能需要重写的方法进行空实现
 */
public class SkillAdapter extends Skill{
    @Serial
    private static final long serialVersionUID = 2213875036793057649L;

    @Override
    public int getAttack() {
        return 0;
    }

    @Override
    public int getDefense() {
        return 0;
    }

    @Override
    public Element getElement() {
        return null;
    }

    @Override
    public void takeDamage(int damage) {

    }

    @Override
    public int attack(Versus attacker, Versus target) {
        return 0;
    }

    @Override
    public int getMaxHp() {
        return 0;
    }

    @Override
    public int getMaxMp() {
        return 0;
    }

    @Override
    public int getCurrentHp() {
        return 0;
    }

    @Override
    public int getCurrentMp() {
        return 0;
    }

    @Override
    public void setCurrentHp(int i) {

    }

    @Override
    public void setCurrentMp(int i) {

    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public boolean use(Versus user, Versus target) {
        return false;
    }
}
