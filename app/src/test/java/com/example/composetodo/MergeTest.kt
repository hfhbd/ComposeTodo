package com.example.composetodo

import java.time.OffsetDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.days

class MergeTest {
    data class T(override val id: Int, val title: String, override val modificationDate: OffsetDateTime) :
        AutoMergeable<Int>

    interface Identifiable<ID> {
        val id: ID
    }

    interface AutoMergeable<ID> : Identifiable<ID> {
        val modificationDate: OffsetDateTime
    }

    class Merge<ID, T : Identifiable<ID>>(
        /** create these, eg upload or in local database */
        val new: Set<T>,
        /** no action required */
        val noChange: Set<T>,
        /** save these changes */
        val change: Set<T>
    ) {
        companion object {
            fun <ID, T : AutoMergeable<ID>> merging(
                remote: Source.Remote<T>,
                local: Source.Local<T>,
                override: Boolean = true
            ): Result<ID, T> {
                val remoteOnly = remote.values - local.values
                val localOnly = local.values - remote.values
                val both = local.values intersect remote.values
                val noChanges = both
                val changes = both
                val remoteMerge = Merge(localOnly, noChanges, changes)
                val localMerge = Merge(remoteOnly, noChanges, changes)
                return Result(remoteMerge, localMerge)
            }
        }

        data class Result<ID, T : Identifiable<ID>>(
            val remote: Merge<ID, T>,
            val local: Merge<ID, T>
        )
    }

    sealed class Source<T> {
        abstract val values: Set<T>

        data class Remote<T>(override val values: Set<T>) : Source<T>()
        data class Local<T>(override val values: Set<T>) : Source<T>()
    }


    @Test
    fun testing() {
        val today = OffsetDateTime.now()
        val yesterday = today - 1.days

        val same = T(1, "Same", today)

        val remote = T(2, "Remote", today)
        val local = T(2, "Local", yesterday)

        val newRemote = T(3, "NewRemote", today)
        val newLocal = T(4, "NewLocal", today)

        val remotes = Source.Remote(setOf(same, remote, newRemote))
        val locals = Source.Local(setOf(same, local, newLocal))

        val result = Merge.merging(remote = remotes, local = locals)

        with(result.remote) {
            assertEquals(setOf(newLocal), new)
            assertEquals(setOf(same, remote, newRemote), noChange)
            assertEquals(setOf(), change)
        }

        with(result.local) {
            assertEquals(setOf(newRemote), new)
            assertEquals(setOf(same, newLocal), noChange)
            assertEquals(setOf(remote), change)
        }
    }

    private operator fun OffsetDateTime.minus(duration: Duration): OffsetDateTime =
        this.minusNanos(duration.toLongNanoseconds())
}


