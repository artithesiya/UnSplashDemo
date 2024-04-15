package com.example.unsplashdemo.service

import com.example.unsplashdemo.model.UnsplashPhoto
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("collections/317099/photos")
    suspend fun getPhotos(
        @Query("client_id") clientId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<UnsplashPhoto>
}