# API Documentation

## Firebase Firestore Database Structure

### Collections

#### 1. Companies Collection (`companies`)

**Document Structure:**
```json
{
  "id": "string (auto-generated)",
  "name": "string",
  "tagline": "string",
  "description": "string",
  "logo": "string (URL)",
  "coverImage": "string (URL)",
  "industry": "enum (IndustryCategory)",
  "companySize": "enum (CompanySize)",
  "foundedYear": "number",
  "website": "string (URL)",
  "email": "string",
  "phone": "string",
  "address": {
    "street": "string",
    "city": "string",
    "state": "string",
    "country": "string",
    "zipCode": "string",
    "latitude": "number (optional)",
    "longitude": "number (optional)"
  },
  "socialMedia": {
    "linkedin": "string (URL)",
    "twitter": "string (URL)",
    "facebook": "string (URL)",
    "instagram": "string (URL)",
    "youtube": "string (URL)"
  },
  "products": [
    {
      "id": "string",
      "name": "string",
      "description": "string",
      "images": ["string (URLs)"],
      "price": "number (optional)",
      "currency": "string",
      "category": "string",
      "isAvailable": "boolean"
    }
  ],
  "services": ["string"],
  "galleryImages": ["string (URLs)"],
  "videos": ["string (URLs)"],
  "isFeatured": "boolean",
  "isVerified": "boolean",
  "rating": "number",
  "reviewCount": "number",
  "followers": "number",
  "views": "number",
  "createdAt": "timestamp",
  "updatedAt": "timestamp",
  "ownerId": "string (user ID)"
}
```

**Industry Categories (Enum):**
- TECHNOLOGY
- FINTECH
- HEALTHCARE
- EDUCATION
- ECOMMERCE
- FOOD_BEVERAGE
- FASHION
- REAL_ESTATE
- AUTOMOTIVE
- MANUFACTURING
- ENTERTAINMENT
- MARKETING
- CONSULTING
- ENERGY
- AGRICULTURE
- LOGISTICS
- SPORTS
- TRAVEL
- OTHER

**Company Sizes (Enum):**
- STARTUP (1-10 employees)
- SMALL (11-50 employees)
- MEDIUM (51-200 employees)
- LARGE (201-1000 employees)
- ENTERPRISE (1000+ employees)

---

#### 2. Reels Collection (`reels`)

**Document Structure:**
```json
{
  "id": "string (auto-generated)",
  "companyId": "string",
  "companyName": "string",
  "companyLogo": "string (URL)",
  "title": "string",
  "description": "string",
  "videoUrl": "string (Firebase Storage URL)",
  "thumbnailUrl": "string (URL)",
  "duration": "number (seconds)",
  "category": "enum (ReelCategory)",
  "tags": ["string"],
  "likes": "number",
  "views": "number",
  "shares": "number",
  "comments": "number",
  "createdAt": "timestamp",
  "isActive": "boolean"
}
```

**Reel Categories (Enum):**
- PRODUCT_LAUNCH
- INNOVATION
- STARTUP_STORY
- BEHIND_THE_SCENES
- TUTORIAL
- CASE_STUDY
- COMPANY_CULTURE
- ANNOUNCEMENT
- TESTIMONIAL
- EVENT

---

#### 3. Users Collection (`users`)

**Document Structure:**
```json
{
  "id": "string (Firebase Auth UID)",
  "email": "string",
  "displayName": "string",
  "photoUrl": "string (URL)",
  "userType": "enum (UserType)",
  "companyId": "string (optional)",
  "savedCompanies": ["string (company IDs)"],
  "likedReels": ["string (reel IDs)"],
  "followedCompanies": ["string (company IDs)"],
  "createdAt": "timestamp",
  "lastLogin": "timestamp",
  "fcmToken": "string"
}
```

**User Types (Enum):**
- VISITOR (regular user)
- COMPANY_OWNER
- ADMIN

---

#### 4. Analytics Collection (`analytics`)

**Document Structure (per company):**
```json
{
  "companyId": "string",
  "profileViews": "number",
  "profileViewsGrowth": "number (percentage)",
  "totalFollowers": "number",
  "followersGrowth": "number (percentage)",
  "reelViews": "number",
  "reelViewsGrowth": "number (percentage)",
  "totalLikes": "number",
  "totalShares": "number",
  "engagementRate": "number (percentage)",
  "topPerformingReels": [
    {
      "reelId": "string",
      "title": "string",
      "thumbnailUrl": "string",
      "views": "number",
      "likes": "number",
      "shares": "number",
      "engagementRate": "number"
    }
  ],
  "viewsByDate": {
    "YYYY-MM-DD": "number"
  },
  "demographicData": {
    "ageGroups": {
      "18-24": "number",
      "25-34": "number",
      "35-44": "number",
      "45-54": "number",
      "55+": "number"
    },
    "topCountries": {
      "country_name": "number"
    },
    "topCities": {
      "city_name": "number"
    }
  }
}
```

---

## Repository Methods

### CompanyRepository

#### getCompanies()
```kotlin
suspend fun getCompanies(
    category: IndustryCategory? = null,
    isFeatured: Boolean? = null,
    limit: Int = 20
): Flow<Resource<List<Company>>>
```
**Description:** Retrieves a list of companies with optional filtering.

#### getCompanyById()
```kotlin
suspend fun getCompanyById(id: String): Flow<Resource<Company>>
```
**Description:** Fetches a single company by ID.

#### searchCompanies()
```kotlin
suspend fun searchCompanies(query: String): Flow<Resource<List<Company>>>
```
**Description:** Searches companies by name.

#### createCompany()
```kotlin
suspend fun createCompany(company: Company): Flow<Resource<String>>
```
**Description:** Creates a new company profile.

#### updateCompany()
```kotlin
suspend fun updateCompany(company: Company): Flow<Resource<Unit>>
```
**Description:** Updates an existing company.

#### followCompany()
```kotlin
suspend fun followCompany(companyId: String): Flow<Resource<Unit>>
```
**Description:** Follows a company (increments follower count).

#### unfollowCompany()
```kotlin
suspend fun unfollowCompany(companyId: String): Flow<Resource<Unit>>
```
**Description:** Unfollows a company.

---

### ReelRepository

#### getReels()
```kotlin
suspend fun getReels(
    category: ReelCategory? = null,
    limit: Int = 20
): Flow<Resource<List<Reel>>>
```
**Description:** Retrieves reels with optional category filtering.

#### getReelsByCompany()
```kotlin
suspend fun getReelsByCompany(companyId: String): Flow<Resource<List<Reel>>>
```
**Description:** Fetches all reels for a specific company.

#### uploadReel()
```kotlin
suspend fun uploadReel(reel: Reel, videoUri: String): Flow<Resource<String>>
```
**Description:** Uploads a video to Firebase Storage and creates a reel document.

#### likeReel()
```kotlin
suspend fun likeReel(reelId: String): Flow<Resource<Unit>>
```
**Description:** Likes a reel.

#### incrementViews()
```kotlin
suspend fun incrementViews(reelId: String): Flow<Resource<Unit>>
```
**Description:** Increments view count.

---

## Firebase Storage Structure

```
/reels/
  /{reel-id}.mp4

/images/
  /companies/
    /{company-id}/
      /logo.jpg
      /cover.jpg
      /gallery/
        /image1.jpg
        /image2.jpg
  /products/
    /{product-id}/
      /image1.jpg

/thumbnails/
  /{reel-id}.jpg
```

---

## Error Handling

All repository methods return `Resource<T>` which can be:
- `Resource.Loading`: Operation in progress
- `Resource.Success<T>`: Operation completed successfully with data
- `Resource.Error`: Operation failed with error message

Example usage:
```kotlin
viewModelScope.launch {
    repository.getCompanies().collect { result ->
        when (result) {
            is Resource.Loading -> showLoading()
            is Resource.Success -> updateUI(result.data)
            is Resource.Error -> showError(result.message)
        }
    }
}
```

---

## Security Considerations

1. All write operations require authentication
2. Users can only modify their own data
3. Company owners can only edit their own companies
4. File uploads are size-limited (videos: 50MB, images: 5MB)
5. All URLs use HTTPS
6. Sensitive data is never exposed in client-side code
