package com.eta.shapes.impl.geometry

import com.eta.shapes.api.geometry.*
import com.eta.shapes.api.geometry.GeometryType.RECTANGLE
import org.springframework.stereotype.Component
import java.lang.Integer.max
import kotlin.math.min

@Component
internal class RectangleGeometryHandler(
    private val circleHandler: CircleGeometryHandler,
    private val polygonHandler: PolygonGeometryHandler,
) : GeometryHandler<Rectangle>(RECTANGLE) {
    override fun getBoundary(geometry: Rectangle) = geometry

    override fun intersect(geometry1: Rectangle, geometry2: Geometry) = when (geometry2::class) {
        Rectangle::class -> intersectRectangle(geometry1, geometry2 as Rectangle)
        Circle::class -> circleHandler.intersect(geometry2 as Circle, geometry1)
        Polygon::class -> polygonHandler.intersect(geometry2 as Polygon, geometry1)
        else -> throw IllegalStateException("Unknown geometry found: $geometry2")
    }

}
