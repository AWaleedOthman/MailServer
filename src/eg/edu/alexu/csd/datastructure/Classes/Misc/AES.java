package eg.edu.alexu.csd.datastructure.Classes.Misc;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AES {
    private static SecretKeySpec secretKey;

    public static void setKey(String myKey)
    {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String secret)
    {
        String returned = null;
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            setKey(secret);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            returned = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
            return returned;
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return returned;
    }

    public static String decrypt(String strToDecrypt, String secret)
    {
        String returned = null;
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            returned = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            return returned;
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return returned;
    }
}
