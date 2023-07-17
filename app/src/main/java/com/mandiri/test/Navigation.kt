package com.mandiri.test

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.mandiri.test.common.Constants
import com.mandiri.test.ui.detailmovie.DetailMovieScreen
import com.mandiri.test.ui.detailmovie.YoutubeScreen
import com.mandiri.test.ui.genre.GenreListScreen
import com.mandiri.test.ui.movie.MovieListScreen
import com.mandiri.test.ui.movie.MoviesViewModel
import com.mandiri.test.ui.review.UserReviewScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController
) {

    AnimatedNavHost(
        navController = navController,
        startDestination = Screens.GenreListScren.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(
            Screens.GenreListScren.route
        ) {
            GenreListScreen(
                navController
            )
        }
        composable(
            Screens.MovieListByGenreScreen.route +
                    "?${Constants.GENRE}={genre}",
            arguments = listOf(
                navArgument(Constants.GENRE) { nullable = true })
        ) {
            MovieListScreen(
                navHostController = navController,
                genre = getStringArg("genre", it)
            )
        }
        composable(
            Screens.UserReviewScreen.route +
                    "?${Constants.MOVIE_ID}={id}",
            arguments = listOf(
                navArgument(Constants.MOVIE_ID) { nullable = true })
        ) {
            UserReviewScreen(
                navHostController = navController,
                movieID = getStringArg("id", it).toInt()
            )
        }
        composable(
            Screens.DetailMovieScreen.route +
                    "?${Constants.MOVIE_ID}={id}",
            arguments = listOf(
                navArgument(Constants.MOVIE_ID) { nullable = true })
        ) {
            DetailMovieScreen(
                navHostController = navController,
                movieID = getStringArg("id", it).toInt()
            )
        }
        composable(
            Screens.YoutubeScreen.route +
                    "?${Constants.YOUTUBE}={id}",
            arguments = listOf(
                navArgument(Constants.YOUTUBE) { nullable = true })
        ) {
            YoutubeScreen(
                navHostController = navController,
                youtubeID = getStringArg("id", it),
            )
        }

    }
}

fun getStringArg(key: String, entry: NavBackStackEntry) =
    entry.arguments?.getString(key).toString()