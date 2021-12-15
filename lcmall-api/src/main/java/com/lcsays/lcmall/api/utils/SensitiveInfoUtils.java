package com.lcsays.lcmall.api.utils;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class SensitiveInfoUtils {

    public static String encrypt(String orignalInfo, String sensitiveSalt, String sensitiveKey) throws Exception {
        orignalInfo = orignalInfo.trim();
        if (StringUtils.isBlank(orignalInfo)) {
            return orignalInfo;
        }

        byte[] key = sensitiveKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        String str = orignalInfo + sensitiveSalt;
        byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return new BASE64Encoder().encode(encrypted);
    }

    public static String decrypt(String encrypt, String sensitiveSalt, String sensitiveKey) throws Exception {
        if (null == encrypt || StringUtils.isBlank(encrypt) || StringUtils.equals(encrypt, "null")) {
            return "";
        }

        byte[] key = sensitiveKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] encrypted1 = new BASE64Decoder().decodeBuffer(encrypt);
        byte[] original = cipher.doFinal(encrypted1);
        String originalInfo = new String(original, StandardCharsets.UTF_8);
        if (originalInfo.endsWith(sensitiveSalt)) {
            return originalInfo.substring(0, originalInfo.length()-sensitiveSalt.length());
        } else {
            return "";
        }
    }
}
