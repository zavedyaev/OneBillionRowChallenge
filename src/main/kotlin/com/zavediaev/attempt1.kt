package com.zavediaev

import java.io.File

/**
 * compiled to jar using ./gradlew shadowJar
 * to start:
 * time java -cp OneBillionRowChallengeKotlin-1.0-SNAPSHOT-all.jar com.zavediaev.MainKt
 *
 * took: 3:50.75
 */
fun attempt1(path: String) {
    val storage = StorageWithFloat()

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