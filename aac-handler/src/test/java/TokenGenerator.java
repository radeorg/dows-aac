import java.security.SecureRandom;

public class TokenGenerator {
    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        int length = random.nextInt(30) + 3; // 生成3到32之间的长度
        StringBuilder token = new StringBuilder();
        String charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charPool.length());
            token.append(charPool.charAt(index));
        }
        return token.toString();
    }

    public static void main(String[] args) {
        String token = generateToken();
        System.out.println("生成的Token: " + token);
    }
}