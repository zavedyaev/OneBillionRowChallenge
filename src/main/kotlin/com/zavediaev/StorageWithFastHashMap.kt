package com.zavediaev

import kotlin.math.round

private val fastHashMapInitialMapCapacity = 10000

data class StorageWithFastHashMap(
    val statsByCity: FastHashMap<IntStatsForCity> = FastHashMap(fastHashMapInitialMapCapacity)
) {
    fun add(city: ByteArray, temperature: Int) {
        val currentStats = statsByCity[city]
        if (currentStats != null) {
            currentStats.add(temperature)
        } else {
            statsByCity[city] = IntStatsForCity(temperature, temperature, 1, temperature.toLong())
        }
    }

    fun printResults() {
        statsByCity.map { entry ->
            val cityString = String(entry.key)
            cityString to entry.value
        }.sortedBy { it.first }.forEach { (city, stats) ->
            val min = stats.min / 10f
            val avg = (round(stats.sum / stats.count.toDouble()) / 10).toFloat()
            val max = stats.max / 10f
            println("$city;$min;$avg;$max")
        }
    }

    operator fun plus(other: StorageWithFastHashMap): StorageWithFastHashMap {
        val cities = statsByCity.keys + other.statsByCity.keys

        val result = FastHashMap<IntStatsForCity>(fastHashMapInitialMapCapacity)
        cities.forEach { city ->
            val stats = statsByCity[city]
            val otherStats = other.statsByCity[city]

            if (stats == null) {
                result[city] = otherStats!!
            } else if (otherStats == null) {
                result[city] = stats
            } else {
                result[city] = stats + otherStats
            }
        }
        return StorageWithFastHashMap(result)
    }
}