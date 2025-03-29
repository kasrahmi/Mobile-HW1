// di/ServiceLocator.kt
package di

import api.GitHubApiService
import repository.GitHubRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utils.FileStorageManager

object ServiceLocator {
    private const val BASE_URL = "https://api.github.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: GitHubApiService by lazy {
        retrofit.create(GitHubApiService::class.java)
    }


    private val fileStorage: FileStorageManager by lazy {
        FileStorageManager()
    }

    val repository: GitHubRepository by lazy {
        GitHubRepository(apiService, fileStorage)
    }
}