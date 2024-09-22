package com.zavediaev

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

data class StatsForCity(
    var max: Int = Int.MIN_VALUE,
    var min: Int = Int.MAX_VALUE,
    var count: Int = 0,
    var sum: Long = 0
) {
    fun add(temperature: Int) {
        max = max(max, temperature)
        min = min(min, temperature)
        count += 1
        sum += temperature
    }

    operator fun plus(other: StatsForCity): StatsForCity {
        return StatsForCity(
            max = max(max, other.max),
            min = min(min, other.min),
            count = count + other.count,
            sum = sum + other.sum,
        )
    }
}

const val initialMapCapacity = 1000

data class StorageWithObj(
    val statsByCity: HashMap<String, StatsForCity> = HashMap(initialMapCapacity)
) {
    fun add(city: String, temperature: Int) {
        val currentStats = statsByCity[city]
        if (currentStats != null) {
            currentStats.add(temperature)
        } else {
            statsByCity[city] = StatsForCity(temperature, temperature, 1, temperature.toLong())
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

    operator fun plus(other: StorageWithObj): StorageWithObj {
        val cities = statsByCity.keys + other.statsByCity.keys

        val result = HashMap<String, StatsForCity>(initialMapCapacity)
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
        return StorageWithObj(result)
    }
}