package com.useless.core.discard.service;

import com.useless.core.discard.constant.ConfigConsts;
import com.useless.core.discard.dao.UserDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlService {

    private SqlSession session;

    private UserDao userDao;

    private boolean closed = false;

    public SqlService() {
        try {
            session = new SqlSessionFactoryBuilder()
                    .build(Resources.getResourceAsReader(ConfigConsts.MYBATIS_CONFIG))
                    .openSession(true);

            userDao = session.getMapper(UserDao.class);
        } catch (Exception ignored) {
            close();
        }
    }

    public boolean isClosed() {
        return closed;
    }

    public void close() {
        if (!closed) {
            session.close();
            closed = true;
        }
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
