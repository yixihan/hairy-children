package com.wq.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类 - 发布确认
 * @author : yixihan
 * @create : 2022-02-13-14:06
 */
@Configuration
public class ConfirmConfig {

    /**
     * 点赞 - 文章
     */
    public static final String TITLE_LIKE_EXCHANGE_NAME = "title_like_exchange";
    public static final String TITLE_LIKE_QUEUE_NAME = "title_like_queue";
    public static final String TITLE_LIKE_ROUTING_KEY = "title_like";

    /**
     * 点赞 - 父评论
     */
    public static final String COMMENT_LIKE_EXCHANGE_NAME = "comment_like_exchange";
    public static final String COMMENT_LIKE_QUEUE_NAME = "comment_like_queue";
    public static final String COMMENT_LIKE_ROUTING_KEY = "comment_like";

    /**
     * 回复 - 文章
     */
    public static final String TITLE_REPLY_EXCHANGE_NAME = "title_reply_exchange";
    public static final String TITLE_REPLY_QUEUE_NAME = "title_reply_queue";
    public static final String TITLE_REPLY_ROUTING_KEY = "title_reply";

    /**
     * 回复 - 父评论
     */
    public static final String COMMENT_REPLY_EXCHANGE_NAME = "comment_reply_exchange";
    public static final String COMMENT_REPLY_QUEUE_NAME = "comment_reply_queue";
    public static final String COMMENT_REPLY_ROUTING_KEY = "comment_reply";

    /**
     * 领养
     */
    public static final String ADOPT_EXCHANGE_NAME = "adopt_exchange";
    public static final String ADOPT_QUEUE_NAME = "adopt_queue";
    public static final String ADOPT_ROUTING_KEY = "adopt_like";

    /**
     * 线索
     */
    public static final String CLUE_EXCHANGE_NAME = "clue_exchange";
    public static final String CLUE_QUEUE_NAME = "clue_queue";
    public static final String CLUE_ROUTING_KEY = "clue_like";



    @Bean("titleLikeExchange")
    public DirectExchange titleLikeExchange () {
        return new DirectExchange (TITLE_LIKE_EXCHANGE_NAME);
    }

    @Bean("titleLikeQueue")
    public Queue titleLikeQueue () {
        return QueueBuilder.durable (TITLE_LIKE_QUEUE_NAME).build ();
    }

    @Bean
    public Binding queueBindingTitleLikeExchange (
            @Qualifier("titleLikeExchange") DirectExchange titleLikeExchange,
            @Qualifier("titleLikeQueue") Queue titleLikeQueue
    ) {
        return BindingBuilder
                .bind (titleLikeQueue)
                .to (titleLikeExchange)
                .with (TITLE_LIKE_ROUTING_KEY);
    }


    @Bean("commentLikeExchange")
    public DirectExchange commentLikeExchange () {
        return new DirectExchange (COMMENT_LIKE_EXCHANGE_NAME);
    }

    @Bean("commentLikeQueue")
    public Queue commentLikeQueue () {
        return QueueBuilder.durable (COMMENT_LIKE_QUEUE_NAME).build ();
    }

    @Bean
    public Binding queueBindingCommentLikeExchange (
            @Qualifier("commentLikeExchange") DirectExchange commentLikeExchange,
            @Qualifier("commentLikeQueue") Queue commentLikeQueue
    ) {
        return BindingBuilder
                .bind (commentLikeQueue)
                .to (commentLikeExchange)
                .with (COMMENT_LIKE_ROUTING_KEY);
    }


    @Bean("titleReplyExchange")
    public DirectExchange titleReplyExchange () {
        return new DirectExchange (TITLE_REPLY_EXCHANGE_NAME);
    }

    @Bean("titleReplyQueue")
    public Queue titleReplyQueue () {
        return QueueBuilder.durable (TITLE_REPLY_QUEUE_NAME).build ();
    }

    @Bean
    public Binding queueBindingTitleReplyExchange (
            @Qualifier("titleReplyExchange") DirectExchange titleReplyExchange,
            @Qualifier("titleReplyQueue") Queue titleReplyQueue
    ) {
        return BindingBuilder
                .bind (titleReplyQueue)
                .to (titleReplyExchange)
                .with (TITLE_REPLY_ROUTING_KEY);
    }


    @Bean("commentReplyExchange")
    public DirectExchange commentReplyExchange () {
        return new DirectExchange (COMMENT_REPLY_EXCHANGE_NAME);
    }

    @Bean("commentReplyQueue")
    public Queue commentReplyQueue () {
        return QueueBuilder.durable (COMMENT_REPLY_QUEUE_NAME).build ();
    }

    @Bean
    public Binding queueBindingCommentReplyExchange (
            @Qualifier("commentReplyExchange") DirectExchange commentReplyExchange,
            @Qualifier("commentReplyQueue") Queue commentReplyQueue
    ) {
        return BindingBuilder
                .bind (commentReplyQueue)
                .to (commentReplyExchange)
                .with (COMMENT_REPLY_ROUTING_KEY);
    }


    @Bean("adoptExchange")
    public DirectExchange adoptExchange () {
        return new DirectExchange (ADOPT_EXCHANGE_NAME);
    }

    @Bean("adoptQueue")
    public Queue adoptQueue () {
        return QueueBuilder.durable (ADOPT_QUEUE_NAME).build ();
    }

    @Bean
    public Binding queueBindingAdoptExchange (
            @Qualifier("adoptExchange") DirectExchange adoptExchange,
            @Qualifier("adoptQueue") Queue adoptQueue
    ) {
        return BindingBuilder
                .bind (adoptQueue)
                .to (adoptExchange)
                .with (ADOPT_ROUTING_KEY);
    }


    @Bean("clueExchange")
    public DirectExchange clueExchange () {
        return new DirectExchange (CLUE_EXCHANGE_NAME);
    }


    @Bean("clueQueue")
    public Queue clueQueue () {
        return QueueBuilder.durable (CLUE_QUEUE_NAME).build ();
    }

    @Bean
    public Binding queueBindingClueExchange (
            @Qualifier("clueExchange") DirectExchange clueExchange,
            @Qualifier("clueQueue") Queue clueQueue
    ) {
        return BindingBuilder
                .bind (clueQueue)
                .to (clueExchange)
                .with (CLUE_ROUTING_KEY);
    }

}

