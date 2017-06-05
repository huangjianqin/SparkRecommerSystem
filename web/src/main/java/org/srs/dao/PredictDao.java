package org.srs.dao;

import org.apache.ibatis.session.RowBounds;
import org.srs.domain.Predict;

import java.util.List;

/**
 * Created by 健勤 on 2017/3/23.
 */
public interface PredictDao {
    List<Predict> findById(String id);
}
