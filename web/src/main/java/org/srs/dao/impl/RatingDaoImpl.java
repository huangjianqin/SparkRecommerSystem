package org.srs.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.srs.dao.RatingDao;
import org.srs.domain.Rating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 健勤 on 2017/3/22.
 */
public class RatingDaoImpl extends SqlSessionDaoSupport implements RatingDao{
    public List<Rating> findByUser(String userId) {
        return getSqlSession().selectList("org.srs.mapper.RatingMapper.findByUser", userId);
    }

    public List<Rating> findByUserMovie(Map<String, Object> params) {
        return getSqlSession().selectList("org.srs.mapper.RatingMapper.findByUserMovie", params);
    }

    public boolean insertRatingInfo(Map<String, Object> params) {
        return getSqlSession().insert("org.srs.mapper.RatingMapper.insertRatingInfo", params) > 0;
    }

    public List<Rating> selectAll(RowBounds bounds) {
        Map<String, Object> params = new HashMap<String, Object>();
        return getSqlSession().selectList("org.srs.mapper.RatingMapper.selectAll", params, bounds);
    }
}
