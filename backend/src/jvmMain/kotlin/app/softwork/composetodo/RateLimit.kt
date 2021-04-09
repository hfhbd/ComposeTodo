package app.softwork.composetodo

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.*
import kotlin.collections.set
import kotlin.time.*

@ExperimentalTime
interface Storage {
    suspend fun getOrNull(host: String): Requested?
    suspend fun set(host: String, requested: Requested)
    suspend fun remove(host: String)

    data class Requested(val trial: Int, val beginLock: TimeMark?)
}

@ExperimentalTime
class InMemory : Storage {
    private val storage = mutableMapOf<String, Storage.Requested>()

    override suspend fun getOrNull(host: String): Storage.Requested? = storage[host]
    override suspend fun set(host: String, requested: Storage.Requested) {
        storage[host] = requested
    }

    override suspend fun remove(host: String) {
        storage.remove(host)
    }
}

@ExperimentalTime
class RateLimit(val configuration: Configuration) {
    suspend fun isAllowed(host: String): Boolean {
        if (configuration.alwaysAllow(host)) {
            return true
        }
        if (configuration.alwaysBlock(host)) {
            return false
        }

        val storage = configuration.storage
        val previous = storage.getOrNull(host)
        if (previous == null) {
            storage.set(host, Storage.Requested(trial = 1, beginLock = null))
            return true
        }
        if (previous.trial < configuration.limit) {
            storage.set(host, previous.copy(trial = previous.trial + 1))
            return true
        }
        val lock = previous.beginLock
        if (lock == null) {
            storage.set(host, previous.copy(beginLock = TimeSource.Monotonic.markNow()))
            return false
        }
        if (lock.elapsedNow() > configuration.coolDown) {
            storage.remove(host)
            return true
        }
        return false
    }

    class Configuration(
        var host: (ApplicationCall) -> String = { it.request.local.remoteHost },
        var alwaysAllow: (String) -> Boolean = { false },
        var alwaysBlock: (String) -> Boolean = { false },
        var storage: Storage = InMemory(),
        var limit: Int = 1000,
        var coolDown: Duration = 1.hours,
    )

    companion object Feature : ApplicationFeature<Application, Configuration, RateLimit> {
        override val key = AttributeKey<RateLimit>("RateLimit")

        override fun install(
            pipeline: Application,
            configure: Configuration.() -> Unit
        ): RateLimit {
            val feature = RateLimit(Configuration().apply(configure))

            pipeline.intercept(ApplicationCallPipeline.Features) {
                val host = feature.configuration.host(call)
                if (feature.isAllowed(host)) {
                    proceed()
                } else {
                    finish()
                    call.respond(HttpStatusCode.TooManyRequests)
                }
            }
            return feature
        }
    }
}
