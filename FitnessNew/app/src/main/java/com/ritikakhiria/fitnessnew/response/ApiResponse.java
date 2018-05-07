package com.ritikakhiria.fitnessnew.response;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("level")
    int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
