package com.mandiri.test

sealed class Screens(val route: String, val label: String, val icon: Int? = null) {
    object GenreListScren : Screens("GenreScreen", "Genre")
    object MovieListByGenreScreen : Screens("MovieListByGenreScreen", "Movies")
    object UserReviewScreen : Screens("UserReviewScreen", "User Review")
    object DetailMovieScreen : Screens("DetailMovieScreen", "Detail Movie")
    object YoutubeScreen : Screens("YoutubeScreen", "Youtube")

    fun withArgs(args: Map<String, String>): String {
        return buildString {
            append(route)
            append("?")
            val iterator = args.iterator()
            while (iterator.hasNext()) {
                val map = iterator.next()
                append(map.key + "=" + map.value)
                if (iterator.hasNext())
                    append("&")
            }
        }
    }
}
