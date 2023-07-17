package com.mandiri.test.remote

import com.mandiri.test.common.Resource
import com.mandiri.test.common.getResponse
import com.mandiri.test.model.GenreResponse
import com.mandiri.test.model.MoviesModel
import com.mandiri.test.remote.ApiService
import javax.inject.Inject

class MoviesDatasource @Inject constructor(val apiService: ApiService) {

    suspend fun getGenreList(): Resource<GenreResponse> {
        return getResponse(
            request = { apiService.getGenreList() }
        )
    }

    suspend fun getMovieDetail(movieID:Int): Resource<MoviesModel> {
        return getResponse(
            request = { apiService.getMovieDetail(movieID)}
        )
    }

}