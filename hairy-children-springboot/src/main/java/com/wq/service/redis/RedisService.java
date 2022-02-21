package com.wq.service.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author : yixihan
 * @create : 2022-02-16-10:26
 */
@Service
@Slf4j
public class RedisService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 点赞 / 取消点赞
     * <p>
     * setBit (author_id:title_id, reader_id, boolean) ===>
     * 读者 (reader_id) 对 作者 (author_id) 的 某篇作品 (title_id) 点了个赞 (true)
     * <p>
     * setBit (comment_user_id:root_id:title_id, reply_id, boolean) ===>
     * 用户 (reply_id) 对 用户 (comment_user_id) 在 某篇作品 (title_id) 下的 某个父评论 (root_id) 点了个赞 (true)
     *
     * @param key    redis key
     * @param offset 点赞人 id
     * @param value  true : 点赞 | false : 取消点赞
     * @return true : 操作成功 | false : 操作失败
     */
    public Boolean setBit (String key, Long offset, Boolean value) {
        return stringRedisTemplate.opsForValue ().setBit (key, offset, value);
    }

    /**
     *
     * @param key redis key
     * @param offset 点赞人 id
     * @return true : 已点赞 | false|null : 未点赞
     */
    public Boolean getBit (String key, Long offset) {
        return stringRedisTemplate.opsForValue ().getBit (key, offset);
    }

    /**
     * 获取点赞数
     * <p>
     * getLikeValueAll (author_id:title_id) ===>
     * 查看 作者(author_id) 某篇作品 (title_id) 的点赞量
     * <p>
     * getLikeValueAll (comment_user_id:root_id:title_id) ===>
     * 查看 用户 (comment_user_id) 在 某篇作品 (title_id) 下的 某个父评论 (root_id) 的点赞量
     *
     * @param key key redis key
     * @return 点赞总数
     */
    public Long getLikeValueAll(String key) {

        return stringRedisTemplate.execute (
                (RedisCallback<Long>) con -> con.bitCount (key.getBytes (StandardCharsets.UTF_8))
        );
    }
}
