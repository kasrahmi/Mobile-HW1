// Main.kt

import di.ServiceLocator
import repository.GitHubRepository
import utils.Result
import kotlinx.coroutines.runBlocking
import java.util.*

fun main() = runBlocking {
    val repository = ServiceLocator.repository
    val scanner = Scanner(System.`in`)

    while (true) {
        printMenu()
        when (scanner.nextLine()) {
            "1" -> fetchUser(repository, scanner)
            "2" -> listUsers(repository)
            "3" -> searchUser(repository, scanner)
            "4" -> searchRepositories(repository, scanner)
            "5" -> {
                println("Exiting program...")
                break
            }
            else -> println("Invalid option!")
        }
    }
    scanner.close() // Close the scanner to release resources
}

private suspend fun fetchUser(repository: GitHubRepository, scanner: Scanner) {
    print("Enter username: ")
    val username = scanner.nextLine()
    when (val result = repository.getUser(username)) {
        is Result.Success -> {
            val user = result.data
            println("""
                User Details:
                Username: ${user.login}
                Followers: ${user.followers}
                Following: ${user.following}
                Created At: ${user.createdAt}
                Repositories (${user.repos.size}):
                ${user.repos.joinToString("\n") { " - ${it.name}: ${it.description ?: "No description"}" }}
            """.trimIndent())
        }
        is Result.Error -> println("Error: ${result.message}")
    }
}

private fun listUsers(repository: GitHubRepository) {
    val users = repository.getCachedUsers()
    if (users.isEmpty()) {
        println("No users in cache!")
        return
    }
    println("Cached Users:")
    users.forEach { println("- ${it.login} (${it.repos.size} repos)") }
}

private fun searchUser(repository: GitHubRepository, scanner: Scanner) {
    print("Search username: ")
    val username = scanner.nextLine()
    val result = repository.searchUser(username)?.let {
        println("Found user: ${it.login}")
    } ?: println("User not found in cache")
}

private fun searchRepositories(repository: GitHubRepository, scanner: Scanner) {
    print("Search repository: ")
    val query = scanner.nextLine()
    val results = repository.searchRepositories(query)
    if (results.isEmpty()) {
        println("No repositories found")
        return
    }
    println("Found repositories:")
    results.forEach { (user, repo) ->
        println("""
            - ${repo.name} 
              Owner: ${user.login}
              Description: ${repo.description ?: "None"}
              URL: ${repo.htmlUrl}
        """.trimIndent())
    }
}

private fun printMenu() {
    println("""
        1. Fetch GitHub User
        2. List Cached Users
        3. Search User in Cache
        4. Search Repositories
        5. Exit
        Enter choice: 
    """.trimIndent())
}