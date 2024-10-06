package com.zavediaev.obrc.attempt1

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

data class StorageWithStringFloatMaps(
    val maxByCity: HashMap<String, Float> = HashMap(),
    val minByCity: HashMap<String, Float> = HashMap(),
    val countByCity: HashMap<String, Int> = HashMap(),
    val sumByCity: HashMap<String, Double> = HashMap(),
) {
    fun add(city: String, temperature: Float) {
        val count = countByCity.getOrElse(city) { 0 }
        val currentMax = maxByCity.getOrElse(city) { Float.MIN_VALUE }
        val currentMin = minByCity.getOrElse(city) { Float.MAX_VALUE }
        val currentSum = sumByCity.getOrElse(city) { 0.0 }

        countByCity[city] = count + 1
        maxByCity[city] = max(currentMax, temperature)
        minByCity[city] = min(currentMin, temperature)
        sumByCity[city] = currentSum + temperature
    }

    fun printResults() {
        val cities = maxByCity.keys.sorted()

        cities.forEach { city ->
            val min = minByCity.getValue(city)
            val avg = (round((sumByCity.getValue(city) / countByCity.getValue(city)) * 10) / 10).toFloat()
            val max = maxByCity.getValue(city)
            println("$city;$min;$avg;$max")
        }
    }

    operator fun plus(other: StorageWithStringFloatMaps): StorageWithStringFloatMaps {
        val cities = maxByCity.keys + other.maxByCity.keys

        val newMaxByCity: HashMap<String, Float> = HashMap()
        val newMinByCity: HashMap<String, Float> = HashMap()
        val newCountByCity: HashMap<String, Int> = HashMap()
        val newSumByCity: HashMap<String, Double> = HashMap()

        cities.forEach { city ->
            newMaxByCity[city] = max(maxByCity[city] ?: Float.MIN_VALUE, other.maxByCity[city] ?: Float.MIN_VALUE)
            newMinByCity[city] = min(minByCity[city] ?: Float.MAX_VALUE, other.minByCity[city] ?: Float.MAX_VALUE)
            newCountByCity[city] = (countByCity[city] ?: 0) + (other.countByCity[city] ?: 0)
            newSumByCity[city] = (sumByCity[city] ?: 0.0) + (other.sumByCity[city] ?: 0.0)
        }
        return StorageWithStringFloatMaps(
            newMaxByCity,
            newMinByCity,
            newCountByCity,
            newSumByCity,
        )
    }
}