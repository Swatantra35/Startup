package com.startupshowcase.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reel(
    val id: String = "",
    val companyId: String = "",
    val companyName: String = "",
    val companyLogo: String = "",
    val title: String = "",
    val description: String = "",
    val videoUrl: String = "",
    val thumbnailUrl: String = "",
    val duration: Long = 0L, // in seconds
    val category: ReelCategory = ReelCategory.PRODUCT_LAUNCH,
    val tags: List<String> = emptyList(),
    val likes: Int = 0,
    val views: Int = 0,
    val shares: Int = 0,
    val comments: Int = 0,
    val isLikedByCurrentUser: Boolean = false,
    val createdAt: Long = 0L,
    val isActive: Boolean = true
) : Parcelable

enum class ReelCategory {
    PRODUCT_LAUNCH,
    INNOVATION,
    STARTUP_STORY,
    BEHIND_THE_SCENES,
    TUTORIAL,
    CASE_STUDY,
    COMPANY_CULTURE,
    ANNOUNCEMENT,
    TESTIMONIAL,
    EVENT
}
