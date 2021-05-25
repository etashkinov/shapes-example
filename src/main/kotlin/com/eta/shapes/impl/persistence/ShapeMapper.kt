package com.eta.shapes.impl.persistence

import com.eta.shapes.api.Shape
import com.eta.shapes.api.geometry.Geometry
import com.eta.shapes.api.geometry.GeometryType
import com.eta.shapes.api.geometry.Rectangle
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

/**
 * A helper class to translate an entity to an API POJO and vice versa.
 *
 * All different geometry implementations are serialised into a json string to be stored in the DB
 *
 * Super useful lib [Mapstruct](https://mapstruct.org/) can help with these mappings a lot,
 * but in this case, just manual mapping will do
  */
@Component
internal class ShapeMapper(private val objectMapper: ObjectMapper) {

    fun toEntity(name: String, boundary: Rectangle, geometry: Geometry) = ShapeEntity(
        null,
        name,
        geometry.type.name,
        boundary.x1, boundary.x2, boundary.y1, boundary.y2,
        objectMapper.writeValueAsString(geometry)
    )

    fun toShape(entity: ShapeEntity): Shape {
        val type = GeometryType.valueOf(entity.type)
        val geometry = objectMapper.readValue(entity.geometry, type.geometryClass.java)
        return Shape(entity.id!!, entity.name, type, geometry)
    }

}
