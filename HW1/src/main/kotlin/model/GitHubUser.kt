// model/GitHubUser.kt
package model

import com.google.gson.annotations.SerializedName

data class GitHubUser(
    val login: String,
    val followers: Int,
    val following: Int,
    @SerializedName("created_at") val createdAt: String,
    val repos: List<GitHubRepo> = emptyList()
)