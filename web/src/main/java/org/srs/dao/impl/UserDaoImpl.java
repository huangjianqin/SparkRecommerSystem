package org.srs.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.srs.dao.UserDao;
import org.srs.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 健勤 on 2017/3/22.
 */
public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao{
    public List<User> findById(String userId) {
        return getSqlSession().selectList("org.srs.mapper.UserMapper.findById", userId);
    }

    public boolean insertUserInfo(User user) {
        return getSqlSession().insert("org.srs.mapper.UserMapper.insertUserInfo", user) > 0;
    }

    public List<User> selectAll(RowBounds bounds) {
        Map<String, Object> params = new HashMap<String, Object>();
        return getSqlSession().selectList("org.srs.mapper.UserMapper.selectAll", params, bounds);
    }
}
