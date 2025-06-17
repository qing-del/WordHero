package Data;

import java.io.*;
import java.util.ArrayList;

public class AccountData {
    //用户名数组
    private static ArrayList<String> USER = new ArrayList<>();
    //密码数组
    private static ArrayList<String> PASS = new ArrayList<>();

    private static final String fatherPath = "src\\Data\\";
    //数据操作
    static File file = new File(fatherPath + "Account_Data.dat");

    static {

        //若是文件不存在即创建文件
        if (!file.exists()) {
            try {
                File fatherDir = new File(fatherPath);
                if (!fatherDir.exists()) fatherDir.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*//先判断文件是否存在
        if(file.exists()){}*/

        try (BufferedReader fr = new BufferedReader(new FileReader(file))) {
            String line;    //读取一行的数据（读取的是加密的数据）
            while((line=fr.readLine())!=null) {
                //line = decompile(line); //先进行反编译
                String[] parts = line.split(",");   //分割开成为两部分

                if(parts.length!=2) {
                    System.out.println("该行数据异常 不进行读取 内容如下：");
                    System.out.println(line);
                    continue;
                }

                USER.add(parts[0]);
                PASS.add(parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Please check if the data in the database is normal!");
            System.err.println("Closing Program!");
            System.exit(0);
        }
    }

    /**
     * 对读入的字符串进行解码
     * @param line 传入读入的字符串
     * @return  输出解码之后的字符串
     */
    private static String decompile(String line) {
        char[] str = line.toCharArray();    //先转成数组
        for(int i=0;i<str.length;i++) str[i]=(char)(str[i]^'x');
        return new String(str);
    }

    /**
     * 对需要输出至文件的字符串进行加密
     * @param line  输出的字符串内容
     * @return  加密之后的字符串
     */
    private static String encryption(String line) {
        char[] str = line.toCharArray();
        for(char c : str) c = (char) (c ^ 'x');
        return new String(str);
    }

    /**
     * 查找用户是否在数据库中
     * @param user 传入用户名
     * @return  返回用户所在数据库的下标
     */
    static public int find(String user) {
        for(int i=0;i<USER.size();i++)
            if(USER.get(i).equals(user))
                return i;   //找到了返回下标
        return -1;  //找不到返回 -1
    }

    /**
     * 匹配密码是否配对
     * @param index 传入用户的下标用于查找密码
     * @param pass  传入输入的密码 查看密码是否与数据库中的密码配对
     * @return  返回配对结果（布尔值）
     */
    static public boolean match(int index, String pass){
        return PASS.get(index).equals(pass);
    }

    /**
     * 加入新的用户
     * @param user 用户名
     * @param pass 用户密码
     */
    static public void addAccount(String user, String pass){
        System.out.println("账户添加成功！");
        USER.add(user);
        PASS.add(pass);
    }

    static public void setPASS(int index, String pass) {
        System.out.println("密码修改成功！");
        PASS.set(index, pass);
    }

    static protected int getSize() {
        return USER.size();
    }

    static protected String getUser(int index) {
        return USER.get(index);
    }

    static protected String getPass(int index) {
        return PASS.get(index);
    }

    static protected void removeUser(int index) {
        USER.remove(index);
    }

    static protected void removePass(int index) {
        PASS.remove(index);
    }

    /**
     * 将所有数据存储到文件中实现持久化保存
     */
    static public void saveFile() {
        System.out.println("开始保存账号数据...");
        try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
            for(int i=0;i<USER.size();i++)
            {
                String line = USER.get(i) + "," + PASS.get(i);
                char[] str = line.toCharArray();
                //for(int j=0;j<str.length;j++) str[j]=(char)(str[j]^'x');
                fw.write(str);
                fw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("账号保存错误！");
            System.err.println("Closing Program!");
            System.exit(0);
        }
        System.out.println("账号数据保存成功！");
    }
}