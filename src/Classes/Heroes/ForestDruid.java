package Classes.Heroes;

import Classes.Element;
import Classes.Skills.SkillAdapter;
import Rule.Versus;
import javax.swing.*;
import java.io.Serial;

/**
 * 森林德鲁伊 - 木属性自然操控者
 * 技能随等级解锁：
 * Lv.1  藤蔓缠绕
 * Lv.5  自然治愈
 * Lv.10 剧毒孢子
 * Lv.15 森林之怒
 */
public class ForestDruid extends Hero {
    @Serial
    private static final long serialVersionUID = 1832408193830721050L;

    public ForestDruid(String name) {
        super(name, Element.WOOD);
        this.id=2L;
        skills.add(new VineBind());
        skills.add(new ToxicSpore());
        skills.add(new NatureHeal());
        skills.add(new ForestWrath());
    }

    public ForestDruid(long id, String name, int level, int maxHp, int currentHp, int maxMp, int currentMp, int attack, int defense, int growthRate, Element element, int exp, int maxExp) {
        super(id, name, level, maxHp, currentHp, maxMp, currentMp, attack, defense, growthRate, element, exp, maxExp);
        skills.add(new VineBind());
        skills.add(new ToxicSpore());
        skills.add(new NatureHeal());
        skills.add(new ForestWrath());
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        unlockSkillsByLevel();
    }

    private void unlockSkillsByLevel() {
        if(level == 5) {
            showSkillUnlockDialog("自然治愈");
        } else if(level == 10) {
            showSkillUnlockDialog("剧毒孢子");
        } else if(level == 15) {
            showSkillUnlockDialog("森林之怒");
        }
    }

    private void showSkillUnlockDialog(String skillName) {
        JOptionPane.showMessageDialog(null,
                "领悟自然奥秘：" + skillName + "!",
                "森之低语",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /*===== 技能实现 =====*/
    // 藤蔓缠绕 (Lv.1)
    static class VineBind extends SkillAdapter {
        public VineBind() {
            this.skillName = "藤蔓缠绕";
            this.costMp = 15;
            this.level=1;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getCurrentMp() < costMp) return false;

            /*// TODO: 添加减速DEBUFF
            int damage = (int)(user.getAttack() * 1.0 + user.getLevel() * 2);
            target.takeDamage(damage);*/

            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }

    // 自然治愈 (Lv.5)
    static class NatureHeal extends SkillAdapter {
        public NatureHeal() {
            this.skillName = "自然治愈";
            this.costMp = 30;
            this.level=5;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 5 || user.getCurrentMp() < costMp) return false;

            // 治疗目标（可对队友使用）
            int heal = (int)(user.getAttack() * 0.8 + user.getLevel() * 4);
            target.setCurrentHp(target.getCurrentHp() + heal);

            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }

    // 剧毒孢子 (Lv.10)
    static class ToxicSpore extends SkillAdapter {
        public ToxicSpore() {
            this.skillName = "剧毒孢子";
            this.costMp = 50;
            this.level=10;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 10 || user.getCurrentMp() < costMp) return false;

            /*// TODO: 添加中毒DEBUFF（持续3回合）
            int damage = (int)(user.getAttack() * 0.5);
            target.takeDamage(damage);*/

            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }

    // 森林之怒 (Lv.15)
    static class ForestWrath extends SkillAdapter {
        public ForestWrath() {
            this.skillName = "森林之怒";
            this.costMp = 100;
            this.level=15;
        }

        @Override
        public boolean use(Versus user, Versus target) {
            if(user.getLevel() < 15 || user.getCurrentMp() < costMp) return false;

            // 三连自然打击
            for(int i=0; i<3; i++){
                int damage = (int)(user.getAttack() * 1.5 + user.getLevel() * 5);
                target.takeDamage(damage);
            }
            user.setCurrentMp(user.getCurrentMp() - costMp);
            return true;
        }
    }
}