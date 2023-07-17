package com.mandiri.test.repository

import androidx.paging.PagingData
import com.mandiri.test.common.Resource
import com.mandiri.test.model.GenreResponse
import com.mandiri.test.model.MoviesModel
import com.mandiri.test.model.ReviewModel
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getGenreList(): Flow<Resource<GenreResponse>>
    fun getMovieDetails(movieID: Int): Flow<Resource<MoviesModel>>
    fun getMoviesByGenre(genre: String): Flow<PagingData<MoviesModel>>
    fun getUserReview(movieID: Int): Flow<PagingData<ReviewModel>>

}