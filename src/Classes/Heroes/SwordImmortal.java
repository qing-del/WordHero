package Classes.Heroes;

import Classes.Element;
import Classes.Skills.SkillAdapter;
import Rule.Versus;

import javax.swing.*;
import java.io.Serial;

/**
 * 剑仙 - 金属性剑术大师
 * 技能随等级解锁：
 * Lv.1  剑诀
 * Lv.5  御剑术
 * Lv.10 剑气斩
 * Lv.15 万剑归宗
 */
public class SwordImmortal extends Hero {
    @Serial
    private static final long serialVersionUID = -1972883284378508558L;

    public SwordImmortal(String name) {
        super(name, Element.METAL);
        this.id = 5L;
        skills.add(new SwordArt());
        skills.add(new FlyingSword());
        skills.add(new SwordQiSlash());
        skills.add(new SwordRain());
    }

    public SwordImmortal(long id, String name, int level, int maxHp, int currentHp, int maxMp, int currentMp, int attack, int defense, int growthRate, Element element, int exp, int maxExp) {
        super(id, name, level, maxHp, currentHp, maxMp, currentMp, attack, defense, growthRate, element, exp, maxExp);
        skills.add(new SwordArt());
        skills.add(new FlyingSword());
        skills.add(new SwordQiSlash());
        skills.add(new SwordRain());
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        unlockSkillsByLevel(); // 每次升级检查技能解锁
    }

    // 根据等级解锁技能
    private void unlockSkillsByLevel() {
        if(level == 5) {
            showSkillUnlockDialog("御剑术");
        } else if(level == 10) {
            showSkillUnlockDialog("剑气斩");
        } else if(level == 15) {
            showSkillUnlockDialog("万剑归宗");
        }
    }

    private void showSkillUnlockDialog(String skillName) {
        JOptionPane.showMessageDialog(null,
                "领悟新技能：" + skillName + "!",
                "剑道突破",
                JOptionPane.INFORMATION_MESSAGE);
    }


    /*===== 技能实现 =====*/
    // 剑诀 (Lv.1)
    static class SwordArt extends SkillAdapter {
        public SwordArt() {
            this.skillName = "剑诀";
            this.costMp = 10;
            this.level=1;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getCurrentMp() < costMp) return false;  //蓝量不足使用失败

            int damage = (int)(user.getAttack() * 1.2
                    + user.getLevel() * 3);
            user.setCurrentMp(user.getCurrentMp() - costMp);
            target.takeDamage(damage);
            return true;    //使用成功
        }
    }

    // 御剑术 (Lv.5)
    static class FlyingSword extends SkillAdapter {
        public FlyingSword() {
            this.skillName = "御剑术·破空";
            this.costMp = 30;
            this.level=5;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 5 || user.getCurrentMp() < costMp) return false;

            // 金属性技能对木属性双倍伤害
            double multiplier = (target.getElement() == Element.WOOD) ? 2.0 : 1.5;
            int damage = (int)((user.getAttack() * 2 + user.getLevel() * 5) * multiplier);

            user.setCurrentMp(user.getCurrentMp() - costMp);
            target.takeDamage(damage);
            return true;    //使用成功
        }
    }

    // 剑气斩 (Lv.10)
    static class SwordQiSlash extends SkillAdapter {
        public SwordQiSlash() {
            this.skillName = "剑气纵横";
            this.costMp = 50;
            this.level=10;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 10 || user.getCurrentMp() < costMp) return false;

            // 对目标造成三段伤害
            for(int i=0; i<3; i++){
                int damage = (int)(user.getAttack() * 0.8
                        + user.getLevel() * 4);
                target.takeDamage(damage);
            }
            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;    //使用成功
        }
    }

    // 万剑归宗 (Lv.15)
    static class SwordRain extends SkillAdapter {
        public SwordRain() {
            this.skillName = "万剑归宗";
            this.costMp = 100;
            this.level=15;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 15 || user.getCurrentMp() < costMp) return false;

            // 大范围AOE攻击
            int baseDamage = user.getAttack() * 3
                    + user.getLevel() * 10;
            // 对金属性自身有30%吸血
            if(user.getElement() == Element.METAL) {
                user.setCurrentHp(user.getCurrentHp() + (int)(baseDamage * 0.3));
            }
            target.takeDamage(baseDamage);
            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }
}