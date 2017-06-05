package org.srs.dao;

import org.apache.ibatis.session.RowBounds;
import org.srs.domain.Movie;
import java.util.List;
import java.util.Map;

/**
 * Created by 健勤 on 2017/3/23.
 */
public interface MovieDao {
    List<Movie> findMovieByIdOrName(String keyword);
    boolean insertMovieInfo(Movie movie);
    List<Movie> selectAll(RowBounds bounds);
}
