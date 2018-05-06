package company.wow.gallary.model

import java.io.Serializable


data class Links(
    val self: String,
    val html: String,
    val photos: String,
    val likes: String,
    val portfolio: String,
    val following: String,
    val followers: String
) : Serializable