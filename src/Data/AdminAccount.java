package Data;

public class AdminAccount {
    private static final String USER = "Account-root";
    private static final String PASS = "666666";
    private AdminAccount(){}
    static public boolean check(String user, String pass) {
        if(user.isEmpty() || pass.isEmpty()) return false;
        return USER.equals(user) && PASS.equals(pass);
    }
}
