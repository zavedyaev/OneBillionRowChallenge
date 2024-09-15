package com.zavediaev

fun main(args: Array<String>) {
    val path = args.firstOrNull() ?: "/Users/zavediaev-work/workspace/1brc/measurements.txt"

    // 3:50.75
    attempt1(path)
}