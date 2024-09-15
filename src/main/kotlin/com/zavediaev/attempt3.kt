package com.zavediaev

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.concurrent.Executors

/**
 * compiled to jar using ./gradlew shadowJar
 * to start:
 * time java -cp OneBillionRowChallengeKotlin-1.0-SNAPSHOT-all.jar com.zavediaev.MainKt
 *
 * took: 42.968
 */
fun attempt3(path: String) {
    val threadPool = Executors.newFixedThreadPool(12)
    runBlocking(threadPool.asCoroutineDispatcher()) {
        val linesChannel = Channel<List<String>>(1000)

        val file = File(path)

        val read = async {
            file.useLines { lines ->
                lines.chunked(1000).forEach { chunk ->
                    linesChannel.send(chunk)
                }
                linesChannel.close()
            }
        }
        read.start()

        val processCoroutinesNumber = 12
        val deferred = (0 until processCoroutinesNumber).map { coroutineIndex ->
            val coroutine = async {
                var lines = linesChannel.receiveCatching().getOrNull()
                val storage = StorageWithInt()
                while (lines != null) {
                    processLines(lines, storage)
                    lines = linesChannel.receiveCatching().getOrNull()
                }
                storage
            }
            coroutine.start()
            coroutine
        }

        val storage = deferred.map { it.await() }.reduce { acc, storageWithInt -> acc + storageWithInt }
        storage.printResults()
    }
    threadPool.shutdown()
}

private fun processLines(lines: List<String>, storage: StorageWithInt): StorageWithInt {
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