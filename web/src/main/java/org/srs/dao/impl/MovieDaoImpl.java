package org.srs.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.srs.dao.MovieDao;
import org.srs.domain.Movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 健勤 on 2017/3/22.
 */
public class MovieDaoImpl extends SqlSessionDaoSupport implements MovieDao {

    public List<Movie> findMovieByIdOrName(String keyword) {
        return getSqlSession().selectList("org.srs.mapper.MovieMapper.findMovieByIdOrName", keyword);
    }

    public boolean insertMovieInfo(Movie movie) {
        return getSqlSession().insert("org.srs.mapper.MovieMapper.findByName", movie) > 0;
    }

    public List<Movie> selectAll(RowBounds bounds) {
        Map<String, Object> params = new HashMap<String, Object>();
        return getSqlSession().selectList("org.srs.mapper.MovieMapper.selectAll", params, bounds);
    }
}
