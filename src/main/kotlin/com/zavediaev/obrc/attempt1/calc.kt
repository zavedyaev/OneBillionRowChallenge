package com.zavediaev.obrc.attempt1

import java.io.File

/**
 * Straight-forward single-thread implementation with no optimizations.
 *
 * Takes ~230 seconds
 */
fun calc(path: String) {
    val storage = StorageWithStringFloatMaps()

    val file = File(path)
    file.useLines { lines ->
        lines.forEach { line ->
            val split = line.split(";")
            val name = split.first()
            val temperature = split.last().toFloat()

            storage.add(name, temperature)
        }
    }

    storage.printResults()
}