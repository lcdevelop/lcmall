package com.lcsays.lcmall.api;

import com.lcsays.lcmall.api.utils.SensitiveInfoUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SensitiveInfoUtilsTest {

    @Test
    public void testEncrypt() {
        String originalInfo = "13812345678";
        String sensitiveSalt = "jid8*37838**)(&#(";
        String sensitiveKey = "1234567890abcdef";
        try {
            String res = SensitiveInfoUtils.encrypt(originalInfo, sensitiveSalt, sensitiveKey);
            System.out.println(res);
            String decrypt = SensitiveInfoUtils.decrypt(res, sensitiveSalt, sensitiveKey);
            System.out.println(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
