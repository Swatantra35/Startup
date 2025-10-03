package com.startupshowcase.app.domain.repository

import com.startupshowcase.app.domain.model.Reel
import com.startupshowcase.app.domain.model.ReelCategory
import com.startupshowcase.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface ReelRepository {
    
    suspend fun getReels(
        category: ReelCategory? = null,
        limit: Int = 20
    ): Flow<Resource<List<Reel>>>
    
    suspend fun getReelById(id: String): Flow<Resource<Reel>>
    
    suspend fun getReelsByCompany(companyId: String): Flow<Resource<List<Reel>>>
    
    suspend fun uploadReel(reel: Reel, videoUri: String): Flow<Resource<String>>
    
    suspend fun updateReel(reel: Reel): Flow<Resource<Unit>>
    
    suspend fun deleteReel(reelId: String): Flow<Resource<Unit>>
    
    suspend fun likeReel(reelId: String): Flow<Resource<Unit>>
    
    suspend fun unlikeReel(reelId: String): Flow<Resource<Unit>>
    
    suspend fun incrementViews(reelId: String): Flow<Resource<Unit>>
    
    suspend fun shareReel(reelId: String): Flow<Resource<Unit>>
}
