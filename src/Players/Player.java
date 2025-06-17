package Players;

import Classes.Heroes.Hero;
import Classes.Maps.MainCity;
import Classes.Maps.Map;
import UI.MainUI.GameJFrame;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/*一个玩家账号的基本信息
一个玩家对象表示一个账号的数据*/
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = -582410529992290502L;
    private long coins;
    Date AccountCreationDate;
    public boolean newPlayer = true;   //是否是新玩家
    String user;    //玩家用户名
    String name;    //玩家名字
    int heroNum;
    ArrayList<Hero> team = new ArrayList<>();    //玩家所拥有的英雄团
    public Map position; //玩家处于的位置（默认在主城）
    public transient GameJFrame gameJFrame;   //玩家的游戏窗口链接


    /**
     * 玩家登录所使用的函数
     * @param user 传入玩家的用户名
     * @param name 传入玩家的名字
     */
    public Player(String user, String name) {
        this.user = user;
        this.name = name;
        AccountCreationDate = new Date();
        this.position = new MainCity(); //默认出生在主城
    }

    public Player(Date accountCreationDate, boolean newPlayer, String user, String name, ArrayList<Hero> team) {
        AccountCreationDate = accountCreationDate;
        this.newPlayer = newPlayer;
        this.user = user;
        this.name = name;
        this.team = team;
        this.position = new MainCity(); //默认出生在主城
    }

    /**
     * 给玩家英雄团加入英雄
     * @param hero 加入玩家英雄团的英雄
     */
    public void addHero(Hero hero) {
        team.add(hero);
        heroNum=team.size();
    }

    /**
     * 是否进入新手过程
     * @return 是否需要进入
     */
    public boolean BeginnerProcess() {
        if(newPlayer) {
            newPlayer=false;
            return true;
        }
        return false;
    }

    public void initializeNewPlayer(Hero selectedHero) {
        team.add(selectedHero);
        newPlayer = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Date getAccountCreationDate() { return this.AccountCreationDate; }
    public ArrayList<Hero> getTeam() {
        return team;
    }

    public Player(int heroNum) {
        this.heroNum = heroNum;
    }

    public long getCoins() {
        return coins;
    }
    public void gainCoin(long add) {
        this.coins += add;
    }
}