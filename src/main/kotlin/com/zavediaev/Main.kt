package com.zavediaev

fun main(args: Array<String>) {
    val path = args.firstOrNull() ?: "/Users/zavediaev-work/workspace/1brc/measurements.txt"

//    attempt1(path) // 3:50.75
//    attempt2(path) // 2:38.56
//    attempt3(path) // 42.968
    attempt4(path) // 6.052
}