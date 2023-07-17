package com.mandiri.test.ui.review

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
class ReviewViewModel @Inject constructor(
    private val repo: MoviesRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private val _reviewFlow = MutableStateFlow(0)

    fun setMovieID(id: Int) {
        _reviewFlow.value = id
    }

    val getReviews = _reviewFlow
        .filterNotNull() // Skip our default value
        .distinctUntilChanged() // Don't return a new value if the category hasn't changed
        .flatMapLatest { id ->
            repo.getUserReview(id)
        }.cachedIn(viewModelScope)

}