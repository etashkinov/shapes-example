package com.eta.shapes.api.geometry

import kotlin.reflect.KClass

/**
 * Type of the geometry uniquely related to a particular geometry class
 */
enum class GeometryType(val geometryClass: KClass<out Geometry>) {
    RECTANGLE(Rectangle::class),
    CIRCLE(Circle::class),
    POLYGON(Polygon::class),
}
