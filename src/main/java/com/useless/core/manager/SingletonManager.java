package com.useless.core.manager;

import cn.hutool.core.lang.Singleton;

public class SingletonManager {

    public static <T> T getInstance(Class<T> clazz) {
        return Singleton.get(clazz);
    }

    private SingletonManager() {}
}
