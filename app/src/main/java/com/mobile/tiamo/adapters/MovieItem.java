package com.mobile.tiamo.adapters;


/*
  A class of movie item
  This class is to populate for the list of the movie
 */
public class MovieItem {

    private String imdbID;
    private String Title;
    private String Year;
    private String Type;
    private String Poster;

    public MovieItem() {
    }

    public MovieItem(String imdbID, String title, String year, String type, String poster) {
        this.imdbID = imdbID;
        Title = title;
        Year = year;
        Type = type;
        Poster = poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }
}
