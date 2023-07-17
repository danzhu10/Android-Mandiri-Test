package com.mandiri.test.ui.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandiri.test.common.Resource
import com.mandiri.test.model.GenreResponse
import com.mandiri.test.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val repo: MoviesRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    private val _genreMovie = MutableStateFlow<Resource<GenreResponse>>(Resource.Loading())
    val genreMovie: StateFlow<Resource<GenreResponse>>
        get() = _genreMovie

    init {
        getGenreList()
    }

    fun getGenreList() =
        viewModelScope.launch {
            repo.getGenreList().collect {
                _genreMovie.value = it
            }
        }
}