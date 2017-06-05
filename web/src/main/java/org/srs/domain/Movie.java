package org.srs.domain;

/**
 * Created by 健勤 on 2017/3/22.
 */
public class Movie {
    private String id;
    private String name;
    private String genres;

    public Movie() {
    }

    public Movie(int id, String name, String genres) {
        this(String.valueOf(id), name, genres);
    }

    public Movie(String id, String name, String genres) {
        this.id = id;
        this.name = name;
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;

        return !(id != null ? !id.equals(movie.id) : movie.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
