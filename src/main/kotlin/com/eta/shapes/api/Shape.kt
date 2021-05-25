package com.eta.shapes.api

import com.eta.shapes.api.geometry.Geometry
import com.eta.shapes.api.geometry.GeometryType

/**
 * Stored shape with particular geometry implementation corresponding to the type
 *
 * This POJO is backed by a persistent entity object [com.eta.shapes.impl.persistence.ShapeEntity],
 * but exposes only the data we want to provide via API in required format, not all the persisted fields
 */
class Shape(
    val id: Long,
    val name: String,
    val type: GeometryType,
    val geometry: Geometry
) {
    init {
        // Because enums cannot be generic, it's impossible to get a compile time type/geometry checks
        require(type.geometryClass == geometry::class) { "$geometry should be of ${type.geometryClass}" }
    }
}
