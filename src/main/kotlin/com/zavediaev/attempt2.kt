package com.zavediaev

import java.io.File

/**
 * compiled to jar using ./gradlew shadowJar
 * to start:
 * time java -cp OneBillionRowChallengeKotlin-1.0-SNAPSHOT-all.jar com.zavediaev.MainKt
 *
 * took: 2:38.56
 */
fun attempt2(path: String) {
    val storage = StorageWithStringIntMaps()

    val file = File(path)
    file.useLines { lines ->
        lines.forEach { line ->
            val nameSb = StringBuilder()

            var makingName = true
            var temp = 0
            var tempMultiplier = 1

            for (char in line) {
                if (char == ';') {
                    makingName = false
                } else {
                    if (makingName) {
                        nameSb.append(char)
                    } else {
                        if (char == '-') {
                            tempMultiplier = -1
                        } else {
                            if (char != '.') {
                                temp = (temp * 10) + (char.code - 48)
                            }
                        }
                    }
                }
            }
            temp *= tempMultiplier

            storage.add(nameSb.toString(), temp)
        }
    }

    storage.printResults()
}