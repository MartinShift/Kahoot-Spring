package com.example.kahoot_front.api;

import com.example.kahoot_front.model.LoginModel;
import com.example.kahoot_front.model.Note;
import com.example.kahoot_front.model.RegisterModel;
import com.example.kahoot_front.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {

    @POST("/submitLogin")
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

    @POST("/notes")
    Call<Note> createNote(@Body Note note);

    @GET("/notes/{id}")
    Call<Note> getNoteById(@Path("id") int id);

    @GET("/notes")
    Call<List<Note>> getAllNotes();

    @PUT("/notes/{id}")
    Call<Note> updateNote(@Path("id") int id, @Body Note note);

    @DELETE("/notes/{id}")
    Call<Void> deleteNote(@Path("id") int id);

    @GET("/notes/user/{username}")
    Call<List<Note>> getByUser(@Path("username") String username);

}
