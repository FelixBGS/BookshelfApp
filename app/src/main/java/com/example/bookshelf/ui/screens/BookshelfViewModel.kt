package com.example.bookshelf.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.network.HttpException
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.model.VolumeInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import java.io.IOException

class BookshelfViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BookshelfUiState())
    val uiState: StateFlow<BookshelfUiState> = _uiState.asStateFlow()

    init {
        getBooks()
    }


    private fun updateNetworkState(updatedNetworkState: NetworkState) {
        _uiState.update { currentState ->
            currentState.copy(
                networkState = updatedNetworkState
            )
        }
        Log.d("MyTag", "updateNetworkState: $updatedNetworkState")
    }

    fun updateCurrentSearch(currentSearch: String) {
        _uiState.update { currentState ->
            currentState.copy(
                currentSearch = currentSearch
            )
        }
        Log.d("MyTag", "updateCurrentSearch: $currentSearch")
        getBooks()
    }

    fun updateToExpandedView(currentBook: VolumeInfo) {
        _uiState.update { currentState ->
            currentState.copy(
                currentBook = currentBook,
                isExpandedView = true
            )
        }
    }

    fun updateToGridView() {
        _uiState.update { currentState ->
            currentState.copy(
                isExpandedView = false
            )
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun getBooks() {
        viewModelScope.launch {
            try {
                delay(300)
                if (_uiState.value.currentSearch.isNotBlank()) {
                    updateNetworkState(NetworkState.Success(bookshelfRepository.getBooks(_uiState.value.currentSearch)))
                    Log.d("MyTag", "Catch in if")
                } else {
                    updateNetworkState(NetworkState.NoSearchResult)
                    Log.d("MyTag", "Catch in else")
                }
            } catch (e: IOException) {
                updateNetworkState(NetworkState.Error)
            } catch (e: MissingFieldException) {
                updateNetworkState(NetworkState.NoSearchResult)
                Log.d("MyTag", "Catch in catch")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                BookshelfViewModel(bookshelfRepository = bookshelfRepository)
            }
        }
    }
}