package com.readboy.onestroke


class Point(val x: Int, val y: Int,rectnum:Int) {


    init {
        if (x < 0 || y < 0 || x >= rectnum || y >= rectnum) {
            throw IllegalArgumentException("startX and y must be greater than 0 and less than $rectnum")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "Point(x=$x, y=$y)"
    }
}