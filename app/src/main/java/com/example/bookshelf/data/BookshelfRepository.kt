package com.example.bookshelf.data

import android.icu.text.StringSearch
import com.example.bookshelf.model.Items
import com.example.bookshelf.model.VolumeInfo
import com.example.bookshelf.network.BookshelfApiService

const val API_KEY = ""

interface BookshelfRepository {
    suspend fun getBooks(currentSearch: String): Items
}

class NetworkBooksRepository(
    private val bookshelfApiService: BookshelfApiService
) : BookshelfRepository {
    override suspend fun getBooks(currentSearch: String): Items
    = bookshelfApiService.getBooks(currentSearch, API_KEY)
}