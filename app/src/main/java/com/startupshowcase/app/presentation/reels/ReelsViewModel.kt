package com.startupshowcase.app.presentation.reels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startupshowcase.app.domain.model.Reel
import com.startupshowcase.app.domain.repository.ReelRepository
import com.startupshowcase.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReelsViewModel @Inject constructor(
    private val reelRepository: ReelRepository
) : ViewModel() {
    
    private val _reels = MutableStateFlow<List<Reel>>(emptyList())
    val reels: StateFlow<List<Reel>> = _reels.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private var currentPage = 0
    private var isLoadingMore = false
    
    fun loadReels() {
        viewModelScope.launch {
            _isLoading.value = true
            reelRepository.getReels(limit = 20).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _reels.value = result.data ?: emptyList()
                        _isLoading.value = false
                        currentPage = 1
                    }
                    is Resource.Error -> {
                        _error.value = result.message
                        _isLoading.value = false
                    }
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }
    
    fun loadMoreReels() {
        if (isLoadingMore) return
        
        viewModelScope.launch {
            isLoadingMore = true
            reelRepository.getReels(limit = 20).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val currentReels = _reels.value.toMutableList()
                        result.data?.let { newReels ->
                            currentReels.addAll(newReels)
                            _reels.value = currentReels
                        }
                        isLoadingMore = false
                        currentPage++
                    }
                    is Resource.Error -> {
                        isLoadingMore = false
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }
    
    fun toggleLike(reelId: String) {
        viewModelScope.launch {
            val reel = _reels.value.find { it.id == reelId }
            if (reel?.isLikedByCurrentUser == true) {
                reelRepository.unlikeReel(reelId).collect { }
            } else {
                reelRepository.likeReel(reelId).collect { }
            }
            
            // Update local state
            _reels.value = _reels.value.map { r ->
                if (r.id == reelId) {
                    r.copy(
                        isLikedByCurrentUser = !r.isLikedByCurrentUser,
                        likes = if (r.isLikedByCurrentUser) r.likes - 1 else r.likes + 1
                    )
                } else r
            }
        }
    }
    
    fun onReelViewed(position: Int) {
        viewModelScope.launch {
            val reel = _reels.value.getOrNull(position)
            reel?.let {
                reelRepository.incrementViews(it.id).collect { }
            }
        }
    }
}
