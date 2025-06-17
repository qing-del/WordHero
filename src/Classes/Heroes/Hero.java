package Classes.Heroes;

import Classes.Element;
import Classes.Skills.SkillAdapter;
import Rule.Versus;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * 英雄基类
 * 包含战斗系统需要的基础属性和方法
 */
public class Hero implements Versus, Serializable {
    @Serial
    private static final long serialVersionUID = -3261079181993965251L;

    private static final double Experience_Multiplier = 1.5;   //升级所需经验倍率

    protected long id;

    // 基础属性
    protected String name;        // 英雄名称
    protected int level;          // 当前等级
    protected int maxHp;          // 最大生命值
    protected int currentHp;      // 当前生命值
    protected int maxMp;          //最大蓝量
    protected int currentMp;      //当前蓝量
    protected int attack;         // 攻击力
    protected int defense;        // 防御力
    protected int growthRate;     // 成长值（影响升级提升幅度）
    protected Element element;    // 五行属性

    // 成长系统
    protected int exp;            // 当前经验值
    protected int maxExp;         // 升级所需经验

    public ArrayList<SkillAdapter> skills = new ArrayList<>();

    /**
     *英雄构造函数
     * @param name 名称
     * @param element 五行属性
     */
    public Hero(String name, Element element) {
        Random rd = new Random();
        this.name = name;
        this.attack = rd.nextInt(20,35);   //基础攻击力
        this.defense = rd.nextInt(10,20);  //基础防御力
        this.growthRate = rd.nextInt(5,10);    //基础成长值
        this.element = element;
        this.level = 1;
        this.maxExp = 100;
        this.maxHp = rd.nextInt(85,125);  //基础生命值
        this.maxMp = rd.nextInt(25,50);   //基础蓝量
        this.currentHp = maxHp;
        this.currentMp = maxMp;
    }

    /**
     * 用于管理创建英雄
     */
    public Hero(long id, String name, int level, int maxHp, int currentHp, int maxMp, int currentMp, int attack, int defense, int growthRate, Element element, int exp, int maxExp) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.maxHp = maxHp;
        this.currentHp = currentHp;
        this.maxMp = maxMp;
        this.currentMp = currentMp;
        this.attack = attack;
        this.defense = defense;
        this.growthRate = growthRate;
        this.element = element;
        this.exp = exp;
        this.maxExp = maxExp;
    }

    /**
     * 经验获取
     * 最高等级上线为 100级
     * @param exp 获得经验值
     */
    public void gainExp(int exp) {
        if(this.level >= 100) {
            this.exp=0;
            return;
        }
        this.exp += exp;
        while(this.exp >= maxExp) {
            levelUp();
        }
    }

    /**
     * 升级处理
     * 成长计算公式：
     *      攻击力 += 成长比值 * 2
     *      防御力 += 成长比值
     *      生命上线 += 成长比值 * 5
     *      蓝量上线 += 成长比值 * 2
     */
    protected void levelUp() {
        this.level++;
        this.exp -= maxExp;
        this.maxExp = (int)(maxExp * Experience_Multiplier);

        // 根据成长值提升属性
        this.attack += growthRate * 2;
        this.defense += growthRate;
        this.maxHp += growthRate * 5;
        this.currentHp = maxHp; // 升级回满血
        this.maxMp += growthRate * 2;
        this.currentMp = maxMp; //升级回满蓝

        System.out.println(name + "升级到Lv." + level + "!");
        JOptionPane.showMessageDialog(new JDialog(), name + "升级到Lv." + level + "!", "升级提示", JOptionPane.INFORMATION_MESSAGE);
    }

    // Getter方法
    public String getName() { return name; }
    @Override
    public int getAttack() { return attack; }
    @Override
    public int getDefense() {return this.defense;}
    @Override
    public int getLevel() { return level; }
    @Override
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getMaxMp() { return maxMp; }
    @Override
    public Element getElement() { return element; }
    @Override
    public int getCurrentMp() {return currentMp;}
    public int getGrowthRate() { return growthRate; }

    // 在Hero类中添加以下setter
    @Override
    public void setCurrentMp(int mp) {this.currentMp = Math.min(Math.max(mp, 0), maxMp);}
    @Override
    public void setCurrentHp(int hp) {this.currentHp = Math.min(Math.max(hp, 0), maxHp);}

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }

    public long getId() { return this.id; }
    public int getMaxExp() { return this.maxExp; }
    public int getExp() { return this.exp; }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setGrowthRate(int growthRate) {
        this.growthRate = growthRate;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }


    public ArrayList<SkillAdapter> getSkills() {
        return new ArrayList<>(skills);
    }

    /**
     * 状态检查
     * @return 是否存活
     */
    public boolean isAlive() {
        return currentHp > 0;
    }

    /**
     * 攻击方法
     * 伤害计算公式：（攻击力 * 0.5 + 等级 * 2） * 倍数 - 敌人防御
     * @param target 攻击目标
     * @return 造成的实际伤害
     */
    public int attack(Versus target) {
        double elementMultiplier = Element.getMultiplier(this.element, target.getElement()); //是否有克制关系
        int damage = (int)
                ((this.attack * 0.5 + this.level * 2) * elementMultiplier)
                - target.getDefense();
        damage = Math.max(damage, 1); // 至少造成1点伤害
        target.takeDamage(damage);  //调用敌人承受伤害函数
        return damage;
    }

    /**
     * 承受伤害
     * @param damage 伤害值
     */
    @Override
    public void takeDamage(int damage) {
        currentHp = Math.max(currentHp - damage, 0);
    }

    /**
     * 接入接口的攻击函数
     * 伤害计算公式：（攻击力 * 0.5 + 等级 * 2） * 倍数 - 敌人防御
     * @param attacker 攻击者
     * @param target 攻击目标
     * @return 造成的伤害值
     */
    @Override
    public int attack(Versus attacker, Versus target) {
        double elementMultiplier = Element.getMultiplier(this.element, target.getElement()); //是否有克制关系
        int damage = (int)
                ((this.attack * 0.5 + this.level * 2) * elementMultiplier)
                - target.getDefense();
        damage = Math.max(damage, 1); // 至少造成1点伤害
        target.takeDamage(damage);  //调用敌人承受伤害函数
        return damage;
    }

    @Override
    public String toString() {
        return String.join(",",
                Long.toString(id),
                name,
                Integer.toString(level),
                Integer.toString(maxHp),
                Integer.toString(currentHp),
                Integer.toString(maxMp),
                Integer.toString(currentMp),
                Integer.toString(attack),
                Integer.toString(defense),
                Integer.toString(growthRate),
                element.name(),
                Integer.toString(exp),
                Integer.toString(maxExp)
        );
    }

    /**
     * 从字符串解析Hero属性（需在Hero类内部调用）
     * @param str 格式为name,level,maxHp,currentHp,maxMp,currentMp,attack,defense,growthRate,element,exp,maxExp
     * @return 包含属性的Hero实例

    public static Hero fromString(String str) {
        String[] parts = str.split(",", -1);
        if (parts.length != 12) {
            throw new IllegalArgumentException("Invalid hero string format");
        }

        try {
            String name = parts[0];
            int level = Integer.parseInt(parts[1]);
            int maxHp = Integer.parseInt(parts[2]);
            int currentHp = Integer.parseInt(parts[3]);
            int maxMp = Integer.parseInt(parts[4]);
            int currentMp = Integer.parseInt(parts[5]);
            int attack = Integer.parseInt(parts[6]);
            int defense = Integer.parseInt(parts[7]);
            int growthRate = Integer.parseInt(parts[8]);
            Element element = Element.valueOf(parts[9]);
            int exp = Integer.parseInt(parts[10]);
            int maxExp = Integer.parseInt(parts[11]);

            // 使用管理员测试构造函数
            Hero hero = new Hero(name, attack, defense, growthRate, element);
            hero.level = level;
            hero.maxHp = maxHp;
            hero.currentHp = currentHp;
            hero.maxMp = maxMp;
            hero.currentMp = currentMp;
            hero.exp = exp;
            hero.maxExp = maxExp;

            return hero;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse hero from string: " + str, e);
        }
    }*/

}