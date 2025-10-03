package com.startupshowcase.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startupshowcase.app.domain.model.Company
import com.startupshowcase.app.domain.model.IndustryCategory
import com.startupshowcase.app.domain.repository.CompanyRepository
import com.startupshowcase.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {
    
    private val _featuredCompanies = MutableStateFlow<List<Company>>(emptyList())
    val featuredCompanies: StateFlow<List<Company>> = _featuredCompanies.asStateFlow()
    
    private val _companies = MutableStateFlow<List<Company>>(emptyList())
    val companies: StateFlow<List<Company>> = _companies.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private var currentCategory: IndustryCategory? = null
    
    fun loadFeaturedCompanies() {
        viewModelScope.launch {
            companyRepository.getCompanies(isFeatured = true, limit = 10).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _featuredCompanies.value = result.data ?: emptyList()
                    }
                    is Resource.Error -> {
                        _error.value = result.message
                    }
                    is Resource.Loading -> {
                        // Handle loading if needed
                    }
                }
            }
        }
    }
    
    fun loadCompanies() {
        viewModelScope.launch {
            _isLoading.value = true
            companyRepository.getCompanies(category = currentCategory).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _companies.value = result.data ?: emptyList()
                        _isLoading.value = false
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
    
    fun filterByCategory(category: IndustryCategory?) {
        currentCategory = category
        loadCompanies()
    }
    
    fun refreshData() {
        loadFeaturedCompanies()
        loadCompanies()
    }
    
    fun toggleFollow(companyId: String) {
        viewModelScope.launch {
            // Check if already following
            val company = _companies.value.find { it.id == companyId }
            if (company != null) {
                // Toggle follow status
                companyRepository.followCompany(companyId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            // Update local list
                            refreshData()
                        }
                        is Resource.Error -> {
                            _error.value = result.message
                        }
                        is Resource.Loading -> {}
                    }
                }
            }
        }
    }
}
