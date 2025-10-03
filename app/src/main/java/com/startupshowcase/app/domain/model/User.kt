package com.startupshowcase.app.domain.model

data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val userType: UserType = UserType.VISITOR,
    val companyId: String? = null,
    val savedCompanies: List<String> = emptyList(),
    val likedReels: List<String> = emptyList(),
    val followedCompanies: List<String> = emptyList(),
    val createdAt: Long = 0L,
    val lastLogin: Long = 0L,
    val fcmToken: String = ""
)

enum class UserType {
    VISITOR,        // Regular user browsing companies
    COMPANY_OWNER,  // User who owns/manages a company
    ADMIN           // Platform administrator
}
