package Classes.Heroes;

import Classes.Element;
import Classes.Skills.SkillAdapter;
import Rule.Versus;
import javax.swing.*;
import java.io.Serial;

/**
 * 神枪手 - 物理远程输出
 * 技能随等级解锁：
 * Lv.1  精准射击
 * Lv.5  快速连射
 * Lv.10 穿甲弹
 * Lv.15 死亡标记
 */
public class Gunslinger extends Hero {
    @Serial
    private static final long serialVersionUID = -8177551658725520452L;

    public Gunslinger(String name) {
        super(name, Element.FIRE);
        this.id=3L;
        skills.add(new PreciseShot());
        skills.add(new RapidFire());
        skills.add(new ArmorPiercing());
        skills.add(new DeathMark());
    }

    public Gunslinger(long id, String name, int level, int maxHp, int currentHp, int maxMp, int currentMp, int attack, int defense, int growthRate, Element element, int exp, int maxExp) {
        super(id, name, level, maxHp, currentHp, maxMp, currentMp, attack, defense, growthRate, element, exp, maxExp);
        skills.add(new PreciseShot());
        skills.add(new RapidFire());
        skills.add(new ArmorPiercing());
        skills.add(new DeathMark());
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        unlockSkillsByLevel();
    }

    private void unlockSkillsByLevel() {
        if(level == 5) {
            showSkillUnlockDialog("快速连射");
        } else if(level == 10) {
            showSkillUnlockDialog("穿甲弹");
        } else if(level == 15) {
            showSkillUnlockDialog("死亡标记");
        }
    }

    private void showSkillUnlockDialog(String skillName) {
        JOptionPane.showMessageDialog(null,
                "掌握新技能：" + skillName + "!",
                "枪械精通",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /*===== 技能实现 =====*/
    // 精准射击 (Lv.1)
    static class PreciseShot extends SkillAdapter {
        public PreciseShot() {
            this.skillName = "精准射击";
            this.costMp = 15;
            this.level=1;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getCurrentMp() < costMp) return false;

            // 基础伤害+暴击率提升
            int damage = (int)(user.getAttack() * 1.5
                    + user.getLevel() * 2);
            if(Math.random() < 0.3) damage *= 2; // 30%暴击

            user.setCurrentMp(user.getCurrentMp() - costMp);
            target.takeDamage(damage);
            return true;
        }
    }

    // 快速连射 (Lv.5)
    static class RapidFire extends SkillAdapter {
        public RapidFire() {
            this.skillName = "快速连射";
            this.costMp = 30;
            this.level=5;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 5 || user.getCurrentMp() < costMp) return false;

            // 三次快速射击
            for(int i=0; i<3; i++) {
                int damage = (int)(user.getAttack() * 0.7
                        + user.getLevel() * 1.5);
                target.takeDamage(damage);
            }
            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }

    // 穿甲弹 (Lv.10)
    static class ArmorPiercing extends SkillAdapter {
        public ArmorPiercing() {
            this.skillName = "穿甲弹";
            this.costMp = 50;
            this.level=10;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 10 || user.getCurrentMp() < costMp) return false;

            // 无视50%防御
            int damage = (int)((user.getAttack() * 2 + user.getLevel() * 4)
                    - (target.getDefense() * 0.5));
            damage = Math.max(damage, 1);

            user.setCurrentMp(user.getCurrentMp() - costMp);
            target.takeDamage(damage);
            return true;
        }
    }

    // 死亡标记 (Lv.15)
    static class DeathMark extends SkillAdapter {
        public DeathMark() {
            this.skillName = "死亡标记";
            this.costMp = 100;
            this.level=15;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 15 || user.getCurrentMp() < costMp) return false;

            // 标记目标三回合，每回合造成伤害
            new Timer(3000, e -> {
                int dotDamage = (int)(user.getAttack() * 0.5);
                target.takeDamage(dotDamage);
            }).start();

            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }
}