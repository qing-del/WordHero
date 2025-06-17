package Players;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlayerDataManager {
    static String fatherPath = "src\\Players\\PlayersData\\";
    static String AllPlayers = "src\\Players\\PlayersData\\allPlayers.dat";
    static ArrayList<String> USER = new ArrayList<>();
    static ArrayList<String> DATE = new ArrayList<>();
    static File file = new File(AllPlayers);

    static {
        // 初始化时加载所有用户名
        if(!file.exists()) {
            try {
                new File(fatherPath).mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("初始化玩家数据文件失败");
                System.exit(0);
            }
        }
        try {
            loadUserList();
            loadData();
        } catch (IOException e) {
            System.out.println("读取异常");
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载所有用户名到内存
     */
    private static void loadUserList() throws IOException {
        BufferedReader fr = new BufferedReader(new FileReader(AllPlayers));
        String line;
        while((line = fr.readLine()) != null) {
            //line = encryption(line);
            USER.add(line);
        }
        System.out.println("玩家列表读取成功！");
    }

    /**
     * 保存用户名列表
     */
    private static void saveUserList() throws IOException {
        BufferedWriter fw = new BufferedWriter(new FileWriter(AllPlayers));
        for(int i=0;i<USER.size();i++) {
            String line = USER.get(i);
            fw.write(line);
            fw.newLine();
        }
        fw.close();
        System.out.println("保存玩家列表成功！");
    }

    /**
     * 创建新玩家（自动记录时间）
     */
    public static Player createNewPlayer(String user, String name) throws IOException {
        if (USER.contains(user)) return null;

        Player player = new Player(user, name);
        savePlayer(player);
        return player;
    }

    /**
     * 异或加密（与解密使用相同方法）
     */
    private static String encryption(String line) {
        char[] chars = line.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (chars[i] ^ 'x');
        }
        return new String(chars);
    }

    /**
     * 保存玩家数据 （舍弃版）
     * @param player 要保存的玩家对象
     * @throws IOException 当文件操作失败时抛出
    public static void savePlayer(Player player) throws IOException {
        String dateDir = new SimpleDateFormat("yyyy-MM-dd").format(player.getAccountCreationDate());
        File saveDir = new File(fatherPath + dateDir);
        if (!saveDir.exists()) saveDir.mkdirs();    //如果文件父级路径不存在就创建

        //将玩家保存至列表当中
        if (!USER.contains(player.user)) {
            USER.add(player.user);
            DATE.add(dateDir);
            saveData();
            saveUserList();
        }

        BufferedWriter fw = new BufferedWriter(new FileWriter(new File(saveDir, player.user + ".dat")));
        fw.write(Boolean.toString(player.newPlayer));   //写出是否是新玩家数据
        fw.newLine();
        fw.write(player.user);  //写出玩家用户名
        fw.newLine();
        fw.write(player.name);  //写出玩家名字
        fw.newLine();
        fw.write(Integer.toString(player.heroNum));
        fw.newLine();
        for(int i=0;i<player.team.size();i++) {
            fw.write(player.team.get(i).toString());
            fw.newLine();
        }
        fw.close();
        System.out.println("玩家数据保存成功！");
    }*/

    /**
     * 保存玩家数据
     * @param player 要保存的玩家对象
     * @throws IOException 当文件操作失败时抛出
     */
    public static void savePlayer(Player player) throws IOException {
        String dateDir = new SimpleDateFormat("yyyy-MM-dd").format(player.getAccountCreationDate());
        File faSaveDir = new File(fatherPath + dateDir);
        if(!faSaveDir.exists()) faSaveDir.mkdirs();

        //将玩家保存至列表当中
        if (!USER.contains(player.user)) {
            USER.add(player.user);
            DATE.add(dateDir);
            saveData();
            saveUserList();
        }

        File saveDir = new File(fatherPath + dateDir + "\\" + player.user + ".dat");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveDir));
        oos.writeObject(player);
        oos.close();
        System.out.println(player.getName() + " 玩家保存成功！");
    }


    public static Player loadPlayer(String username) throws IOException, ClassNotFoundException, ParseException {
        for(int i=0;i<USER.size();i++) {
            if(USER.get(i).equals(username)) {
                return loadPlayer(username, new SimpleDateFormat("yyyy-MM-dd").parse(DATE.get(i)));
            }
        }
        return null;
    }

    /**
     * 加载玩家数据 （舍弃版）
     * @param username 要加载的用户名
     * @param createDate 账号创建日期
     * @return 加载的玩家对象，不存在时返回null
     * @throws IOException 当文件操作失败时抛出
     * @throws ClassNotFoundException 当类定义不匹配时抛出
    public static Player loadPlayer(String username, Date createDate) throws IOException, ClassNotFoundException {
        if (!USER.contains(username)) return null;

        String dateDir = new SimpleDateFormat("yyyy-MM-dd").format(createDate);

        File playerSaver = new File(fatherPath + dateDir + "\\" + username + ".dat");
        if(!playerSaver.exists()) playerSaver.createNewFile();

        BufferedReader fr = new BufferedReader(new FileReader(playerSaver));
        boolean newPlayer = Boolean.parseBoolean(fr.readLine());    //转化布尔值
        username = fr.readLine();    //读取用户名
        String name = fr.readLine();    //读取玩家名字
        ArrayList<Hero> team  = new ArrayList<>();  //先创建一个英雄团队
        int heroNum = Integer.parseInt(fr.readLine());  //读取英雄的数量
        for(int i=0;i<heroNum;i++) {
            String[] parts = fr.readLine().split(",");
            long id = Long.parseLong(parts[0]); //先分割id
            String heroName = parts[1]; //读取英雄名字
            int level = Integer.parseInt(parts[2]); //读取英雄等级
            int maxHp = Integer.parseInt(parts[3]); //读取英雄最大生命值
            int currentHp = Integer.parseInt(parts[4]); //读取英雄目前生命值
            int maxMp = Integer.parseInt(parts[5]); //读取英雄最大魔力
            int currentMp = Integer.parseInt(parts[6]); //读取英雄当前魔力
            int attack = Integer.parseInt(parts[7]);    //读取英雄攻击力
            int defense = Integer.parseInt(parts[8]);   //读取英雄防御力
            int growthRate = Integer.parseInt(parts[9]);    //读取英雄成长值
            Element element = Element.valueOf(Element.class, parts[10]);    //读取五行属性
            int exp = Integer.parseInt(parts[11]);  //读取目前经验状态
            int maxExp = Integer.parseInt(parts[12]);   //读取最大经验

            team.add(switch ((int) id) {
                case 1 -> new EarthGuardian(id, heroName, level, maxHp, currentHp, maxMp, currentMp, attack, defense, growthRate, element, exp, maxExp);
                case 2 -> new ForestDruid(id, heroName, level, maxHp, currentHp, maxMp, currentMp, attack, defense, growthRate, element, exp, maxExp);
                case 3 -> new Gunslinger(id, heroName, level, maxHp, currentHp, maxMp, currentMp, attack, defense, growthRate, element, exp, maxExp);
                case 4 -> new Mage(id, heroName, level, maxHp, currentHp, maxMp, currentMp, attack, defense, growthRate, element, exp, maxExp);
                case 5 -> new SwordImmortal(id, heroName, level, maxHp, currentHp, maxMp, currentMp, attack, defense, growthRate, element, exp, maxExp);
                default -> throw new RuntimeException("读取英雄错误！");
            });
        }

        fr.close(); //关闭通道
        System.out.println("玩家加载成功！");
        return new Player(createDate, newPlayer, username, name, team);
    }*/

    /**
     * 加载玩家数据
     * @param username 要加载的用户名
     * @param createDate 账号创建日期
     * @return 加载的玩家对象，不存在时返回null
     * @throws IOException 当文件操作失败时抛出
     * @throws ClassNotFoundException 当类定义不匹配时抛出
     */
    public static Player loadPlayer (String username, Date createDate) throws IOException, ClassNotFoundException {
        if (!USER.contains(username)) return null;

        String dateDir = new SimpleDateFormat("yyyy-MM-dd").format(createDate);

        File playerSaver = new File(fatherPath + dateDir + "\\" + username + ".dat");
//        if(!playerSaver.exists()) return null;

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(playerSaver));

        Player res =  (Player) ois.readObject();
        System.out.println("玩家加载成功！");
        ois.close();
        return res;
    }

    /**
     * 保存玩家的日期
     * @throws IOException 保存失败的时候抛出
     */
    public static void saveData() throws IOException {
        BufferedWriter fw = new BufferedWriter(new FileWriter(fatherPath + "Date.dat"));
        for(int i=0;i<DATE.size();i++) {
            fw.write(DATE.get(i).toCharArray());
            fw.newLine();
        }

        fw.close();
        System.out.println("保存玩家日期成功！");
    }

    public static void loadData() throws IOException {
        File dateSaver = new File(fatherPath + "Date.dat");
        if(!dateSaver.exists()) dateSaver.createNewFile();
        BufferedReader fr = new BufferedReader(new FileReader(dateSaver));
        String line;
        while((line = fr.readLine()) != null) {
            DATE.add(line);
        }

        fr.close();
        System.out.println("日期加载成功！");
    }

    /**
     * 删除玩家所有数据
     * @param user 传入玩家名字
     */
    public static void delete(String user) {
        int index = -1;
        for(int i=0;i<USER.size();i++) {
            if(USER.get(i).equals(user)){
                index=i;
                break;
            }
        }

        if(index==-1) return;  //玩家不存在

        File f = new File(fatherPath + DATE.get(index) + "\\" + user + ".dat");
        f.delete();
        USER.remove(index);
        DATE.remove(index);

        try {
            saveData();
            saveUserList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("玩家数据删除成功！");
    }
}