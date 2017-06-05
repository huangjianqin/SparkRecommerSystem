package org.srs.dao;

import org.apache.ibatis.session.RowBounds;
import org.srs.domain.Rating;

import java.util.List;
import java.util.Map;

/**
 * Created by 健勤 on 2017/3/23.
 */
public interface RatingDao {
    List<Rating> findByUser(String userId);
    List<Rating> findByUserMovie(Map<String, Object> params);
    boolean insertRatingInfo(Map<String, Object> params);
    List<Rating> selectAll(RowBounds bounds);
}
