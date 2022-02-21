package com.wq;

import com.wq.service.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class HairyChildrenSpringbootApplicationTests {

    @Resource
    private RedisService redisService;

    @Test
    void contextLoads() {
    }

    @Test
    public void test1 () {
        String key = "title_like:2:1";
        System.out.println (redisService.getLikeValueAll (key));
    }

}
