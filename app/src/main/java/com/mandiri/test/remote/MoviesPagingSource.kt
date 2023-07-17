package com.mandiri.test.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mandiri.test.model.MoviesModel
import com.mandiri.test.model.ReviewModel

class MoviesPagingSource(private val service: ApiService, val genre: String) :
    PagingSource<Int, MoviesModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesModel> {
        val pageNumber = params.key ?: 1

        return try {
            val response = service.getMoviesByGenre(pageNumber, genre)
            val datas = response.body()
            LoadResult.Page(
                data = datas!!.results,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (pageNumber == datas.total_pages) null else pageNumber + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MoviesModel>): Int = 1
}

class ReviewPagingSource(private val service: ApiService, val id: Int) :
    PagingSource<Int, ReviewModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewModel> {
        val pageNumber = params.key ?: 1

        return try {
            val response = service.getUserReviews(id, pageNumber)
            val datas = response.body()
            if (datas!!.results.size > 0) {
                LoadResult.Page(
                    data = datas.results,
                    prevKey = if (pageNumber == 1) null else pageNumber - 1,
                    nextKey = if (pageNumber == datas.total_pages) null else pageNumber + 1
                )
            } else {
                LoadResult.Error(Throwable("No Review at this time"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ReviewModel>): Int = 1
}