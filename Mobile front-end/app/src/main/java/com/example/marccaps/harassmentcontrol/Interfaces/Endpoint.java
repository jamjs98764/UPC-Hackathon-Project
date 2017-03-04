package com.example.marccaps.harassmentcontrol.Interfaces;

import com.example.marccaps.harassmentcontrol.Response;
import com.sendbird.android.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by MarcCaps on 04/03/2017.
 */

public interface Endpoint {

    @FormUrlEncoded
    @POST("message")
    Call<Response> analizeMessage(@Body String message);
}
