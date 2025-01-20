package com.example.bookshelf.ui.screens

import com.example.bookshelf.model.Items
import com.example.bookshelf.model.VolumeInfo

data class BookshelfUiState(
    val currentSearch: String = "",
    val networkState: NetworkState = NetworkState.Loading,
    val currentBook: VolumeInfo? = null,
    val isExpandedView: Boolean = false
)

sealed interface NetworkState {
    data class Success(val books: Items) : NetworkState
    object Loading : NetworkState
    object Error: NetworkState
    object NoSearchResult: NetworkState
}