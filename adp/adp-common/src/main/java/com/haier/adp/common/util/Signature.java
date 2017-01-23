package com.haier.adp.common.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * AUTHOR: zhangbin
 * ON: 15/5/13
 */
public class Signature {

    private Signature() {
    }

    private static final Random RANDOM = new Random();

    public static String sign(String key, Integer deep) {
        if (deep == 0) {
            return key;
        }
        String secretKey = Hashing.md5().newHasher().putString(key, Charsets.UTF_8).hash().toString();
        return sign(secretKey, deep - 1);
    }

    public static String sign(String key, String salt) {
        if (StringUtils.isEmpty(salt)) {
            return key;
        }
        return sign(key + salt, salt.length());
    }

    public static String random(Integer len) {
        final byte[] buffer = new byte[len];
        RANDOM.nextBytes(buffer);
        return BaseEncoding.base64Url().omitPadding().encode(buffer);
    }

}
