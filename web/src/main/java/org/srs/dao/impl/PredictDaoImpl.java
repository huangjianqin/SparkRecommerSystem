package org.srs.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.srs.dao.PredictDao;
import org.srs.domain.Predict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 健勤 on 2017/3/23.
 */
public class PredictDaoImpl extends SqlSessionDaoSupport implements PredictDao {
    public List<Predict> findById(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", id);
        return getSqlSession().selectList("org.srs.mapper.PredictMapper.findById", params);
    }
}
