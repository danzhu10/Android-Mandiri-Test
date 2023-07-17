package com.mandiri.test.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mandiri.test.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    repo: MoviesRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private val _genreFlow = MutableStateFlow("")

    fun setGenre(genre: String) {
        _genreFlow.value = genre
    }

//    val genreFlow = repo.getMoviesByGenre().cachedIn(viewModelScope)

    val genreFlow = _genreFlow
        .filterNotNull() // Skip our default value
        .distinctUntilChanged() // Don't return a new value if the category hasn't changed
        .flatMapLatest { genre ->
            repo.getMoviesByGenre(genre)
        }.cachedIn(viewModelScope)

}