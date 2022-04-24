package com.wq;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author : yixihan
 * @create : 2022-03-17-23:32
 */
public class CustomTest {

    @Test
    public void test () {
        System.out.println (Integer.MIN_VALUE - 1);
        System.out.println (Integer.MAX_VALUE);
    }

    @Test
    public void testEncodeURI () throws UnsupportedEncodingException {
        String url = URLDecoder.decode ("/title/2//title/2/%E5%90%8D%E5%AD%97%E6%B5%8B%E8%AF%95.assets/1650815402493.png", "UTF-8");
        System.out.println (url);
    }
}
