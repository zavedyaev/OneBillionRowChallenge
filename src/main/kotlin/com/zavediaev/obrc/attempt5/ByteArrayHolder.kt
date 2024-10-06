package com.zavediaev.obrc.attempt5

class ByteArrayHolder(
    val byteArray: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is ByteArrayHolder) {
            return false
        }
        return byteArray.contentEquals(other.byteArray)
    }

    override fun hashCode(): Int {
        return byteArray.contentHashCode()
    }
}