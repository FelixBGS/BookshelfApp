package com.example.bookshelf.network

import com.example.bookshelf.model.Items
import com.example.bookshelf.model.VolumeInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface BookshelfApiService {
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") endpoint: String,
        @Query("key") apiKey: String,
        @Query("maxResults") maxResults: Int = 12
    ): Items
}