package com.startupshowcase.app.domain.repository

import com.startupshowcase.app.domain.model.User
import com.startupshowcase.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    
    suspend fun signIn(email: String, password: String): Flow<Resource<User>>
    
    suspend fun signUp(email: String, password: String, displayName: String): Flow<Resource<User>>
    
    suspend fun signInWithGoogle(idToken: String): Flow<Resource<User>>
    
    suspend fun signOut(): Flow<Resource<Unit>>
    
    suspend fun getCurrentUser(): Flow<Resource<User?>>
    
    suspend fun updateUserProfile(user: User): Flow<Resource<Unit>>
    
    suspend fun resetPassword(email: String): Flow<Resource<Unit>>
    
    fun isUserLoggedIn(): Boolean
}
