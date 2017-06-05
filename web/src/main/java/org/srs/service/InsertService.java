package org.srs.service;

import java.util.Map;

/**
 * Created by 健勤 on 2017/3/26.
 */
public interface InsertService {
    boolean insertMovieInfo(String id, String name, String genres);
    boolean insertRatingInfo(String userId, String movieId, double rating);
    boolean insertUserInfo(String id);
}
