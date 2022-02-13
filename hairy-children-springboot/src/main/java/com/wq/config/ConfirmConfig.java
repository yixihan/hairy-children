package com.wq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 配置类 - 发布确认
 * @author : yixihan
 * @create : 2022-02-13-14:06
 */
@Configuration
public class ConfirmConfig {

    /**
     * 交换机
     */
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";

    /**
     * 队列
     */
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";

    /**
     * routing key
     */
    public static final String CONFIRM_ROUTING_KEY = "key1";

    /**
     * 备份交换机
     */
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";

    /**
     * 备份队列
     */
    public static final String BACKUP_QUEUE_NAME = "backup_queue";

    /**
     * 报警队列
     */
    public static final String WARNING_QUEUE_NAME = "warning_queue";


    @Bean("confirmExchange")
    public DirectExchange confirmExchange () {

        HashMap<String, Object> arguments = new HashMap<> (16);
        arguments.put ("alternate-exchange", BACKUP_EXCHANGE_NAME);

        return ExchangeBuilder.directExchange (CONFIRM_EXCHANGE_NAME)
                .durable (true)
                .withArguments (arguments)
                .build ();
    }

    @Bean("backupExchange")
    public FanoutExchange backupExchange () {
        return new FanoutExchange (BACKUP_EXCHANGE_NAME);
    }

    @Bean("backupQueue")
    public Queue backupQueue () {
        return QueueBuilder.durable (BACKUP_QUEUE_NAME).build ();
    }

    @Bean("warningQueue")
    public Queue warningQueue () {
        return QueueBuilder.durable (WARNING_QUEUE_NAME).build ();
    }


    @Bean("confirmQueue")
    public Queue confirmQueue () {
        return QueueBuilder.durable (CONFIRM_QUEUE_NAME).build ();
    }

    @Bean
    public Binding queueBindingExchange (
            @Qualifier("confirmExchange") DirectExchange confirmExchange,
            @Qualifier("confirmQueue") Queue confirmQueue
    ) {
        return BindingBuilder.bind (confirmQueue).to (confirmExchange).with (CONFIRM_ROUTING_KEY);
    }

    @Bean
    public Binding queueBackupBindingExchangeBackup (
            @Qualifier("backupExchange") FanoutExchange confirmExchange,
            @Qualifier("backupQueue") Queue backupQueue
    ) {
        return BindingBuilder.bind (backupQueue).to (confirmExchange);
    }

    @Bean
    public Binding queueWarningBindingExchangeBackup (
            @Qualifier("backupExchange") FanoutExchange confirmExchange,
            @Qualifier("warningQueue") Queue warningQueue
    ) {
        return BindingBuilder.bind (warningQueue).to (confirmExchange);
    }

}

