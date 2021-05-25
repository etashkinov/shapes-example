package com.eta.shapes.api.exception

import com.eta.shapes.api.Shape

/**
 * Thrown when trying to add a shape intersecting with already stored shapes
 *
 * @param intersections list of shapes which the original shape is intersecting with
 */
class IntersectionException(val intersections: List<Shape>) : Exception("The shape intersects with other shapes")
