package org.srs.controller;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.srs.service.InsertService;
import javax.annotation.Resource;
import java.util.*;

/**
 * Created by 健勤 on 2017/3/26.
 */
@Controller
@RequestMapping("/insert")
public class InsertController {
    private static Logger log = Logger.getLogger(InsertController.class);
    @Resource
    private InsertService insert;
    @Resource
    private KafkaProducer kafkaProducer;

    @RequestMapping("/movieInfo")
    @ResponseBody
    Map<String, Object> insertMovieInfo(@RequestParam("id") String id,
                                        @RequestParam("name") String name,
                                        @RequestParam("genres") String genres){
        Map<String, Object> result = new HashMap<String, Object>();
        int flag = insert.insertMovieInfo(id, name, genres) ? 1:-1;

        result.put("flag", flag);

        return result;
    }

    @RequestMapping("/ratingInfo")
    @ResponseBody
    Map<String, Object> insertRatingInfo(@RequestParam("ratings") String ratings){
        Map<String, Object> result = new HashMap<String, Object>();
        int flag = 1;

        List<String> msgs = new ArrayList<String>();
        for(String rating: ratings.split("\\n")){
            String userId = rating.split(",")[0];
            String movieId = rating.split(",")[1];
            double ratingD = Double.valueOf(rating.split(",")[2]);

            msgs.add(userId + "::" + movieId + "::" + ratingD);
            //交由spark往hbase存储
            //存储速度会比hive快多了
//            flag *= insert.insertRatingInfo(userId, movieId, ratingD) ? 1:-1;
        }

        //发送kafka消息
        log.info("sending " + msgs.size() + " msg...");
        for(final String msg: msgs){
            kafkaProducer.send(new ProducerRecord("ratings", msg), new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    log.info("kafka msg send success >>> " + msg);
                }
            });
        }
        log.info("send completed");

        result.put("flag", flag);
        return result;
    }

    @RequestMapping("/userInfo")
    @ResponseBody
    Map<String, Object> insertUserInfo(@RequestParam("id") String id){
        Map<String, Object> result = new HashMap<String, Object>();
        int flag = insert.insertUserInfo(id) ? 1:-1;

        result.put("flag", flag);
        return result;
    }
}
