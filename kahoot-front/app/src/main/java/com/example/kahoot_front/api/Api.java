package com.example.kahoot_front.api;

import com.example.kahoot_front.model.LoginModel;
import com.example.kahoot_front.model.RegisterModel;
import com.example.kahoot_front.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    @POST("/login")
    Call<User> loginUser(@Body LoginModel loginModel);

    @POST("/register")
    Call<User> registerUser(@Body RegisterModel registerModel);

    @Multipart
    @POST("/upload")
    Call<String> uploadImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("/notfound")
    Call<String> notFound();

    @GET("/test")
    Call<String> test();

}
