package com.zavediaev.obrc.attempt2

fun processLines(lines: Iterator<String>, storage: StorageWithStringIntMaps): StorageWithStringIntMaps {
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
    return storage
}