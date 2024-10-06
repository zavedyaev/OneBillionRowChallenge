package com.zavediaev.obrc.attempt2

import java.io.File

/**
 * Single-thread implementation.
 * Optimizations against [com.zavediaev.obrc.attempt1.calc]:
 * 1. Do not create instance of String for the part of line containing temperature (save time on instance creation / GC)
 * 2. use Integer instead of Float to store temperature (speeds up parsing and calculations)
 *
 * Takes ~158 seconds
 */
fun calc(path: String) {
    val storage = StorageWithStringIntMaps()

    val file = File(path)
    file.useLines { lines ->
        processLines(lines.iterator(), storage)
    }

    storage.printResults()
}