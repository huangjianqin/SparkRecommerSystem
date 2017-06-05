package org.srs.dao;

import org.apache.ibatis.session.RowBounds;
import org.srs.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by 健勤 on 2017/3/23.
 */
public interface UserDao {
    List<User> findById(String userId);
    boolean insertUserInfo(User user);
    List<User> selectAll(RowBounds bounds);
}
