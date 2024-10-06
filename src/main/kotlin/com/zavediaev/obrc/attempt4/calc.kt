package com.zavediaev.obrc.attempt4

import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.math.min


/**
 * Multi-thread implementation.
 * Optimizations against [com.zavediaev.obrc.attempt3.calc]:
 * 1. parallel file read
 * 2. optimized for M3 Pro processor (18 threads for 6 performance cores, 6 threads for 6 efficiency cores)
 * 3. reading file using byte arrays and converting to String only actual city names (not the whole line)
 * 4. storage is updated to have only 1 HashMap per city (not 4) so saving time on operations with maps
 * 5. set initial map's capacity to avoid re-builds of maps
 *
 * Takes 5.251 seconds (best result)
 */
fun calc(path: String) {
    val file = File(path)
    val fileLength = file.length()

    val threads = 24
    val bufferSize = 1024 * 256

    val pool = Executors.newFixedThreadPool(threads)

    val bytesPerThread = fileLength / threads
    val bufferReadsPerThread = bytesPerThread / bufferSize
    val adjustedBytesPerThread = bufferReadsPerThread * bufferSize

    val futures = (0 until threads).map { thread ->
        val callable = Callable {
            val toSkipBytes = adjustedBytesPerThread * thread
            var skippedFirstPartBa = ByteArray(0)
            val threadStorage = StorageWithStringStatsMap()

            val lineBufferSize = 512
            val lineBuffer = ByteArray(lineBufferSize)
            var lineBufferIndex = 0

            FileChannel.open(Paths.get(path), StandardOpenOption.READ).use { fileChannel ->
                fileChannel.position(toSkipBytes)
                val buffer: ByteBuffer = ByteBuffer.allocate(bufferSize)

                var firstChunk = true

                val chunkBuffer = ByteArray(bufferSize)
                val processChunk = { bytesRead: Int ->
                    for (byteIndexInChunk in 0 until bytesRead) {
                        val byteFromChunk = chunkBuffer[byteIndexInChunk]
                        if (byteFromChunk != nextLineByte) {
                            lineBuffer[lineBufferIndex] = byteFromChunk
                            lineBufferIndex++
                        } else {
                            if (firstChunk) {
                                firstChunk = false
                                skippedFirstPartBa = ByteArray(lineBufferIndex)
                                lineBuffer.copyInto(skippedFirstPartBa, 0, 0, lineBufferIndex)
                            } else {
                                var semicolonIndex = -1
                                for (i in 0 until lineBufferIndex) {
                                    val byte = lineBuffer[i]
                                    if (byte == semicolonByte) {
                                        semicolonIndex = i
                                        break
                                    }
                                }
                                val name = String(lineBuffer, 0, semicolonIndex)

                                var temp = 0
                                var tempMultiplier = 1
                                for (i in (semicolonIndex + 1) until lineBufferIndex) {
                                    val byte = lineBuffer[i]
                                    if (byte != commaByte) {
                                        if (byte == minusByte) {
                                            tempMultiplier = -1
                                        } else {
                                            temp = (temp * 10) + (lineBuffer[i].toInt() - 48)
                                        }
                                    }
                                }
                                temp *= tempMultiplier

                                threadStorage.add(name, temp)
                            }
                            lineBufferIndex = 0
                        }

                    }
                }

                if (thread == threads - 1) {
                    while (fileChannel.read(buffer) > 0) {
                        buffer.flip()

                        val bytesToRead = min(buffer.remaining(), bufferSize)
                        buffer.get(chunkBuffer, 0, bytesToRead)
                        processChunk(bytesToRead)
                        buffer.clear() // Ready for the next read
                    }
                } else {
                    for (i in 1..bufferReadsPerThread) {
                        fileChannel.read(buffer)
                        buffer.flip()

                        val bytesRead = buffer.remaining()
                        buffer.get(chunkBuffer)
                        processChunk(bytesRead)
                        buffer.clear() // Ready for the next read
                    }
                }

            }


            val skippedLastPartBa = if (lineBufferIndex != 0) {
                val skippedLastPartBa = ByteArray(lineBufferIndex)
                lineBuffer.copyInto(skippedLastPartBa, 0, 0, lineBufferIndex)
                skippedLastPartBa
            } else {
                ByteArray(0)
            }

            ThreadResult(
                skippedBeginning = skippedFirstPartBa,
                skippedEnd = skippedLastPartBa,
                storage = threadStorage
            )
        }
        pool.submit(callable)
    }


    val threadResults = futures.map { it.get() }
    pool.shutdown()

    val storageSum = threadResults.map { it.storage }.reduce { acc, storage -> acc + storage }
    val unprocessedStr = threadResults.map { it.skippedBeginning + nextLineByte + it.skippedEnd }
        .reduce { acc, bytes -> acc + bytes }.toString(Charsets.UTF_8)
    val storageForUnprocessed = StorageWithStringStatsMap()

    val lines = unprocessedStr.split("\n")
    lines.filter { it.isNotBlank() }.forEach { line ->
        val split = line.split(";")
        val name = split.first()
        val temperature = split.last().replace(".", "").toInt()

        storageForUnprocessed.add(name, temperature)
    }

    val result = storageSum + storageForUnprocessed
    result.printResults()

}
