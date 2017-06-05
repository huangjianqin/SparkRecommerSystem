package org.srs.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 健勤 on 2017/3/23.
 */
public class Predict {
    private String id;
    private List<Rating> predicts;

    public Predict() {
    }

    public Predict(String key, String predicts) {
        this.id = key.split(":")[0];
        this.predicts = new ArrayList<Rating>();

        for(String tuple2: predicts.split("::")){
            String userId = id;
            String movieId = tuple2.split("&")[0];
            String rating = tuple2.split("&")[1];
            this.predicts.add(new Rating(userId, movieId, Double.valueOf(rating)));
        }

        this.predicts.sort(new Comparator<Rating>() {
            public int compare(Rating o1, Rating o2) {
                if(o1.getRating() == o2.getRating()){
                    return 0;
                }
                else if(o1.getRating() > o2.getRating()){
                    return -1;
                }
                else{
                    return 1;
                }
            }
        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Rating> getPredicts() {
        return predicts;
    }

    public void setPredicts(List<Rating> predicts) {
        this.predicts = predicts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Predict)) return false;

        Predict predict = (Predict) o;

        return !(id != null ? !id.equals(predict.id) : predict.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
