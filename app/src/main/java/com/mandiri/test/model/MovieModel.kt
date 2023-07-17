package com.mandiri.test.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PagingResponse<T>(
    val page: Int,
    val results: MutableList<T>,
    val total_pages: Int,
    val total_results: Int
)

@Parcelize
data class MoviesModel(
    val id: Int,
    val original_title: String = "",
    val adult: String = "",
    val title: String = "",
    var overview: String = "",
    var release_date:String,
    var poster_path: String,
    val videos: VideosResponse

) : Parcelable

@Parcelize
data class VideosResponse(
    val results: ArrayList<VideoModel>,
) : Parcelable

@Parcelize
data class VideoModel(
    val name: String,
    val key:String,
    val published_at:String,
    val site:String
) : Parcelable

@Parcelize
data class ReviewModel(
    val author: String = "",
    val author_details: AuthorModel,
    val content: String = "",
    val created_at: String = ""
) : Parcelable

@Parcelize
data class AuthorModel(
    val avatar_path: String = "",
    val rating: Double,
) : Parcelable

@Parcelize
data class GenreResponse(
    val genres: ArrayList<GenreModel>
) : Parcelable

@Parcelize
data class GenreModel(
    val id: String = "",
    val name: String = "",

    ) : Parcelable