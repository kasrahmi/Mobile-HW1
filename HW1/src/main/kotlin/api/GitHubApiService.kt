// api/GitHubApiService.kt
package api

import model.GitHubRepo
import model.GitHubUser
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GitHubApiService {
    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String,
        @Header("User-Agent") userAgent: String = "GitHubInfoApp"
    ): GitHubUser

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String,
        @Header("User-Agent") userAgent: String = "GitHubInfoApp"
    ): List<GitHubRepo>
}