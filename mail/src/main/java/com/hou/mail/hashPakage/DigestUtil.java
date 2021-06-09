package com.hou.mail.hashPakage;

import java.security.MessageDigest;

public class DigestUtil {
    public static String shaDigest(String str) {
        String DEFAULT_ENCODING = "UTF-8";
        String SHA_256 = "SHA";
        return Digest.digest(str, SHA_256, DEFAULT_ENCODING);
    }
}

class Digest {
    public static String digest(String str, String hashType, String charencoding) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder stringBuilder = new StringBuilder();
        MessageDigest messageDigest;//default is md5
        try {
            messageDigest = MessageDigest.getInstance(hashType);
            messageDigest.update(str.getBytes());
            byte[] ans = messageDigest.digest();
            for (byte an : ans) {
                int temp = an < 0 ? an + 256 : an;
                int id1 = temp / 16;
                int id2 = temp % 16;
                stringBuilder.append(hexDigits[id1]).append(hexDigits[id2]);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            System.out.println("digest failed!");
            e.printStackTrace();
        }
        return null;
    }
}