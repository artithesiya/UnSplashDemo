package com.example.unsplashdemo.service

import com.example.unsplashdemo.model.UnsplashPhoto
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("photos")
    suspend fun getPhotos(@Query("client_id") client_id:String,@Query("page") page: Int, @Query("per_page") perPage: Int): List<UnsplashPhoto>
}