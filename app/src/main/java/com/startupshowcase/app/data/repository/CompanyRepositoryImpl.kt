package com.startupshowcase.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.startupshowcase.app.domain.model.Company
import com.startupshowcase.app.domain.model.IndustryCategory
import com.startupshowcase.app.domain.repository.CompanyRepository
import com.startupshowcase.app.util.Constants
import com.startupshowcase.app.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CompanyRepository {
    
    private val companiesCollection = firestore.collection(Constants.COLLECTION_COMPANIES)
    
    override suspend fun getCompanies(
        category: IndustryCategory?,
        isFeatured: Boolean?,
        limit: Int
    ): Flow<Resource<List<Company>>> = callbackFlow {
        trySend(Resource.Loading())
        
        try {
            var query: Query = companiesCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
            
            if (category != null) {
                query = query.whereEqualTo("industry", category.name)
            }
            
            if (isFeatured != null) {
                query = query.whereEqualTo("isFeatured", isFeatured)
            }
            
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Unknown error"))
                    return@addSnapshotListener
                }
                
                val companies = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Company::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(Resource.Success(companies))
            }
            
            awaitClose { listener.remove() }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message ?: "Failed to load companies"))
        }
    }
    
    override suspend fun getCompanyById(id: String): Flow<Resource<Company>> = callbackFlow {
        trySend(Resource.Loading())
        
        try {
            val listener = companiesCollection.document(id)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Resource.Error(error.message ?: "Unknown error"))
                        return@addSnapshotListener
                    }
                    
                    val company = snapshot?.toObject(Company::class.java)?.copy(id = snapshot.id)
                    if (company != null) {
                        trySend(Resource.Success(company))
                    } else {
                        trySend(Resource.Error("Company not found"))
                    }
                }
            
            awaitClose { listener.remove() }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message ?: "Failed to load company"))
        }
    }
    
    override suspend fun searchCompanies(query: String): Flow<Resource<List<Company>>> = flow {
        emit(Resource.Loading())
        
        try {
            val snapshot = companiesCollection
                .orderBy("name")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .limit(20)
                .get()
                .await()
            
            val companies = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Company::class.java)?.copy(id = doc.id)
            }
            
            emit(Resource.Success(companies))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Search failed"))
        }
    }
    
    override suspend fun createCompany(company: Company): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        
        try {
            val docRef = companiesCollection.add(company).await()
            emit(Resource.Success(docRef.id))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to create company"))
        }
    }
    
    override suspend fun updateCompany(company: Company): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            companiesCollection.document(company.id)
                .set(company)
                .await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update company"))
        }
    }
    
    override suspend fun deleteCompany(companyId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            companiesCollection.document(companyId).delete().await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to delete company"))
        }
    }
    
    override suspend fun followCompany(companyId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            // Increment followers count
            companiesCollection.document(companyId)
                .update("followers", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to follow company"))
        }
    }
    
    override suspend fun unfollowCompany(companyId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            companiesCollection.document(companyId)
                .update("followers", com.google.firebase.firestore.FieldValue.increment(-1))
                .await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to unfollow company"))
        }
    }
    
    override suspend fun getFollowedCompanies(): Flow<Resource<List<Company>>> = flow {
        emit(Resource.Loading())
        // Implementation depends on how you track followed companies
        emit(Resource.Success(emptyList()))
    }
    
    override suspend fun incrementViews(companyId: String): Flow<Resource<Unit>> = flow {
        try {
            companiesCollection.document(companyId)
                .update("views", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to increment views"))
        }
    }
}
