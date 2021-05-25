package com.eta.shapes.api.geometry

import com.eta.shapes.api.geometry.GeometryType.CIRCLE

/**
 * Circle shape defined by a center and a positive radius.
 *
 * Looking forward could be a child of a generic second order curve
 */
data class Circle(val center: Point, val radius: Int) : Geometry(CIRCLE) {
    init {
        require(radius > 0) { "Radius ($radius) should positive" }
    }
}
