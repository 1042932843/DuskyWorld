package com.dusky.world.Net.API;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Name: LoginRegisterService
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //TODO
 * Date: 2017-09-30 16:33
 */

public interface LoginRegisterService {

    @POST("login")
    @FormUrlEncoded
    Observable<ResponseBody> login(@Field("account") String account,
                                   @Field("password") String password);

    @POST("updateUser")
    @FormUrlEncoded
    Observable<ResponseBody> reset(@Field("id") String id,
                                   @Field("oldpassword") String oldpassword,
                                   @Field("newpassword") String newpassword);

}
