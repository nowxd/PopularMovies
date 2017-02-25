package org.nowxd.popularmovies.data;


import org.json.JSONException;
import org.json.JSONObject;

public class Review {

    /*
      "id": "5723a329c3a3682e720005db",
      "author": "elshaarawy",
      "content": "very good movie 9.5/10",
      "url": "https://www.themoviedb.org/review/5723a329c3a3682e720005db"
     */

    private String author;
    private String content;
    private String url;

    private static final String AUTHOR_KEY = "author";
    private static final String CONTENT_KEY = "content";
    private static final String URL_KEY = "url";

    private static final int MAX_CONTENT_LEN = 300;

    public Review(JSONObject jsonObject) {

        try {

            this.author = jsonObject.getString(AUTHOR_KEY);
            this.content = truncate(jsonObject.getString(CONTENT_KEY));
            this.url = jsonObject.getString(URL_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Limit the amount of content shown
    private String truncate(String content) {

        if (content.length() <= MAX_CONTENT_LEN) return content;
        return content.substring(0, MAX_CONTENT_LEN) + "...";

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Review{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
