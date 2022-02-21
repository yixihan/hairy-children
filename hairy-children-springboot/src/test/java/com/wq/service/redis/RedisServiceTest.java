package com.wq.service.redis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author : yixihan
 * @create : 2022-02-16-14:23
 */
@SpringBootTest
class RedisServiceTest {

    @Resource
    private RedisService redisService;

    @Test
    void setBit() {
        String key = "title_like:" + 2 + ":" + 2;
        for (int i = 0; i < 1000; i++) {

            Boolean bit = redisService.setBit (key, (long) i, false);
            System.out.println (bit);

        }

    }

    @Test
    void getBit() {
    }

    @Test
    void getLikeValueAll() {
    }
}