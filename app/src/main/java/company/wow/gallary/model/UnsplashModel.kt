package company.wow.gallary.model



data class UnsplashModel(
        val id: String,
        val created_at: String,
        val updated_at: String,
        val width: Int,
        val height: Int,
        val color: String,
        val description: Any,
        val urls: Urls,
        val links: Links,
        val categories: List<Any>,
        val sponsored: Boolean,
        val likes: Int,
        val liked_by_user: Boolean,
        val current_user_collections: List<Any>,
        val slug: Any,
        val user: User
)