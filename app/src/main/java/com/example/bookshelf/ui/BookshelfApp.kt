package com.example.bookshelf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.R
import com.example.bookshelf.ui.screens.BookshelfViewModel
import com.example.bookshelf.ui.screens.HomeScreen

@Composable
fun BookshelfApp(
    modifier: Modifier = Modifier
) {
    val bookshelfViewModel: BookshelfViewModel = viewModel(factory = BookshelfViewModel.Factory)
    Scaffold(
        topBar = {
            if (!bookshelfViewModel.uiState.collectAsState().value.isExpandedView) {
                BookshelfTopBar(
                    amountInput = bookshelfViewModel.uiState.collectAsState().value.currentSearch,
                    onValueChange = { bookshelfViewModel.updateCurrentSearch(it) },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
        },
        modifier = modifier
    ) {
        contentPadding ->
        HomeScreen(
            contentPadding = contentPadding,
            bookshelfUiState = bookshelfViewModel.uiState.collectAsState().value,
            onCardClick = { bookshelfViewModel.updateToExpandedView(it) },
            onBackClicked = { bookshelfViewModel.updateToGridView() }
        )
    }
}

@Composable
fun BookshelfTopBar(
    modifier: Modifier = Modifier,
    amountInput: String,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(Color.White),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = amountInput,
            onValueChange = onValueChange,
            label = {
                Text(text = stringResource(id = R.string.search_book))
            },
            modifier = modifier
        )
    }
}