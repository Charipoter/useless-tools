package com.useless.core.discard.system;

import com.useless.core.discard.constant.StateConsts;
import com.useless.core.manager.SingletonManager;
import com.useless.core.discard.model.User;
import com.useless.core.discard.service.CacheService;
import com.useless.core.discard.service.SqlService;

public class LoginService {

    CacheService cacheService = SingletonManager.getInstance(CacheService.class);
    SqlService sqlService = SingletonManager.getInstance(SqlService.class);

    public int tryLogin(String account, String password) {
        try {
            if (cacheService.isNotClosed() && cacheService.isAccountExist(account)) {
                // 缓存开启则先尝试在缓存中寻找,匹配密码
                return cacheService.getPassword(account).equals(password)
                        ? StateConsts.ACCOUNT_EXIST
                        : StateConsts.PASSWORD_ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sqlService.isClosed()) {
            return StateConsts.DB_ERROR;
        }
        // 如果数据库开启则在sql数据库中寻找
        try {
            User user = sqlService.getUserDao().findByAccount(account);
            if (user == null) {
                return StateConsts.ACCOUNT_NOT_EXIST;
            } else if (user.getPassword().equals(password)) {
                if (cacheService.isNotClosed()) {
                    cacheService.setPassword(account, password);
                }
                return StateConsts.ACCOUNT_EXIST;
            } else {
                return StateConsts.PASSWORD_ERROR;
            }
        } catch (Exception e) {
            sqlService.close();
            return StateConsts.DB_ERROR;
        }
    }
}
