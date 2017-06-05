package org.srs.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
@Controller
@RequestMapping("/select")
public class SelectController {
    private static Logger log = Logger.getLogger(SelectController.class);
    @Resource
    private SelectService select;

    @RequestMapping("/findMovieByIdOrName")
    @ResponseBody
    Map<String, Object> findMovieByIdOrName(@RequestParam("keyword") String keyword){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        result.put("flag", -1);
        List<Movie> movies = select.findMovieByIdOrName(keyword);

        if(movies != null){
            result.put("result", movies);
            result.put("flag", 1);
        }
        return result;
    }

    @RequestMapping("/findPredictById")
    @ResponseBody
    Map<String, Object> findPredictById(@RequestParam(value = "id", defaultValue = "1") String id){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        result.put("flag", -1);
        List<Predict> predicts = select.findPredictById(id);

        if(predicts != null){
            if(predicts.size() == 1 && predicts.get(0) != null){
                result.put("result", predicts.get(0));

            }
            else if(predicts.size() == 0){
                result.put("result", null);
            }
            result.put("flag", 1);
        }
        return result;
    }

    @RequestMapping("/findRatingByUserMovie")
    @ResponseBody
    Map<String, Object> findRatingByUserMovie(@RequestParam(value = "userId",defaultValue = "%") String userId,
                                              @RequestParam(value = "movieId", defaultValue = "%") String movieId){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        result.put("flag", -1);
        List<Rating> ratings = select.findRatingByUserMovie(userId, movieId);

        if(ratings != null){
            result.put("result", ratings);
            result.put("flag", 1);
        }
        return result;
    }

    @RequestMapping("/findUserById")
    @ResponseBody
    Map<String, Object> findUserById(@RequestParam("userId") String userId){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        result.put("flag", -1);
        List<User> users = select.findUserById(userId);

        if(users != null){
            result.put("result", users);
            result.put("flag", 1);
        }
        return result;
    }

    @RequestMapping("/allUser")
    @ResponseBody
    Map<String, Object> selectAllUser(@RequestParam(defaultValue = "1", name = "page", required = false)int  page){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        result.put("flag", -1);
        List<User> users = select.selectAllUser(page);

        if(users != null){
            result.put("result", users);
            result.put("flag", 1);
        }
        return result;
    }

    @RequestMapping("/allMovie")
    @ResponseBody
    Map<String, Object> selectAllMovie(@RequestParam(defaultValue = "1", name = "page", required = false)int page){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        result.put("flag", -1);
        List<Movie> movies = select.selectAllMovie(page);

        if(movies != null){
            result.put("result", movies);
            result.put("flag", 1);
        }
        return result;
    }

    @RequestMapping("/allRating")
    @ResponseBody
    Map<String, Object> selectAllRating(@RequestParam(defaultValue = "1", name = "page", required = false)int  page){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        result.put("flag", -1);
        List<Rating> ratings = select.selectAllRating(page);

        if(ratings != null){
            result.put("result", ratings);
            result.put("flag", 1);
        }
        return result;
    }
}
