package org.srs.service;

import org.srs.domain.Movie;
import org.srs.domain.Predict;
import org.srs.domain.Rating;
import org.srs.domain.User;

import java.util.List;

/**
 * Created by 健勤 on 2017/3/26.
 */
public interface SelectService {
    List<Movie> findMovieByIdOrName(String keyword);
    List<Predict> findPredictById(String id);
    List<Rating> findRatingByUser(String userId);
    List<Rating> findRatingByUserMovie(String userId, String movieId);
    List<User> findUserById(String userId);
    List<Movie> selectAllMovie(int page);
    List<User> selectAllUser(int page);
    List<Rating> selectAllRating(int page);
}
