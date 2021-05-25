package com.eta.shapes.impl.geometry

import com.eta.shapes.api.geometry.*
import com.eta.shapes.api.geometry.GeometryType.POLYGON
import org.springframework.stereotype.Component

@Component
internal class PolygonGeometryHandler : GeometryHandler<Polygon>(POLYGON) {
    override fun getBoundary(geometry: Polygon): Rectangle {
        val xCoordinates = geometry.points.map { it.x }
        val yCoordinates = geometry.points.map { it.y }
        return Rectangle(
            xCoordinates.minOrNull()!!,
            xCoordinates.maxOrNull()!!,
            yCoordinates.minOrNull()!!,
            yCoordinates.maxOrNull()!!,
        )
    }

    override fun intersect(geometry1: Polygon, geometry2: Geometry): Geometry? = when (geometry2::class) {
        Rectangle::class -> intersectRectangle(geometry1, geometry2 as Rectangle)
        Circle::class -> intersectCircle(geometry1, geometry2 as Circle)
        Polygon::class -> intersectPolygon(geometry1, geometry2 as Polygon)
        else -> throw IllegalStateException("Unknown geometry found: $geometry2")
    }

    /**
     * TODO("Not yet implemented")
     * Using rectangle intersection as a short cut. At least it's something
     */
    private fun intersectPolygon(p1: Polygon, p2: Polygon) =
        intersectRectangle(p1, getBoundary(p2))

    /**
     * TODO("Not yet implemented")
     */
    private fun intersectCircle(polygon: Polygon, circle: Circle) = null

    /**
     * TODO("Not yet implemented")
     * Using rectangle intersection as a short cut. At least it's something
     */
    private fun intersectRectangle(polygon: Polygon, rectangle: Rectangle) =
        intersectRectangle(getBoundary(polygon), rectangle)
}
