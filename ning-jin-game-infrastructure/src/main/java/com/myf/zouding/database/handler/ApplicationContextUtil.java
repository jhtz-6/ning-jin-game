package com.myf.zouding.database.handler;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author: myf
 * @CreateTime: 2023-12-17  22:20
 * @Description: TODO
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static Object getBeanByName(String beanName) {
        return context.getBean(beanName);
    }
}
