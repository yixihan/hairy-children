package com.wq.config.shiro;

import com.wq.config.shiro.cache.RedisCacheManager;
import com.wq.config.shiro.jwt.JwtFilter;
import com.wq.config.shiro.realm.CustomRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : yixihan
 * @create : 2022-02-05-12:32
 */
@Configuration
@Slf4j
public class ShiroConfig {

    /**
     * 由 Spring 管理 Shiro 的生命周期
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * 开启对 Shiro 注解的支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }


    /**
     * 实现 Spring 的自动代理
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用 cglib ，防止重复代理和可能引起代理出错的问题
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }


    /**
     * 创建 ShiroFilter（用于拦截所有请求，对受限资源进行 Shiro 的认证和授权判断）
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 添加自己的过滤器并且取名为 jwt
        Map<String, Filter> filterMap = new HashMap<> (16);
        filterMap.put("jwt", new JwtFilter ());
        shiroFilterFactoryBean.setFilters(filterMap);

        // 配置系统的受限资源以及对应的过滤器
        Map<String, String> ruleMap = new HashMap<>(64);
        // 登录路径、注册路径都需要放行不进行拦截
        // 注册
        ruleMap.put("/v/**", "anon");
        ruleMap.put("/register", "anon");
        ruleMap.put("/common/**","anon");
        // 登录
        ruleMap.put("/login","anon");

        // 放行 Swagger 接口
        ruleMap.put("/favicon.ico", "anon");
        ruleMap.put("/swagger-ui.html","anon");
        ruleMap.put("/swagger/**","anon");
        ruleMap.put("/swagger-ui/**","anon");
        ruleMap.put("/swagger-ui/**/**","anon");
        ruleMap.put("/webjars/**","anon");
        ruleMap.put("/webjars/**/**","anon");
        ruleMap.put("/swagger-resources","anon");
        ruleMap.put("/swagger-resources/**","anon");
        ruleMap.put("/swagger-resources/**/**","anon");
        ruleMap.put("/v3/**","anon");

        // 放行 Druid 接口
        ruleMap.put("/druid/**", "anon");
        ruleMap.put("/druid/js/**", "anon");
        ruleMap.put("/druid/css/**", "anon");
        ruleMap.put("/druid/submitLogin", "anon");
        ruleMap.put("/druid/**/**", "anon");

        //静态资源放行
        ruleMap.put("/md/**/**", "anon");
        ruleMap.put("/dynamic/**/**", "anon");
        ruleMap.put("/file/**/**", "anon");
        ruleMap.put("/avatar/**/**", "anon");

        ruleMap.put("/favicon.ico**", "anon");
        ruleMap.put("/html/**", "anon");
        ruleMap.put("/css/**", "anon");
        ruleMap.put("/docs/**", "anon");
        ruleMap.put("/fonts/**", "anon");
        ruleMap.put("/ajax/**", "anon");
        ruleMap.put("/js/**", "anon");
        ruleMap.put("/captcha/captchaImage**", "anon");

        // /**，一般放在最下，表示对所有资源起作用，使用 JwtFilter
        ruleMap.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(ruleMap);

        return shiroFilterFactoryBean;
    }


    /**
     * 创建安全管理器（会自动设置到SecurityUtils中设置这个安全管理器）
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(
            Realm realm,
            CookieRememberMeManager rememberMeManager
    ){

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 给安全管理器设置 realm
        securityManager.setRealm(realm);

        // 给安全管理器设置 rememberMe
        securityManager.setRememberMeManager(rememberMeManager);

        // 关闭 shiro 的 session（无状态的方式使用 shiro ）
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }


    /**
     * 创建自定义 Realm ，注入到 spring 容器中
     */
    @Bean("realm")
    public Realm getRealm(){
        CustomRealm realm = new CustomRealm();

        // 开启缓存
        // 开启缓存管理
        realm.setCacheManager(new RedisCacheManager ());

        // 开启全局缓存
        realm.setCachingEnabled(true);

        // 开启认证缓存
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthenticationCacheName("authenticationCache");

        // 开启授权缓存
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName("authorizationCache");
        return realm;
    }


    /**
     * 创建自定义 RememberMe
     */
    @Bean("rememberMeManager")
    public CookieRememberMeManager getCookieRememberMeManager () {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();

        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(259200000);
        cookieRememberMeManager.setCookie(simpleCookie);

        // 手动设置对称加密秘钥，防止重启系统后系统生成新的随机秘钥，防止导致客户端 cookie 无效
        cookieRememberMeManager.setCipherKey(Base64.decode("6ZmI6I2j3Y+R1aSn5BOlAA=="));

        return cookieRememberMeManager;
    }
}
