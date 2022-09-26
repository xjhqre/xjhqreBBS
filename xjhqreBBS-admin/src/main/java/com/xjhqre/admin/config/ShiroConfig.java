package com.xjhqre.admin.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xjhqre.admin.entity.Permission;
import com.xjhqre.admin.service.PermissionService;
import com.xjhqre.admin.shiro.MySessionManager;
import com.xjhqre.admin.shiro.MyShiroRealm;

@Configuration
public class ShiroConfig {
    @Autowired
    private PermissionService permissionService;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    // @Value("${spring.redis.timeout}")
    // private int timeout;
    //
    // @Value("${spring.redis.password}")
    // private String password;

    @Bean
    public MyShiroRealm getMyShiroRealm() {
        MyShiroRealm mShiroRealm = new MyShiroRealm();
        mShiroRealm.setCredentialsMatcher(this.hashedCredentialsMatcher());
        return mShiroRealm;
    }

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    // /**
    // * thymeleaf里使用shiro的标签的bean
    // *
    // * @return
    // */
    // @Bean
    // public ShiroDialect shiroDialect() {
    // return new ShiroDialect();
    // }

    /**
     * 处理拦截资源文件问题。
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.html"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/initPage");
        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 注意过滤器配置顺序 不能颠倒
        // anon. 配置不会被拦截的请求 顺序判断
        filterChainDefinitionMap.put("/favicon.png", "anon");// 解决弹出favicon.ico下载
        // 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/logout", "authc");
        filterChainDefinitionMap.put("/css/**", "anon");

        // swagger接口权限 开放
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");

        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/font-awesome/**", "anon");

        // 自定义加载权限资源关系
        List<Permission> list = this.permissionService.listAll();
        for (Permission p : list) {
            if (!p.getPerUrl().isEmpty()) {
                String permission = "perms[" + p.getPerUrl() + "]";
                filterChainDefinitionMap.put(p.getPerUrl(), permission);
            }
        }

        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        // // 设置登录页面
        // factoryBean.setLoginUrl("/login");
        // // 未授权页面
        // factoryBean.setUnauthorizedUrl("/unauth");

        return shiroFilterFactoryBean;
    }

    /**
     * 二.权限管理
     * 
     * @Title: securityManager
     * @Description: SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 设置realm.
        securityManager.setRealm(this.getMyShiroRealm());

        // 自定义缓存实现 使用redis
        // securityManager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(this.sessionManager());

        // 注入记住我管理器
        securityManager.setRememberMeManager(this.rememberMeManager());
        return securityManager;
    }

    /**
     * 凭证匹配器
     * 
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);// 散列的次数，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    /**
     * 开启shiro aop注解支持.
     * 
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
            new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 配置shiro redisManager 使用的是shiro-redis开源插件
     * 
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(this.host);
        redisManager.setPort(this.port);
        // redisManager.setPassword(password);
        // redisManager.setExpire(1800);// 配置缓存过期时间
        redisManager.setTimeout(0);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现 使用的是shiro-redis开源插件
     * 
     * @return
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(this.redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        // 自定义session管理 使用redis
        redisSessionDAO.setRedisManager(this.redisManager());
        return redisSessionDAO;
    }

    /**
     * shiro session的管理，自定义sessionManager，用户的唯一标识，即Token或Authorization的认证
     */
    @Bean
    public SessionManager sessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setSessionDAO(this.redisSessionDAO());
        return mySessionManager;
    }

    /**
     * 3.此处对应前端“记住我”的功能，获取用户关联信息而无需登录
     * 
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        // 这个参数是cookie的名称，对应前端的checkbox的name = remember
        SimpleCookie simpleCookie = new SimpleCookie("remember");
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(this.rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("one"));
        return cookieRememberMeManager;
    }

}
