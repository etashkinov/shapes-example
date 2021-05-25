package com.eta.shapes.impl

import com.eta.shapes.api.ShapeService
import com.eta.shapes.api.exception.IntersectionException
import com.eta.shapes.api.exception.NameAlreadyExistsException
import com.eta.shapes.api.geometry.Geometry
import com.eta.shapes.api.geometry.Rectangle
import com.eta.shapes.impl.geometry.GeometryHandlerFactory
import com.eta.shapes.impl.persistence.ShapeProvider
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Implementation of the API interface
 *
 * Should be more an orchestration class for all the downstream logic.
 *
 * In this task it might look like an overhead and over-engineering,
 * as almost all the logic can easily reside here
 * without creating small helpers like [ShapeProvider] or [GeometryHandlerFactory].
 *
 * But in large scale projects it's extremely important
 * to break down the logic in small chunks to decrease the cognitive complexity
 */
@Service
internal class ShapeServiceImpl(
    private val provider: ShapeProvider,
    private val geometryHandlerFactory: GeometryHandlerFactory,
) : ShapeService {

    override fun addShape(name: String, geometry: Geometry): Long {

        // We will fail on insert of a non-unique name anyway, thanks to the unique name index.
        // However, before persisting we do a heavy intersection check,
        // so it's better to do a quick (thanks to the index again) check first
        if (provider.isNamePersisted(name)) {
            throw NameAlreadyExistsException(name)
        }

        val handler = geometryHandlerFactory.getHandler(geometry)
        val boundary = handler.getBoundary(geometry)

        // for simplicity pagination is not used here,
        // but in a case of huge entities volume,
        // it might be needed to handle intersections page by page. Added here just as a marker for future
        val intersections = getShapes(boundary, Pageable.unpaged())
            // once we found all the boundary intersections
            // we do an app level fine intersection calculations.
            // Makes not much sense for rectangles but is required for more complex geometries
            // Not yet implemented properly though.
            .filter { handler.intersect(geometry, it.geometry) != null }

        // It could be useful to reply to the client with the list of conflicting shapes
        if (intersections.isNotEmpty()) {
            throw IntersectionException(intersections)
        }

        return provider.persist(name, boundary, geometry)
    }

    override fun getShapes(area: Rectangle?, pageable: Pageable) =
        provider.getShapes(area, pageable)
}
