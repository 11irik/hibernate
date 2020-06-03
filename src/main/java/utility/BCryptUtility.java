package utility;


import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptUtility {
    private static final int LOG_ROUNDS = 5;

    public BCryptUtility() {
    }

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
    }

    public static boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}

