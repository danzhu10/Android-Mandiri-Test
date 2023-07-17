package com.mandiri.test.ui.review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mandiri.test.R
import com.mandiri.test.compose.DefaultLoading
import com.mandiri.test.compose.ErrorView
import com.mandiri.test.compose.SpacingH5
import com.mandiri.test.compose.TopBarNormal
import com.mandiri.test.ui.theme.contentTextStyle
import com.mandiri.test.ui.theme.dateTextStyle
import com.mandiri.test.ui.theme.titleTextStyle

@Composable
fun UserReviewScreen(
    navHostController: NavHostController,
    movieID: Int,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    viewModel.setMovieID(movieID)
    val reviewState = viewModel.getReviews.collectAsLazyPagingItems()

    Scaffold(topBar = { TopBarNormal(title = "Review", navController = navHostController) }) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {

            },
        ) {
            if (reviewState.loadState.refresh is LoadState.Loading) {
                DefaultLoading()
            }


            LazyColumn(
                contentPadding = it,
                modifier = Modifier.padding(10.dp)
            ) {
                items(reviewState) { review ->
                    review?.let {
                        CommentItem(
                            it.author,
                            it.created_at,
                            it.author_details.avatar_path,
                            it.content
                        )
                    }
                }
                if (reviewState.loadState.append is LoadState.Loading) {
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
                if (reviewState.loadState.append is LoadState.Error) {
                    val state = reviewState.loadState.append as LoadState.Error
                    val errorMessage = state.error.localizedMessage ?: "unknown error occurred"
                    item {
                        ErrorView(errorMessage) {
                            reviewState.retry()
                        }
                    }
                }
            }
            if (reviewState.loadState.refresh is LoadState.Error) {
                val state = reviewState.loadState.refresh as LoadState.Error
                val errorMessage = state.error.localizedMessage ?: "unknown error occurred"
                ErrorView(errorMessage) {
                    reviewState.retry()
                }
            }
        }
    }
}

@Composable
fun CommentItem(
    name: String,
    date: String = "",
    photo: String = "",
    content: String = "",
) {
    val context = LocalContext.current
    val photoUrl = if (photo == null) {
        "https://secure.gravatar.com/avatar/1kks3YnVkpyQxzw36CObFPvhL5f.jpg"
    } else {
        "https://secure.gravatar.com/avatar$photo"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(modifier = Modifier.height(80.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(photoUrl)
                        .crossfade(true)
                        .size(Size.ORIGINAL)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    error = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(70.dp)
                        .fillMaxHeight()
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 7.dp)
                    .height(80.dp)
            ) {
                Column {
                    Text(text = name, style = titleTextStyle)
                    SpacingH5()
                    Text(
                        text = content, maxLines = 2,
                        style = contentTextStyle,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(Modifier.align(Alignment.BottomStart)) {
                    Text(
                        date, style = dateTextStyle
                    )
                }
            }
        }
    }
}