package app.softwork.composetodo.viewmodels

enum class Failure(val reason: String) {
    WrongCredentials("Wrong credentials"),
    NoNetwork("Server not available")
}
