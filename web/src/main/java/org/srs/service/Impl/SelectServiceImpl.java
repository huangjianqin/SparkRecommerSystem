package org.srs.service.Impl;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.srs.dao.MovieDao;
import org.srs.dao.PredictDao;
import org.srs.dao.RatingDao;
import org.srs.dao.UserDao;
import org.srs.domain.Movie;
import org.srs.domain.Predict;
import org.srs.domain.Rating;
import org.srs.domain.User;
import org.srs.service.SelectService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 健勤 on 2017/3/26.
 */
@Service
public class SelectServiceImpl implements SelectService {
    @Resource
    private MovieDao movieDao;
    @Resource
    private UserDao userDao;
    @Resource
    private RatingDao ratingDao;
    @Resource
    private PredictDao predictDao;

    public List<Movie> findMovieByIdOrName(String keyword) {
        return movieDao.findMovieByIdOrName(keyword);
    }

    public List<Predict> findPredictById(String id) {
        return predictDao.findById(id);
    }

    public List<Rating> findRatingByUser(String userId) {
        return ratingDao.findByUser(userId);
    }

    public List<Rating> findRatingByUserMovie(String userId, String movieId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("movieId", movieId);
        return ratingDao.findByUserMovie(params);
    }

    public List<User> findUserById(String userId) {
        return userDao.findById(userId);
    }

    public List<Movie> selectAllMovie(int page) {
        RowBounds bounds = new RowBounds((page-1)*20, 20);
        return movieDao.selectAll(bounds);
    }

    public List<User> selectAllUser(int page) {
        RowBounds bounds = new RowBounds((page-1)*20, 20);
        return userDao.selectAll(bounds);
    }

    public List<Rating> selectAllRating(int page) {
        RowBounds bounds = new RowBounds((page-1)*20, 20);
        return ratingDao.selectAll(bounds);
    }
}
