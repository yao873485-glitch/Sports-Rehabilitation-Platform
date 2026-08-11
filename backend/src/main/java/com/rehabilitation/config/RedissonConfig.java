package com.rehabilitation.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();

        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        if (sentinel != null && StringUtils.hasText(sentinel.getMaster()) && !CollectionUtils.isEmpty(sentinel.getNodes())) {
            SentinelServersConfig sentinelConfig = config.useSentinelServers()
                    .setMasterName(sentinel.getMaster());
            for (String node : sentinel.getNodes()) {
                sentinelConfig.addSentinelAddress(toRedisAddress(node));
            }
            if (StringUtils.hasText(redisProperties.getPassword())) {
                sentinelConfig.setPassword(redisProperties.getPassword());
            }
            sentinelConfig.setDatabase(redisProperties.getDatabase());
        } else {
            String host = redisProperties.getHost() == null ? "127.0.0.1" : redisProperties.getHost();
            int port = redisProperties.getPort();
            String address = "redis://" + host + ":" + port;
            SingleServerConfig singleServerConfig = config.useSingleServer().setAddress(address);
            if (StringUtils.hasText(redisProperties.getPassword())) {
                singleServerConfig.setPassword(redisProperties.getPassword());
            }
            singleServerConfig.setDatabase(redisProperties.getDatabase());
        }

        return Redisson.create(config);
    }

    private String toRedisAddress(String node) {
        if (node.startsWith("redis://") || node.startsWith("rediss://")) {
            return node;
        }
        return "redis://" + node;
    }
}
