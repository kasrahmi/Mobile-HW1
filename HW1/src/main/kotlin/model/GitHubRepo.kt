// model/GitHubRepo.kt
package model

import com.google.gson.annotations.SerializedName

data class GitHubRepo(
    val name: String,
    val description: String?,
    @SerializedName("html_url") val htmlUrl: String
)