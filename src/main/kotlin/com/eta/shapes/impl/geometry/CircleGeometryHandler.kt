package com.eta.shapes.impl.geometry

import com.eta.shapes.api.geometry.*
import com.eta.shapes.api.geometry.GeometryType.CIRCLE
import org.springframework.stereotype.Component

@Component
internal class CircleGeometryHandler(private val polygonHandler: PolygonGeometryHandler) :
    GeometryHandler<Circle>(CIRCLE) {

    override fun getBoundary(geometry: Circle) = Rectangle(
        geometry.center.x - geometry.radius,
        geometry.center.x + geometry.radius,
        geometry.center.y - geometry.radius,
        geometry.center.y + geometry.radius,
    )

    override fun intersect(geometry1: Circle, geometry2: Geometry): Geometry? = when (geometry2::class) {
        Rectangle::class -> intersectRectangle(geometry1, geometry2 as Rectangle)
        Circle::class -> intersectCircle(geometry1, geometry2 as Circle)
        Polygon::class -> polygonHandler.intersect(geometry2 as Polygon, geometry1)
        else -> throw IllegalStateException("Unknown geometry found: $geometry2")
    }

    /**
     * TODO("Not yet implemented")
     * Using rectangle intersection as a short cut. At least it's something
     */
    private fun intersectCircle(c1: Circle, c2: Circle) = intersectRectangle(c1, getBoundary(c2))

    /**
     * TODO("Not yet implemented")
     * Using rectangle intersection as a short cut. At least it's something
     */
    private fun intersectRectangle(circle: Circle, rectangle: Rectangle) =
        intersectRectangle(getBoundary(circle), rectangle)

}
