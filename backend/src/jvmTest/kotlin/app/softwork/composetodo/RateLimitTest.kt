package app.softwork.composetodo

import kotlinx.coroutines.*
import kotlin.test.*
import kotlin.time.*

@ExperimentalTime
class RateLimitTest {
    @Test
    fun testInMemory() = runBlocking {
        val checker = RateLimit(RateLimit.Configuration(limit = 3, coolDown = 3.seconds))
        assertTrue(checker.isAllowed("a"))
        assertTrue(checker.isAllowed("a"))
        assertTrue(checker.isAllowed("a"))

        assertFalse(checker.isAllowed("a"))
        assertFalse(checker.isAllowed("a"))
        assertFalse(checker.isAllowed("a"))
        delay(1.seconds)
        assertFalse(checker.isAllowed("a"))
        assertFalse(checker.isAllowed("a"))
        assertFalse(checker.isAllowed("a"))
        delay(3.seconds)
        assertTrue(checker.isAllowed("a"))

        assertTrue(checker.isAllowed("a"))
        assertTrue(checker.isAllowed("a"))
        assertTrue(checker.isAllowed("a"))

        assertFalse(checker.isAllowed("a"))
        assertFalse(checker.isAllowed("a"))
        assertFalse(checker.isAllowed("a"))
        delay(3.seconds)
        assertTrue(checker.isAllowed("a"))
        assertTrue(checker.isAllowed("a"))
        assertTrue(checker.isAllowed("a"))
    }
}
