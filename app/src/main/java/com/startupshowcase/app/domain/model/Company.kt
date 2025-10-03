package com.startupshowcase.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Company(
    val id: String = "",
    val name: String = "",
    val tagline: String = "",
    val description: String = "",
    val logo: String = "",
    val coverImage: String = "",
    val industry: IndustryCategory = IndustryCategory.OTHER,
    val companySize: CompanySize = CompanySize.SMALL,
    val foundedYear: Int = 0,
    val website: String = "",
    val email: String = "",
    val phone: String = "",
    val address: Address? = null,
    val socialMedia: SocialMedia? = null,
    val products: List<Product> = emptyList(),
    val services: List<String> = emptyList(),
    val galleryImages: List<String> = emptyList(),
    val videos: List<String> = emptyList(),
    val reels: List<Reel> = emptyList(),
    val isFeatured: Boolean = false,
    val isVerified: Boolean = false,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val followers: Int = 0,
    val views: Int = 0,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val ownerId: String = ""
) : Parcelable

@Parcelize
data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val price: Double? = null,
    val currency: String = "USD",
    val category: String = "",
    val isAvailable: Boolean = true
) : Parcelable

@Parcelize
data class Address(
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val zipCode: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
) : Parcelable

@Parcelize
data class SocialMedia(
    val linkedin: String = "",
    val twitter: String = "",
    val facebook: String = "",
    val instagram: String = "",
    val youtube: String = ""
) : Parcelable

enum class IndustryCategory {
    TECHNOLOGY,
    FINTECH,
    HEALTHCARE,
    EDUCATION,
    ECOMMERCE,
    FOOD_BEVERAGE,
    FASHION,
    REAL_ESTATE,
    AUTOMOTIVE,
    MANUFACTURING,
    ENTERTAINMENT,
    MARKETING,
    CONSULTING,
    ENERGY,
    AGRICULTURE,
    LOGISTICS,
    SPORTS,
    TRAVEL,
    OTHER
}

enum class CompanySize {
    STARTUP,      // 1-10 employees
    SMALL,        // 11-50 employees
    MEDIUM,       // 51-200 employees
    LARGE,        // 201-1000 employees
    ENTERPRISE    // 1000+ employees
}
