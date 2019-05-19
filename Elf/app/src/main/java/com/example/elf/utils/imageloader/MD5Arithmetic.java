package com.example.elf.utils.imageloader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Arithmetic {
    /**
     * 将url通过MD5算法转换成相应key的代码
     *
     * @param url
     * @return
     */
    public static String hashKeyFormUrl(String url) {
        String cacheKey = "";
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
