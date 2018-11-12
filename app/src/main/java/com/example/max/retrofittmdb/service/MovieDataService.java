package com.example.max.retrofittmdb.service;

import com.example.max.retrofittmdb.model.MovieDBResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDataService {

    @GET("movie/popular")
    Call<MovieDBResponse> getPopularMovies(@Query("api_key")String apiKey);

    @GET("/3/search/movie")
    Call<MovieDBResponse> getPopularMoviesSearch(@Query("api_key")String apiKey, @Query("query") String movie_string);

}
