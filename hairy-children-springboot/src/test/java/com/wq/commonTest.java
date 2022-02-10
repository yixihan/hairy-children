package com.wq;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : yixihan
 * @create : 2022-02-07-16:29
 */
@SpringBootTest
public class commonTest {

    @Test
    public void test1 () {
        String val1 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3QxIiwiZXhwIjoxNjQ2NTc0MzU5LCJ1c2VySWQiOiIyIiwiaWF0IjoxNjQzOTgyMzU5fQ.hSAggK_QkZYMevL6pPI0Z97NjHV5qN5gy2wSjzwDer0";
        String val2 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InRlc3QxIiwiZXhwIjoxNjQ2ODE0NTA5LCJ1c2VySWQiOiIyIiwiaWF0IjoxNjQ0MjIyNTA5fQ.UaqJsOLk9soX9IodrEbYrPsXc2j5BPxVJ-IQXdzBLGc";

        String salt = "WiDtflDL@G";
        String password = "123456";
        String md5Password1 = "a1754d4b081df61d38b98d8cc7a4d6e2";
        String md5Password2 = new Md5Hash (password, salt, 1024).toHex ();

        System.out.println (md5Password1);
        System.out.println (md5Password2);
        System.out.println (md5Password1.equals (md5Password2));
    }
}
