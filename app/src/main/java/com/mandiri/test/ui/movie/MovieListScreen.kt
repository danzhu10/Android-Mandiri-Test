package com.mandiri.test.ui.movie

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.mandiri.test.R
import com.mandiri.test.Screens
import com.mandiri.test.common.Constants
import com.mandiri.test.compose.DefaultLoading
import com.mandiri.test.compose.ErrorView
import com.mandiri.test.compose.SpacingH5
import com.mandiri.test.compose.TopBarNormal
import com.mandiri.test.ui.theme.dateTextStyle
import com.mandiri.test.ui.theme.titleTextStyle
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("SuspiciousIndentation", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieListScreen(
    navHostController: NavHostController,
    genre: String,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    Timber.d("ini genre $genre")
    val coroutineScope = rememberCoroutineScope()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.setGenre(genre)
    }
    val genreState = viewModel.genreFlow.collectAsLazyPagingItems()

    Scaffold(topBar = { TopBarNormal(title = "Movies", navController = navHostController) }) {

        if (genreState.loadState.refresh is LoadState.Loading) {
            DefaultLoading()
        }

        LazyColumn(contentPadding = it) {
            item {
                SpacingH5()
            }
            items(genreState) { movie ->
                MovieItem(
                    title = movie!!.title,
                    releaseDate = movie.release_date,
                    poster = movie.poster_path
                ) {
                    navHostController.navigate(
                        Screens.DetailMovieScreen.withArgs(
                            args = mapOf(
                                Constants.MOVIE_ID to movie.id.toString(),
                            )
                        )
                    )
                }
            }
            if (genreState.loadState.append is LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(80.dp)
                            .padding(16.dp)
                            .wrapContentHeight()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        strokeWidth = 4.5.dp
                    )
                }
            }
            if (genreState.loadState.append is LoadState.Error) {
                val state = genreState.loadState.append as LoadState.Error
                val errorMessage = state.error.localizedMessage ?: "unknown error occurred"
                item {
                    ErrorView(errorMessage) {
                        genreState.retry()
                    }
                }
            }
        }
        if (genreState.loadState.refresh is LoadState.Error) {
            val state = genreState.loadState.refresh as LoadState.Error
            val errorMessage = state.error.localizedMessage ?: "unknown error occurred"
            ErrorView(errorMessage) {
                genreState.retry()
            }
        }
    }

}

@Composable
fun MovieItem(
    title: String,
    releaseDate: String = "",
    poster: String = "",
    onClickItem: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickItem.invoke()
            }
            .padding(bottom = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(modifier = Modifier.height(80.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(Constants.POSTER_HOST+poster)
                        .crossfade(true)
                        .size(Size.ORIGINAL)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    error = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .fillMaxHeight()
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 7.dp)
                    .height(80.dp)
            ) {
                Column() {
                    Text(text = title, style = titleTextStyle)
                }
                Row(Modifier.align(Alignment.BottomStart)) {
                    Text(
                        releaseDate, style = dateTextStyle
                    )
                }
            }
        }
    }
}