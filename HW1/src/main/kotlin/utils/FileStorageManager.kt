// utils/FileStorageManager.kt
package utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import model.GitHubUser
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class FileStorageManager(private val fileName: String = "src/main/resources/github_cache.json") {
    private val gson = Gson()

    fun saveToFile(data: Map<String, GitHubUser>) {
        try {
            val jsonString = gson.toJson(data)
            File(fileName).writeText(jsonString)
        } catch (e: IOException) {
            println("Error saving cache to file: ${e.message}")
        }
    }

    fun loadFromFile(): Map<String, GitHubUser> {
        return try {
            val jsonString = File(fileName).readText()
            val type = object : TypeToken<Map<String, GitHubUser>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyMap()
        } catch (e: FileNotFoundException) {
            emptyMap()
        } catch (e: IOException) {
            println("Error loading cache from file: ${e.message}")
            emptyMap()
        } catch (e: JsonSyntaxException) {
            println("Error parsing cache file: ${e.message}")
            emptyMap()
        }
    }
}