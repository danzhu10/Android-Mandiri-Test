package com.mandiri.test.remote

import com.mandiri.test.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("genre/movie/list")
    suspend fun getGenreList(
    ): Response<GenreResponse>

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("page") page: Int,
        @Query("with_genres") genreID: String,
    ): Response<PagingResponse<MoviesModel>>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieID: Int,
        @Query("append_to_response") data: String = "videos"
    ): Response<MoviesModel>

    @GET("movie/{movie_id}/reviews")
    suspend fun getUserReviews(
        @Path("movie_id") movieID: Int,
        @Query("page") page: Int
    ): Response<PagingResponse<ReviewModel>>

}