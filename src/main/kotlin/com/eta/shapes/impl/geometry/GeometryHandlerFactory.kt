package com.eta.shapes.impl.geometry

import com.eta.shapes.api.geometry.Geometry
import org.springframework.stereotype.Component

@Component
internal class GeometryHandlerFactory(handlersList: List<GeometryHandler<*>>) {

    /**
     * A map of all the geometry handlers defined in the system by the geometry class they work with
     */
    private val handlers = handlersList.associateBy { it.type.geometryClass }

    /**
     * @return handler defined for the provided geometry
     */
    fun <T : Geometry> getHandler(geometry: T) =
        // Because of non-generic nature of enums we have to add such an unsafe cast here,
        // for the sake of simplicity
        handlers[geometry::class] as GeometryHandler<T>?
            ?: throw IllegalStateException("Unknown geometry found: $geometry")
}
