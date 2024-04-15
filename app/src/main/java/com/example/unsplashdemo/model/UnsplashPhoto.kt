package com.example.unsplashdemo.model

data class UnsplashPhoto(val id: String, val urls: UnsplashPhotoUrls)

data class UnsplashPhotoUrls(val regular: String)
