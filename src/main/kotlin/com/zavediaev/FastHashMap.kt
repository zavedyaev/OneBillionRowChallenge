package com.zavediaev

class FastHashMapEntry<T : Any>(
    override val key: ByteArray,
    override var value: T
) : MutableMap.MutableEntry<ByteArray, T> {
    override fun setValue(newValue: T): T {
        val previousValues = this.value
        value = newValue
        return previousValues
    }
}

class FastHashMap<T : Any>(
    private val mapCapacity: Int,
) : MutableMap<ByteArray, T> {
    private val buckets = Array<MutableList<FastHashMapEntry<T>>>(mapCapacity) {
        mutableListOf()
    }

    private fun getBucketIndex(key: ByteArray): Int {
        // to get rid of negative values without IF
        return (key.contentHashCode() and Int.MAX_VALUE) % mapCapacity
    }


    override fun put(key: ByteArray, value: T): T? {
        val bucket = buckets[getBucketIndex(key)]
        for (fastHashMapEntry in bucket) {
            if (fastHashMapEntry.key.contentEquals(key)) {
                val previousValue = fastHashMapEntry.value
                fastHashMapEntry.value = value
                return previousValue
            }
        }
        bucket.add(FastHashMapEntry(key, value))
        return null
    }


    override fun get(key: ByteArray): T? {
        val bucket = buckets[getBucketIndex(key)]
        for (fastHashMapEntry in bucket) {
            if (fastHashMapEntry.key.contentEquals(key)) {
                return fastHashMapEntry.value
            }
        }
        return null
    }

    override val entries: MutableSet<MutableMap.MutableEntry<ByteArray, T>>
        get() {
            return buckets.flatMap { it }.toMutableSet()
        }

    override val keys: MutableSet<ByteArray>
        get() = buckets.flatMap { pairs -> pairs.map { it.key } }.toMutableSet()

    override val size: Int
        get() = buckets.sumOf { it.size }


    override val values: MutableCollection<T>
        get() = error("not implemented")

    override fun clear() {
        error("not implemented")
    }

    override fun isEmpty(): Boolean {
        error("not implemented")
    }

    override fun remove(key: ByteArray): T? {
        error("not implemented")
    }

    override fun putAll(from: Map<out ByteArray, T>) {
        error("not implemented")
    }

    override fun containsValue(value: T): Boolean {
        error("not implemented")
    }

    override fun containsKey(key: ByteArray): Boolean {
        error("not implemented")
    }
}