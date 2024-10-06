package com.zavediaev

import kotlin.math.round

data class StorageWithByteArrayHolderStatsMap(
    val statsByCity: HashMap<ByteArrayHolder, IntStatsForCity> = HashMap(initialMapCapacity)
) {
    fun add(city: ByteArray, temperature: Int) {
        val baHolder = ByteArrayHolder(city)
        val currentStats = statsByCity[baHolder]
        if (currentStats != null) {
            currentStats.add(temperature)
        } else {
            statsByCity[baHolder] = IntStatsForCity(temperature, temperature, 1, temperature.toLong())
        }
    }

    fun printResults() {
        statsByCity.map { entry ->
            val cityString = String(entry.key.byteArray)
            cityString to entry.value
        }.sortedBy { it.first }.forEach { (city, stats) ->
            val min = stats.min / 10f
            val avg = (round(stats.sum / stats.count.toDouble()) / 10).toFloat()
            val max = stats.max / 10f
            println("$city;$min;$avg;$max")
        }
    }

    operator fun plus(other: StorageWithByteArrayHolderStatsMap): StorageWithByteArrayHolderStatsMap {
        val cities = statsByCity.keys + other.statsByCity.keys

        val result = HashMap<ByteArrayHolder, IntStatsForCity>(initialMapCapacity)
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
        return StorageWithByteArrayHolderStatsMap(result)
    }
}