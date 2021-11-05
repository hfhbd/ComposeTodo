package app.softwork.composetodo

import io.ktor.client.features.cookies.*
import io.ktor.http.*
import platform.Foundation.*

class UserDefaultsCookieStorage : CookiesStorage {
    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        NSUserDefaults.standardUserDefaults.setValue(cookie.value, forKey = "refreshToken")
    }

    override fun close() {}

    override suspend fun get(requestUrl: Url): List<Cookie> =
        NSUserDefaults.standardUserDefaults.stringForKey("refreshToken")?.let {
            listOf(Cookie(name = "SESSION", value = it, secure = true, httpOnly = true))
        } ?: emptyList()
}
