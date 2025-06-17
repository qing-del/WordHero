package Classes.Heroes;

import Classes.Element;
import Classes.Skills.SkillAdapter;
import Rule.Versus;
import javax.swing.*;
import java.io.Serial;

/**
 * 大地守卫 - 土属性防御专家
 * 技能随等级解锁：
 * Lv.1  岩石护甲
 * Lv.5  地震波
 * Lv.10 大地愈合
 * Lv.15 石化凝视
 */
public class EarthGuardian extends Hero {
    @Serial
    private static final long serialVersionUID = -4527254990550527649L;

    public EarthGuardian(String name) {
        super(name, Element.EARTH);
        this.id=1L;
        skills.add(new StoneArmor());
        skills.add(new SeismicWave());
        skills.add(new EarthHeal());
        skills.add(new PetrifyGaze());
    }

    public EarthGuardian(long id, String heroName, int level, int maxHp, int currentHp, int maxMp, int currentMp, int attack, int defense, int growthRate, Element element, int exp, int maxExp) {
        super(id, heroName, level, maxHp, currentHp, maxMp, currentMp, attack, defense, growthRate, element, exp, maxExp);
        skills.add(new StoneArmor());
        skills.add(new SeismicWave());
        skills.add(new EarthHeal());
        skills.add(new PetrifyGaze());
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        unlockSkillsByLevel();
    }

    private void unlockSkillsByLevel() {
        if(level == 5) {
            showSkillUnlockDialog("地震波");
        } else if(level == 10) {
            showSkillUnlockDialog("大地愈合");
        } else if(level == 15) {
            showSkillUnlockDialog("石化凝视");
        }
    }

    private void showSkillUnlockDialog(String skillName) {
        JOptionPane.showMessageDialog(null,
                "领悟大地之力：" + skillName + "!",
                "岩之共鸣",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /*===== 技能实现 =====*/
    // 岩石护甲 (Lv.1)
    static class StoneArmor extends SkillAdapter {
        public StoneArmor() {
            this.skillName = "岩石护甲";
            this.costMp = 20;
            this.level=1;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getCurrentMp() < costMp) return false;

            /*// TODO: 添加防御提升BUFF（持续3回合）
            int defenseBoost = (int)(user.getDefense() * 0.5);
            user.setDefense(user.getDefense() + defenseBoost);*/

            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }

    // 地震波 (Lv.5)
    static class SeismicWave extends SkillAdapter {
        public SeismicWave() {
            this.skillName = "地震波";
            this.costMp = 40;
            this.level=5;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 5 || user.getCurrentMp() < costMp) return false;

           /* // TODO: 添加群体减速DEBUFF
            int damage = (int)(user.getAttack() * 1.2 + user.getLevel() * 3);
            target.takeDamage(damage);*/

            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }

    // 大地愈合 (Lv.10)
    static class EarthHeal extends SkillAdapter {
        public EarthHeal() {
            this.skillName = "大地愈合";
            this.costMp = 60;
            this.level=10;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 10 || user.getCurrentMp() < costMp) return false;

            // 恢复生命值（基础+等级成长）
            int heal = (int)(user.getMaxHp() * 0.3 + user.getLevel() * 5);
            user.setCurrentHp(user.getCurrentHp() + heal);

            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }

    // 石化凝视 (Lv.15)
    static class PetrifyGaze extends SkillAdapter {
        public PetrifyGaze() {
            this.skillName = "石化凝视";
            this.costMp = 100;
            this.level=15;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 15 || user.getCurrentMp() < costMp) return false;

            /*// TODO: 添加石化状态（2回合无法行动）
            int damage = (int)(user.getAttack() * 2.5);
            target.takeDamage(damage);*/

            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }
}