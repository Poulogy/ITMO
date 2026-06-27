package Server.system;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Утилитный класс, предназначенный для хэширования строк методом SHA-256.
 */
public class Encryptor {
    /**
     * Метод, использующий алгоритм SHA-256 для хэширования переданной строки.
     * @param input строка, которую необходимо захэшировать.
     * @return захэшированная строка в виде 16-символьной шестнадцатеричной строки.
     * @throws RuntimeException если не найден алгоритм SHA-256.
     */
    public static String encryptString(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 16) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
