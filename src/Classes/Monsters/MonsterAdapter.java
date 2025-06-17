package Classes.Monsters;

import Classes.Element;

import java.io.Serial;

public class MonsterAdapter extends Monster {
    @Serial
    private static final long serialVersionUID = -4936083966418632118L;

    /**
     * 野怪构造函数
     *
     * @param name    名称
     * @param level   等级（决定强度）
     * @param element 五行属性
     */
    protected MonsterAdapter(String name, int level, Element element) {
        super(name, level, element);
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
    public int getCurrentMp() {
        return 0;
    }

    @Override
    public void setCurrentHp(int i) {
    }

    @Override
    public void setCurrentMp(int i) {

    }
}