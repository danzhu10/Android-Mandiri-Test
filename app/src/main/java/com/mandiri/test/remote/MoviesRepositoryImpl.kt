package com.mandiri.test.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mandiri.test.common.Resource
import com.mandiri.test.model.GenreResponse
import com.mandiri.test.model.MoviesModel
import com.mandiri.test.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val datasource: MoviesDatasource
) : MoviesRepository {

    override fun getGenreList(): Flow<Resource<GenreResponse>> {
        return flow {
            emit(Resource.Loading())
            emit(Resource.Success(datasource.getGenreList().data!!))
        }.flowOn(Dispatchers.IO)
            .catch {
                emit(Resource.Error(it, null))
            }
    }

    override fun getMovieDetails(movieID: Int): Flow<Resource<MoviesModel>> {
        return flow {
            emit(Resource.Loading())
            emit(Resource.Success(datasource.getMovieDetail(movieID).data!!))
        }.flowOn(Dispatchers.IO)
            .catch {
                emit(Resource.Error(it, null))
            }
    }

    override fun getMoviesByGenre(genre: String) = Pager(
        config = PagingConfig(
            pageSize = 50,
            prefetchDistance = 20, initialLoadSize = 1,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            Timber.d("Call Paging")
            MoviesPagingSource(datasource.apiService, genre)
        }
    ).flow

    override fun getUserReview(movieID: Int) = Pager(
        config = PagingConfig(
            pageSize = 50,
            prefetchDistance = 20, initialLoadSize = 1, enablePlaceholders = true
        ),
        pagingSourceFactory = {
            Timber.d("Call Paging")
            ReviewPagingSource(datasource.apiService, movieID)
        }
    ).flow
}