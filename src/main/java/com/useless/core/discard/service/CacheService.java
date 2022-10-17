package com.useless.core.discard.service;

import com.useless.core.discard.constant.ConfigConsts;
import redis.clients.jedis.Jedis;

public class CacheService {

    private final Jedis cache;

    private static final String ACCOUNT_MAP_KEY = "account";

    public CacheService() {
        cache = new Jedis(ConfigConsts.REDIS_HOST, ConfigConsts.REDIS_PORT);
    }

    public boolean isAccountExist(String account) {
        return cache.hexists(ACCOUNT_MAP_KEY, account);
    }

    public String getPassword(String account) {
        return cache.hget(ACCOUNT_MAP_KEY, account);
    }

    public void setPassword(String account, String password) {
        cache.hset(ACCOUNT_MAP_KEY, account, password);
    }

    public void save() {
        cache.bgsave();
    }

    public boolean isNotClosed() {
        return cache.isConnected();
    }

    public void close() {
        if (isNotClosed()) {
            cache.close();
        }
    }

}
