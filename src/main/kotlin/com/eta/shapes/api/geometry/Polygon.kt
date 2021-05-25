package com.eta.shapes.api.geometry

/**
 * A polygon described by a list of its vertices
 */
data class Polygon(val points: List<Point>) : Geometry(GeometryType.POLYGON)
