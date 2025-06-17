package Classes.Skills;

import Rule.Versus;

import java.io.Serial;
import java.io.Serializable;

/**
 * 技能基类（需继承实现）
 */
public abstract class Skill implements Versus, Serializable {
    @Serial
    private static final long serialVersionUID = -1556050583311698541L;

    public int level;
    public String skillName; //技能名称
    public int costMp;       //耗蓝
    public abstract boolean use(Versus user, Versus target);
}