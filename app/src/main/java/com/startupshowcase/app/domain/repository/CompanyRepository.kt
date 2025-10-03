package com.startupshowcase.app.domain.repository

import com.startupshowcase.app.domain.model.Company
import com.startupshowcase.app.domain.model.IndustryCategory
import com.startupshowcase.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
    
    suspend fun getCompanies(
        category: IndustryCategory? = null,
        isFeatured: Boolean? = null,
        limit: Int = 20
    ): Flow<Resource<List<Company>>>
    
    suspend fun getCompanyById(id: String): Flow<Resource<Company>>
    
    suspend fun searchCompanies(query: String): Flow<Resource<List<Company>>>
    
    suspend fun createCompany(company: Company): Flow<Resource<String>>
    
    suspend fun updateCompany(company: Company): Flow<Resource<Unit>>
    
    suspend fun deleteCompany(companyId: String): Flow<Resource<Unit>>
    
    suspend fun followCompany(companyId: String): Flow<Resource<Unit>>
    
    suspend fun unfollowCompany(companyId: String): Flow<Resource<Unit>>
    
    suspend fun getFollowedCompanies(): Flow<Resource<List<Company>>>
    
    suspend fun incrementViews(companyId: String): Flow<Resource<Unit>>
}
