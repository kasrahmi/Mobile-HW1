// repository/GitHubRepository.kt
package repository

import utils.FileStorageManager
import api.GitHubApiService
import model.GitHubUser
import utils.Result
import retrofit2.HttpException
import java.io.IOException

class GitHubRepository(
    private val apiService: GitHubApiService,
    private val fileStorage: FileStorageManager
) {
    private val userCache = mutableMapOf<String, GitHubUser>()

    init {
        // Load cache from file on initialization
        userCache.putAll(fileStorage.loadFromFile())
    }

    suspend fun getUser(username: String): Result<GitHubUser> {
        return try {
            userCache[username]?.let { return Result.Success(it) }

            val user = apiService.getUser(username)
            val repos = apiService.getUserRepos(username)
            val userWithRepos = user.copy(repos = repos)

            // Update both memory and file cache
            userCache[username] = userWithRepos
            fileStorage.saveToFile(userCache)

            Result.Success(userWithRepos)
        } catch (e: HttpException) {
            Result.Error("User not found: ${e.message}")
        } catch (e: IOException) {
            Result.Error("Network error: ${e.message}")
        }
    }

    fun getCachedUsers() = userCache.values.toList()
    fun searchUser(username: String) = userCache[username]
    fun searchRepositories(query: String) =
        userCache.values.flatMap { user ->
            user.repos.filter { repo ->
                repo.name.contains(query, ignoreCase = true)
            }.map { repo -> user to repo }
        }
}