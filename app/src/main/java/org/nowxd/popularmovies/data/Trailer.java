package org.nowxd.popularmovies.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Trailer {

    /*
    "id": "581f8244c3a3685550002fa3",
            "iso_639_1": "en",
            "iso_3166_1": "US",
            "key": "EZ-zFwuR0FY",
            "name": "HD Trailer",
            "site": "YouTube",
            "size": 1080,
            "type": "Trailer"
    */

    private String videoKey;
    private String name;
    private String site;
    private String type;

    // JSON keys
    private static final String VIDEO_KEY = "key";
    private static final String NAME_KEY = "name";
    private static final String SITE_KEY = "site";
    private static final String TYPE_KEY = "type";

    public Trailer(JSONObject jsonObject) {

        try {

            this.videoKey = jsonObject.getString(VIDEO_KEY);
            this.name = jsonObject.getString(NAME_KEY);
            this.site = jsonObject.getString(SITE_KEY);
            this.type = jsonObject.getString(TYPE_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getVideoKey() {
        return videoKey;
    }

    public static String getNameKey() {
        return NAME_KEY;
    }

    public static String getSiteKey() {
        return SITE_KEY;
    }

    public static String getTypeKey() {
        return TYPE_KEY;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
