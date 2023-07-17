package com.mandiri.test.ui.genre

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mandiri.test.Screens
import com.mandiri.test.common.Constants
import com.mandiri.test.common.Resource
import com.mandiri.test.compose.DefaultLoading
import com.mandiri.test.compose.FailedComposable
import com.mandiri.test.compose.SpacingH5
import com.mandiri.test.compose.TopBarNormal
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun GenreListScreen(
    navHostController: NavHostController,
    viewModel: GenreViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val getGenre: () -> Unit = {
        coroutineScope.launch {
            viewModel.getGenreList()
        }
    }
    val genreState = viewModel.genreMovie.collectAsState()
    Scaffold(topBar = { TopBarNormal(title = "Genre", navController = navHostController, useBack = false) }) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                getGenre()
            },
        ) {
            val scrollState = rememberScrollState()
            when (genreState.value) {
                is Resource.Success -> {
                    val data = genreState.value.data!!.genres
                    Column(
                        Modifier
                            .padding(it)
                            .verticalScroll(scrollState)
                    ) {
                        SpacingH5()
                        data.forEach {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navHostController.navigate(
                                        Screens.MovieListByGenreScreen.withArgs(
                                            args = mapOf(
                                                Constants.GENRE to it.id,
                                            )
                                        )
                                    )
                                }) {
                                Text(
                                    modifier = Modifier.padding(start = 5.dp, top = 7.dp, bottom = 7.dp),
                                    text = it.name,
                                )
                            }
                        }
                    }
                }

                is Resource.Error -> FailedComposable(
                    errorMessage = "Error. Silahkan coba lagi",
                    retry = { getGenre() })

                else -> DefaultLoading()
            }
        }
    }
}