/**
 * Created by zeeler on 2019-10-1.
 * How to run:
 * 0) Make sure you have jre or jdk installed on your operation system
 * java -version
 * 1) compile code
 * javac -cp .:org-apache-commons-codec.jar AESBase64.java
 * 2) run class
 * java -cp .:org-apache-commons-codec.jar AESBase64
 */

import org.apache.commons.codec.binary.Base64;
import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESBase64{
    // algorithm and padding mode
    public static final String ALGORITHM_PADDING = "AES/CBC/PKCS5Padding";

    private static final String coding = "UTF-8";

    private SecretKeySpec keySpec;
    private IvParameterSpec iv; 

    public AESBase64(String aesKey, String ivStr) throws SecurityException, UnsupportedEncodingException {
        if (aesKey == null) {
            throw new SecurityException("AES key is null");
        }
        if (ivStr == null) {
            throw new SecurityException("IV is null");
        }
        iv = new IvParameterSpec(ivStr.getBytes(coding));
        byte[] raw = Base64.decodeBase64(aesKey.getBytes(coding));
        if (raw == null || raw.length != 16) {
            throw new IllegalArgumentException("AES Key is invalid");
        }

        keySpec = new SecretKeySpec(raw, "AES");
    }

    // decryption
    public String decrypt(String base64EncodeData) throws SecurityException {
        try {

            byte[] encryptedByte = Base64.decodeBase64(base64EncodeData.getBytes(coding));
            byte[] decryptedByte = decrypt(encryptedByte);
            return new String(decryptedByte, coding);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e);
        }
    }

    public byte[] decrypt(byte[] cipherData) throws SecurityException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            if (null == cipherData) {
                throw new IllegalBlockSizeException("no block data");
            }
            return cipher.doFinal(cipherData);
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    // encryption
    public String encrypt(String data) throws SecurityException {
        try {
            byte[] rawData = data.getBytes(coding);
            return new String(Base64.encodeBase64(encrypt(rawData)), coding);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e);
        }
    }

    public byte[] encrypt(byte[] rawData) throws SecurityException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            return cipher.doFinal(rawData);
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    // main function
    public static void main(String args[]){
        try {
            // init values
            String contentStr = "this is a test string";
            String aesKey = "cTFXd1RySmQ5VzZHa0NoeQ==";
            String ivStr = "1234567890864210";
            System.out.println("Origin: " + contentStr);

            // init obj
            AESBase64 aesCoder = new AESBase64(aesKey, ivStr);
            // encyption
            String encStr = aesCoder.encrypt(contentStr);
            // decryption
            String decStr = aesCoder.decrypt(aesCoder.encrypt(contentStr));
            System.out.println("Encrypt: " + encStr);
            System.out.println("Decrypt: " + decStr);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
