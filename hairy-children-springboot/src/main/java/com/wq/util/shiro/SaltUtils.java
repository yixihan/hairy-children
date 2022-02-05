package com.wq.util.shiro;

import java.util.Random;

/**
 * @author : yixihan
 * @create : 2022-02-05-12:22
 */
public class SaltUtils {

    /**
     * 生成一个指定长度的随机盐
     * @param length length
     * @return salt
     */
    public static String getSalt (int length) {
        char[] arr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*".toCharArray();

        Random random = new Random();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(arr[random.nextInt(arr.length)]);
        }

        return sb.toString();
    }
}
