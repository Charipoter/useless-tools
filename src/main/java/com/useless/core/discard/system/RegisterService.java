package com.useless.core.discard.system;

import com.useless.core.discard.constant.StateConsts;
import com.useless.core.manager.SingletonManager;
import com.useless.core.discard.model.User;
import com.useless.core.discard.service.CacheService;
import com.useless.core.discard.service.SqlService;

public class RegisterService {

    CacheService cacheService = SingletonManager.getInstance(CacheService.class);
    SqlService sqlService = SingletonManager.getInstance(SqlService.class);

    public int accountExist(String account) {
        try {
            if (cacheService.isNotClosed() && cacheService.isAccountExist(account)) {
                // 先在缓存查找
                return StateConsts.ACCOUNT_EXIST;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sqlService.isClosed()) {
            return StateConsts.DB_ERROR;
        }
        try {
            User user = sqlService.getUserDao().findByAccount(account);
            return user != null
                    ? StateConsts.ACCOUNT_EXIST
                    : StateConsts.ACCOUNT_NOT_EXIST;
        } catch (Exception e) {
            sqlService.close();
            return StateConsts.DB_ERROR;
        }
    }

    public void register(User user) {
        sqlService.getUserDao().insert(user);
    }
}


