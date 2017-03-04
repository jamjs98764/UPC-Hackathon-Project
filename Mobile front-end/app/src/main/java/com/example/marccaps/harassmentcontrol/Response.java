package com.example.marccaps.harassmentcontrol;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MarcCaps on 04/03/2017.
 */

public class Response {

    @SerializedName("response")
    int mResponse;

    public Response(int code) {
        this.mResponse = code;
    }

}
