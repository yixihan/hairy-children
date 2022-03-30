package com.wq.util;

/**
 * @author : yixihan
 * @create : 2022-03-29-17:59
 */
public class VerifyUtils {


    /**
     * 校验是否为手机号
     * @param phone 手机号
     * @return
     */
    public static boolean isModel (String phone) {
        return phone.matches ("(13\\d|14[579]|15[^4\\D]|17[^49\\D]|18\\d)\\d{8}");
    }

    /**
     * 校验是否为邮箱
     * @param email 邮箱
     * @return
     */
    public static boolean isEmail (String email) {
        return email.matches ("\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}");
    }

    /**
     * 校验是否为身份证号
     * @param idCard 身份证号
     * @return
     */
    public static boolean isIdCard (String idCard) {
        return idCard.matches ("\\d{17}[0-9Xx]|\\d{15}");
    }


}
