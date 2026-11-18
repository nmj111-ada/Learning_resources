package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        //1.创建Redisson配置
        Config config = new Config();
        //2.复用spring.redis中的单机连接配置，避免两套客户端连接到不同的Redis
        String prefix = redisProperties.isSsl() ? "rediss://" : "redis://";
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setDatabase(redisProperties.getDatabase());
        //3.Redis设置了用户名或密码时，再配置认证信息
        if (StringUtils.hasText(redisProperties.getUsername())) {
            serverConfig.setUsername(redisProperties.getUsername());
        }
        if (StringUtils.hasText(redisProperties.getPassword())) {
            serverConfig.setPassword(redisProperties.getPassword());
        }
        //4.创建客户端，交给Spring管理，应用关闭时释放连接
        return Redisson.create(config);
    }
}
