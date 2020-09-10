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

    class IDSet<ID : Comparable<ID>, E : Identifiable<ID>> : Set<E> {
        private val map: Map<ID, E> = mapOf()

        override fun contains(element: E) = map.containsKey(element.id)
        override fun containsAll(elements: Collection<E>) = map.keys.containsAll(elements.map { it.id })
        override fun isEmpty() = size == 0
        override fun iterator() = map.values.iterator()
        override val size get() = map.size
    }

    class MutableIDSet<ID : Comparable<ID>, E : Identifiable<ID>> : MutableSet<E> {
        private val map: MutableMap<ID, E> = mutableMapOf()

        override fun contains(element: E) = map.containsKey(element.id)
        override fun containsAll(elements: Collection<E>) = map.keys.containsAll(elements.map { it.id })
        override fun isEmpty() = size == 0
        override fun iterator() = map.values.iterator()
        override val size get() = map.size
        override fun add(element: E) = map[element.id]?.let { false } ?: run {
            map[element.id] = element
            true
        }

        override fun addAll(elements: Collection<E>) = false !in elements.map {
            add(it)
        }

        override fun clear() = map.clear()

        override fun remove(element: E) = map.remove(element.id)?.let { true } ?: false

        override fun removeAll(elements: Collection<E>) = false !in elements.map {
            remove(it)
        }

        override fun retainAll(elements: Collection<E>) = map.clear().let {
            addAll(elements)
        }
    }
}


