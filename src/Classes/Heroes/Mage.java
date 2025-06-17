package Classes.Heroes;

import Classes.Element;
import Classes.Skills.SkillAdapter;
import Rule.Versus;
import javax.swing.*;
import java.io.Serial;

/**
 * 魔法师 - 元素魔法掌控者
 * 技能随等级解锁：
 * Lv.1  火球术
 * Lv.5  冰霜新星
 * Lv.10 雷电风暴
 * Lv.15 元素融合
 */
public class Mage extends Hero {
    @Serial
    private static final long serialVersionUID = -4514211256803237848L;

    public Mage(String name) {
        super(name, Element.WATER);
        this.id=4L;
        skills.add(new Fireball());
        skills.add(new FrostNova());
        skills.add(new LightningStorm());
        skills.add(new ElementalFusion());
    }

    public Mage(long id, String name, int level, int maxHp, int currentHp, int maxMp, int currentMp, int attack, int defense, int growthRate, Element element, int exp, int maxExp) {
        super(id, name, level, maxHp, currentHp, maxMp, currentMp, attack, defense, growthRate, element, exp, maxExp);
        skills.add(new Fireball());
        skills.add(new FrostNova());
        skills.add(new LightningStorm());
        skills.add(new ElementalFusion());
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        unlockSkillsByLevel();
    }

    private void unlockSkillsByLevel() {
        if(level == 5) {
            showSkillUnlockDialog("冰霜新星");
        } else if(level == 10) {
            showSkillUnlockDialog("雷电风暴");
        } else if(level == 15) {
            showSkillUnlockDialog("元素融合");
        }
    }

    private void showSkillUnlockDialog(String skillName) {
        JOptionPane.showMessageDialog(null,
                "领悟新法术：" + skillName + "!",
                "魔法顿悟",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /*===== 技能实现 =====*/
    // 火球术 (Lv.1)
    static class Fireball extends SkillAdapter {
        public Fireball() {
            this.skillName = "火球术";
            this.costMp = 20;
            this.level=1;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getCurrentMp() < costMp) return false;

            double multiplier = Element.getMultiplier(Element.FIRE, target.getElement());
            int damage = (int)((user.getAttack() * 1.8 + user.getLevel() * 3) * multiplier);

            user.setCurrentMp(user.getCurrentMp() - costMp);
            target.takeDamage(damage);
            return true;
        }
    }

    // 冰霜新星 (Lv.5)
    static class FrostNova extends SkillAdapter {
        public FrostNova() {
            this.skillName = "冰霜新星";
            this.costMp = 40;
            this.level=5;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 5 || user.getCurrentMp() < costMp) return false;

            // 群体减速+伤害
            int damage = (int)(user.getAttack() * 1.2 + user.getLevel() * 2);
            target.takeDamage(damage);

            /*// 添加减速效果
            new Timer(5000, e ->
                    target.setCurrentHp(target.getCurrentHp() - (int)(damage * 0.3)
                    ).start());*/

            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }

    // 雷电风暴 (Lv.10)
    static class LightningStorm extends SkillAdapter {
        public LightningStorm() {
            this.skillName = "雷电风暴";
            this.costMp = 60;
            this.level=10;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 10 || user.getCurrentMp() < costMp) return false;

            // 三段雷击
            for(int i=0; i<3; i++) {
                int damage = (int)(user.getAttack() * 0.9
                        + user.getLevel() * 4);
                target.takeDamage(damage);
            }
            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }

    // 元素融合 (Lv.15)
    static class ElementalFusion extends SkillAdapter {
        public ElementalFusion() {
            this.skillName = "元素融合";
            this.costMp = 120;
            this.level=15;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 15 || user.getCurrentMp() < costMp) return false;

            // 随机三种元素伤害
            Element[] elements = {Element.FIRE, Element.WATER, Element.WOOD};
            for(Element elem : elements) {
                double multiplier = Element.getMultiplier(elem, target.getElement());
                int damage = (int)((user.getAttack() * 2 + user.getLevel() * 5) * multiplier);
                target.takeDamage(damage);
            }
            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }
}