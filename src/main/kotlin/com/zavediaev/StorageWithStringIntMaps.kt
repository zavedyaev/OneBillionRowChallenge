package com.zavediaev

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

data class StorageWithStringIntMaps(
    val maxByCity: HashMap<String, Int> = HashMap(),
    val minByCity: HashMap<String, Int> = HashMap(),
    val countByCity: HashMap<String, Int> = HashMap(),
    val sumByCity: HashMap<String, Long> = HashMap(),
) {
    fun add(city: String, temperature: Int) {
        val count = countByCity.getOrElse(city) { 0 }
        val currentMax = maxByCity.getOrElse(city) { Int.MIN_VALUE }
        val currentMin = minByCity.getOrElse(city) { Int.MAX_VALUE }
        val currentSum = sumByCity.getOrElse(city) { 0 }

        countByCity[city] = count + 1
        maxByCity[city] = max(currentMax, temperature)
        minByCity[city] = min(currentMin, temperature)
        sumByCity[city] = currentSum + temperature
    }

    fun printResults() {
        val cities = maxByCity.keys.sorted()

        cities.forEach { city ->
            val min = minByCity.getValue(city) / 10f
            val avg = (round(sumByCity.getValue(city) / countByCity.getValue(city).toDouble()) / 10).toFloat()
            val max = maxByCity.getValue(city) / 10f
            println("$city;$min;$avg;$max")
        }
    }

    operator fun plus(other: StorageWithStringIntMaps): StorageWithStringIntMaps {
        val cities = maxByCity.keys + other.maxByCity.keys

        val newMaxByCity: HashMap<String, Int> = HashMap()
        val newMinByCity: HashMap<String, Int> = HashMap()
        val newCountByCity: HashMap<String, Int> = HashMap()
        val newSumByCity: HashMap<String, Long> = HashMap()

        cities.forEach { city ->
            newMaxByCity[city] = max(maxByCity[city] ?: Int.MIN_VALUE, other.maxByCity[city] ?: Int.MIN_VALUE)
            newMinByCity[city] = min(minByCity[city] ?: Int.MAX_VALUE, other.minByCity[city] ?: Int.MAX_VALUE)
            newCountByCity[city] = (countByCity[city] ?: 0) + (other.countByCity[city] ?: 0)
            newSumByCity[city] = (sumByCity[city] ?: 0) + (other.sumByCity[city] ?: 0)
        }
        return StorageWithStringIntMaps(
            newMaxByCity,
            newMinByCity,
            newCountByCity,
            newSumByCity,
        )
    }
}