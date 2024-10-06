package com.zavediaev.obrc.attempt4

import kotlin.math.round

const val initialMapCapacity = 1000

data class StorageWithStringStatsMap(
    val statsByCity: HashMap<String, IntStatsForCity> = HashMap(initialMapCapacity)
) {
    fun add(city: String, temperature: Int) {
        val currentStats = statsByCity[city]
        if (currentStats != null) {
            currentStats.add(temperature)
        } else {
            statsByCity[city] = IntStatsForCity(temperature, temperature, 1, temperature.toLong())
        }
    }

    fun printResults() {
        val cities = statsByCity.keys.sorted()

        cities.forEach { city ->
            val stats = statsByCity.getValue(city)
            val min = stats.min / 10f
            val avg = (round(stats.sum / stats.count.toDouble()) / 10).toFloat()
            val max = stats.max / 10f
            println("$city;$min;$avg;$max")
        }
    }

    operator fun plus(other: StorageWithStringStatsMap): StorageWithStringStatsMap {
        val cities = statsByCity.keys + other.statsByCity.keys

        val result = HashMap<String, IntStatsForCity>(initialMapCapacity)
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
        return StorageWithStringStatsMap(result)
    }
}