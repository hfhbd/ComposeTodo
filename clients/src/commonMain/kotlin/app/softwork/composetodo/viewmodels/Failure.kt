package app.softwork.composetodo.viewmodels

sealed class Failure {
    abstract val reason: String

    object WrongCredentials : Failure() {
        override val reason: String get() = "Wrong credentials"
    }

    object NoNetwork : Failure() {
        override val reason: String get() = "Server not available"
    }
}
