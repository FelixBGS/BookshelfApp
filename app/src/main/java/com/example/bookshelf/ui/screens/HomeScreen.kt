package com.example.bookshelf.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.model.Item
import com.example.bookshelf.model.VolumeInfo


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    bookshelfUiState: BookshelfUiState,
    onCardClick: (VolumeInfo) -> Unit,
    onBackClicked: () -> Unit
) {
   when (bookshelfUiState.networkState) {
        is NetworkState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is NetworkState.Success -> {
            BooksGridScreen(
                books = bookshelfUiState.networkState.books.items,
                contentPadding = contentPadding,
                isExpandedView = bookshelfUiState.isExpandedView,
                onCardClick = onCardClick,
                currentBook = bookshelfUiState.currentBook,
                onBackClicked = onBackClicked,
                modifier = modifier.fillMaxSize(),
            )
        }
        is NetworkState.NoSearchResult -> NoSearchResultScreen(modifier = modifier.fillMaxSize())
        is NetworkState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(200.dp)
            .background(Color.White),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = null
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun NoSearchResultScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.loop_img), contentDescription = null
        )
        Text(text = stringResource(R.string.no_search_results), modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun BooksGridScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    books: List<Item>,
    isExpandedView: Boolean,
    currentBook: VolumeInfo?,
    onCardClick: (VolumeInfo) -> Unit,
    onBackClicked: () -> Unit
) {
    if (isExpandedView) {
        ExpandedBookItem(
            book = currentBook!!,
            onBackClicked = onBackClicked
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = contentPadding,
            modifier = modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            items(books) { book ->
                BookItem(
                    book = book.volumeInfo,
                    modifier = Modifier
                        .height(180.dp)
                        .width(80.dp),
                    onCardClick = { onCardClick(book.volumeInfo) }
                )
            }
        }
    }
}

@Composable
fun ExpandedBookItem(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    book: VolumeInfo
) {
    BackHandler {
        onBackClicked()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (book.imageLinks != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.imageLinks.thumbnail?.replace("http", "https"))
                    .build(),
                placeholder = painterResource(id = R.drawable.book_placeholder),
                contentDescription = book.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(220.dp)
                    .width(120.dp)
                    .padding(top = 10.dp),
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.book_placeholder),
                contentDescription = book.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(220.dp)
                    .width(120.dp),
            )
        }
        Column(
            modifier = Modifier
        ) {
            Text(
                text = book.title ?: "",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = book.authors?.joinToString() ?: "",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            Text(
                text = book.description ?: "",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BookItem (
    modifier: Modifier = Modifier,
    book: VolumeInfo,
    onCardClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onCardClick
    ) {
        if (book.imageLinks != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.imageLinks.thumbnail?.replace("http", "https"))
                    .build(),
                placeholder = painterResource(id = R.drawable.book_placeholder),
                contentDescription = book.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.book_placeholder),
                contentDescription = book.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandedBookItemPreview() {
    ExpandedBookItem(
        book = VolumeInfo(
            title = "title",
            description = "description",
            authors = listOf("author1", "author2"),
            imageLinks = null,
        ),
        onBackClicked = {}
    )
}