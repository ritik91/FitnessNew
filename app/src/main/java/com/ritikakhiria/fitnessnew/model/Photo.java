package com.ritikakhiria.fitnessnew.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("highres")
    @Expose
    private Object highres;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
