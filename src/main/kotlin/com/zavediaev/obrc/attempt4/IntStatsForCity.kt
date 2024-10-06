package com.zavediaev.obrc.attempt4

import kotlin.math.max
import kotlin.math.min

data class IntStatsForCity(
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

    operator fun plus(other: IntStatsForCity): IntStatsForCity {
        return IntStatsForCity(
            max = max(max, other.max),
            min = min(min, other.min),
            count = count + other.count,
            sum = sum + other.sum,
        )
    }
}