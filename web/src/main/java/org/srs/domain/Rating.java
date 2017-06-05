package org.srs.domain;

/**
 * Created by 健勤 on 2017/3/22.
 */
public class Rating {
    private String userId;
    private String movieId;
    private double rating;

    public Rating() {
    }

    public Rating(String key, Double rating) {
        String userId = key.split(":")[0];
        String movieId = key.split(":")[1];

        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public Rating(int userId, int movieId, double rating) {
        this(String.valueOf(userId), String.valueOf(movieId), rating);
    }

    public Rating(String userId, String movieId, double rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating)) return false;

        Rating rating1 = (Rating) o;

        if (Double.compare(rating1.rating, rating) != 0) return false;
        if (userId != null ? !userId.equals(rating1.userId) : rating1.userId != null) return false;
        return !(movieId != null ? !movieId.equals(rating1.movieId) : rating1.movieId != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (movieId != null ? movieId.hashCode() : 0);
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
