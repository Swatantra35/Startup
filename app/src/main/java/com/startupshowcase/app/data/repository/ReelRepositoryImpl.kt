package com.startupshowcase.app.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.startupshowcase.app.domain.model.Reel
import com.startupshowcase.app.domain.model.ReelCategory
import com.startupshowcase.app.domain.repository.ReelRepository
import com.startupshowcase.app.util.Constants
import com.startupshowcase.app.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReelRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ReelRepository {
    
    private val reelsCollection = firestore.collection(Constants.COLLECTION_REELS)
    private val storageRef = storage.reference.child("reels")
    
    override suspend fun getReels(
        category: ReelCategory?,
        limit: Int
    ): Flow<Resource<List<Reel>>> = callbackFlow {
        trySend(Resource.Loading())
        
        try {
            var query: Query = reelsCollection
                .whereEqualTo("isActive", true)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
            
            if (category != null) {
                query = query.whereEqualTo("category", category.name)
            }
            
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Unknown error"))
                    return@addSnapshotListener
                }
                
                val reels = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Reel::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(Resource.Success(reels))
            }
            
            awaitClose { listener.remove() }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message ?: "Failed to load reels"))
        }
    }
    
    override suspend fun getReelById(id: String): Flow<Resource<Reel>> = callbackFlow {
        trySend(Resource.Loading())
        
        try {
            val listener = reelsCollection.document(id)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Resource.Error(error.message ?: "Unknown error"))
                        return@addSnapshotListener
                    }
                    
                    val reel = snapshot?.toObject(Reel::class.java)?.copy(id = snapshot.id)
                    if (reel != null) {
                        trySend(Resource.Success(reel))
                    } else {
                        trySend(Resource.Error("Reel not found"))
                    }
                }
            
            awaitClose { listener.remove() }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message ?: "Failed to load reel"))
        }
    }
    
    override suspend fun getReelsByCompany(companyId: String): Flow<Resource<List<Reel>>> = flow {
        emit(Resource.Loading())
        
        try {
            val snapshot = reelsCollection
                .whereEqualTo("companyId", companyId)
                .whereEqualTo("isActive", true)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val reels = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Reel::class.java)?.copy(id = doc.id)
            }
            
            emit(Resource.Success(reels))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load company reels"))
        }
    }
    
    override suspend fun uploadReel(reel: Reel, videoUri: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        
        try {
            // Upload video to Firebase Storage
            val videoFileName = "${UUID.randomUUID()}.mp4"
            val videoRef = storageRef.child(videoFileName)
            
            val uploadTask = videoRef.putFile(Uri.parse(videoUri)).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await().toString()
            
            // Create reel document with video URL
            val reelWithUrl = reel.copy(videoUrl = downloadUrl)
            val docRef = reelsCollection.add(reelWithUrl).await()
            
            emit(Resource.Success(docRef.id))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to upload reel"))
        }
    }
    
    override suspend fun updateReel(reel: Reel): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            reelsCollection.document(reel.id).set(reel).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update reel"))
        }
    }
    
    override suspend fun deleteReel(reelId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            reelsCollection.document(reelId)
                .update("isActive", false)
                .await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to delete reel"))
        }
    }
    
    override suspend fun likeReel(reelId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            reelsCollection.document(reelId)
                .update("likes", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to like reel"))
        }
    }
    
    override suspend fun unlikeReel(reelId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            reelsCollection.document(reelId)
                .update("likes", com.google.firebase.firestore.FieldValue.increment(-1))
                .await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to unlike reel"))
        }
    }
    
    override suspend fun incrementViews(reelId: String): Flow<Resource<Unit>> = flow {
        try {
            reelsCollection.document(reelId)
                .update("views", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to increment views"))
        }
    }
    
    override suspend fun shareReel(reelId: String): Flow<Resource<Unit>> = flow {
        try {
            reelsCollection.document(reelId)
                .update("shares", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to share reel"))
        }
    }
}
