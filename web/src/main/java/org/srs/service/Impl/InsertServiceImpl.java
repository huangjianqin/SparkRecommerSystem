package org.srs.service.Impl;

import org.springframework.stereotype.Service;
import org.srs.dao.MovieDao;
import org.srs.dao.RatingDao;
import org.srs.dao.UserDao;
import org.srs.domain.Movie;
import org.srs.domain.User;
import org.srs.service.InsertService;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 健勤 on 2017/3/26.
 */
@Service
public class InsertServiceImpl implements InsertService {
    @Resource
    private MovieDao movieDao;
    @Resource
    private UserDao userDao;
    @Resource
    private RatingDao ratingDao;

    public boolean insertMovieInfo(String id, String name, String genres) {
        Movie movie = new Movie(id, name, genres);
        return movieDao.insertMovieInfo(movie);
    }

    public boolean insertRatingInfo(String userId, String movieId, double rating) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", userId + ":" + movieId);
        params.put("rating", rating);
        return ratingDao.insertRatingInfo(params);
    }

    public boolean insertUserInfo(String id) {
        User user = new User(id);
        return userDao.insertUserInfo(user);
    }
}
