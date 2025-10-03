package com.startupshowcase.app.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.startupshowcase.app.data.repository.CompanyRepositoryImpl
import com.startupshowcase.app.data.repository.ReelRepositoryImpl
import com.startupshowcase.app.domain.repository.CompanyRepository
import com.startupshowcase.app.domain.repository.ReelRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
    
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
    
    @Provides
    @Singleton
    fun provideCompanyRepository(
        firestore: FirebaseFirestore
    ): CompanyRepository {
        return CompanyRepositoryImpl(firestore)
    }
    
    @Provides
    @Singleton
    fun provideReelRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): ReelRepository {
        return ReelRepositoryImpl(firestore, storage)
    }
}
