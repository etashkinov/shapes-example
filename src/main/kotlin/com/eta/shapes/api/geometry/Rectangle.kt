package com.eta.shapes.api.geometry

/**
 * Axis aligned rectangle
 *
 * Coordinates must be ordered:
 * - [x1] < [x2]
 * - [y1] < [y2]
 */
data class Rectangle(val x1: Int, val x2: Int, val y1: Int, val y2: Int) : Geometry(GeometryType.RECTANGLE) {
    init {
        require(x1 < x2) { "x1($x1) should be less than x2($x2)" }
        require(y1 < y2) { "y1($y1) should be less than y2($y2)" }
    }
}
