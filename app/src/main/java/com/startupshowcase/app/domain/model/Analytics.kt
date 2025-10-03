package com.startupshowcase.app.domain.model

data class CompanyAnalytics(
    val companyId: String = "",
    val profileViews: Int = 0,
    val profileViewsGrowth: Double = 0.0,
    val totalFollowers: Int = 0,
    val followersGrowth: Double = 0.0,
    val reelViews: Int = 0,
    val reelViewsGrowth: Double = 0.0,
    val totalLikes: Int = 0,
    val totalShares: Int = 0,
    val engagementRate: Double = 0.0,
    val topPerformingReels: List<ReelPerformance> = emptyList(),
    val viewsByDate: Map<String, Int> = emptyMap(),
    val demographicData: DemographicData? = null
)

data class ReelPerformance(
    val reelId: String = "",
    val title: String = "",
    val thumbnailUrl: String = "",
    val views: Int = 0,
    val likes: Int = 0,
    val shares: Int = 0,
    val engagementRate: Double = 0.0
)

data class DemographicData(
    val ageGroups: Map<String, Int> = emptyMap(),
    val topCountries: Map<String, Int> = emptyMap(),
    val topCities: Map<String, Int> = emptyMap()
)
