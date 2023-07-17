package com.mandiri.test.ui.detailmovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandiri.test.common.Resource
import com.mandiri.test.model.MoviesModel
import com.mandiri.test.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    private val repo: MoviesRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    private val _detailMovie = MutableStateFlow<Resource<MoviesModel>>(Resource.Loading())
    val detailMovie: StateFlow<Resource<MoviesModel>>
        get() = _detailMovie

    fun getDetailMovie(movieID:Int) {
        viewModelScope.launch {
            repo.getMovieDetails(movieID).collect {
                _detailMovie.value = it
            }
        }
        _isRefreshing.value = false
    }

}