package com.wq.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取 SpringBoot 工厂中自动生成的 bean
 *
 * @author wq
 */

@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    /**
     * 获取 bean
     * @param beanName bean 的名字
     * @return bean
     */
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }
}
