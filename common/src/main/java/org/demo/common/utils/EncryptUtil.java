package org.demo.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.util.Base64;

// 该工具类由chatgpt生成
public class EncryptUtil {

    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = 62;

    public static String encryptEmail(String email, String key) {
        return encryptAES(email, key);
    }

    public static String decryptEmail(String encryptedEmail, String key) {
        return decryptAES(encryptedEmail, key);
    }

    private static String encryptAES(String data, String key) {
        try {
            if (key.length() != 16)
                throw new IllegalArgumentException("AES 密钥长度必须为16个字符");

            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES 加密失败", e);
        }
    }

    private static String decryptAES(String encryptedData, String key) {
        try {
            if (key.length() != 16)
                throw new IllegalArgumentException("AES 密钥长度必须为16个字符");

            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES 解密失败", e);
        }
    }

    // 加密
    public static String encryptPhone(String phone, long secret) {
        long phoneNumber = Long.parseLong(phone);
        long encoded = phoneNumber ^ secret;
        return encodeBase62(BigInteger.valueOf(encoded));
    }

    // 解密
    public static String decryptPhone(String encrypted, long secret) {
        BigInteger decoded = decodeBase62(encrypted);
        long original = decoded.longValue() ^ secret;
        return String.format("%011d", original);
    }

    private static String encodeBase62(BigInteger value) {
        StringBuilder sb = new StringBuilder();
        while (value.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = value.divideAndRemainder(BigInteger.valueOf(BASE));
            sb.append(BASE62_CHARS.charAt(divmod[1].intValue()));
            value = divmod[0];
        }
        return sb.reverse().toString();
    }

    private static BigInteger decodeBase62(String str) {
        BigInteger result = BigInteger.ZERO;
        for (char c : str.toCharArray()) {
            int index = BASE62_CHARS.indexOf(c);
            result = result.multiply(BigInteger.valueOf(BASE)).add(BigInteger.valueOf(index));
        }
        return result;
    }

}
