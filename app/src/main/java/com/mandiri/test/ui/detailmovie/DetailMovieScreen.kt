package com.mandiri.test.ui.detailmovie


import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mandiri.test.R
import com.mandiri.test.Screens
import com.mandiri.test.common.Constants
import com.mandiri.test.common.Resource
import com.mandiri.test.compose.DefaultLoading
import com.mandiri.test.compose.FailedComposable
import com.mandiri.test.compose.SpacingH10
import com.mandiri.test.compose.SpacingH5
import com.mandiri.test.compose.SpacingV10
import com.mandiri.test.compose.TopBarNormal
import com.mandiri.test.model.MoviesModel
import com.mandiri.test.model.VideosResponse
import com.mandiri.test.ui.theme.TempoRed
import com.mandiri.test.ui.theme.defaultTextStyle
import com.mandiri.test.ui.theme.titleTextStyle
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailMovieScreen(
    navHostController: NavHostController,
    movieID: Int,
    viewModel: DetailMovieViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val retry: () -> Unit = {
        coroutineScope.launch {
            viewModel.getDetailMovie(movieID)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getDetailMovie(movieID)
    }
    val detailState = viewModel.detailMovie.collectAsState()

    Scaffold(topBar = { TopBarNormal(title = "Detail Movie", navController = navHostController) }) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                retry()
            },
        ) {
            when (detailState.value) {
                is Resource.Success -> {
                    val data = detailState.value.data!!
                    Column(
                        Modifier
                            .padding(it)
                            .verticalScroll(rememberScrollState())
                    ) {
                        DetailContent(data, navHostController)
                    }
                }

                is Resource.Error -> FailedComposable(
                    errorMessage = "Error. Silahkan coba lagi",
                    retry = { })

                else ->  DefaultLoading()
            }
        }
    }
}

@Composable
fun DetailContent(data: MoviesModel, navHostController: NavHostController) {
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(Constants.POSTER_HOST + data.poster_path)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .build(),
        placeholder = painterResource(R.drawable.ic_launcher_background),
        error = painterResource(R.drawable.ic_launcher_background),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
    )
    Column(Modifier.padding(10.dp)) {
        Text(
            style = titleTextStyle,
            fontSize = 16.sp,
            text = data.title
        )
        SpacingH5()
        Text(
            style = defaultTextStyle,
            text = data.overview
        )
        SpacingH10()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(TempoRed),
                onClick = {
                    navHostController.navigate(
                        Screens.UserReviewScreen.withArgs(
                            args = mapOf(
                                Constants.MOVIE_ID to data.id.toString(),
                            )
                        )
                    )

                }) {
                Text(
                    style = defaultTextStyle,
                    fontSize = 15.sp,
                    text = "Review"
                )
            }
        }
        SpacingH10()
        Text(
            style = titleTextStyle,
            fontSize = 15.sp,
            text = "Trailer"
        )
        SpacingH5()
        TrailerVideo(video = data.videos, navHostController)
    }
}

@Composable
fun TrailerVideo(video: VideosResponse, navHostController: NavHostController) {
    video.results.forEach {
        VideoItemGrid(title = it.name, site = it.site, published = it.published_at) {
            navHostController.navigate(
                Screens.YoutubeScreen.withArgs(
                    args = mapOf(
                        Constants.YOUTUBE to it.key,
                    )
                )
            )
        }
    }
}

@Composable
fun VideoItemGrid(
    title: String = "",
    site: String,
    published: String,
    onClickItem: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 3.dp)
        .clickable {
            onClickItem()
        }) {
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .size(100.dp, 70.dp)
                    .border(border = BorderStroke(1.dp, Color.Black)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.ic_video)
                        .crossfade(true)
                        .size(Size.ORIGINAL)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_video),
                    error = painterResource(R.drawable.ic_video),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                )
            }
            SpacingV10()
            Column(verticalArrangement = Arrangement.Center) {
                Text(title, style = defaultTextStyle)
                Text(site, style = defaultTextStyle)
                Text(published, style = defaultTextStyle)
            }
        }
    }
}
