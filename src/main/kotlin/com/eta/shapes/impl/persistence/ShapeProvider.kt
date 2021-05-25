package com.eta.shapes.impl.persistence

import com.eta.shapes.api.geometry.Geometry
import com.eta.shapes.api.geometry.Rectangle
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

/**
 * A semantic wrapper on top of a [ShapeRepository]
 * which encapsulates entity specifics and exposes only the business logic level objects.
 *
 * Maybe could be omitted in such a small app, but is a must for production level application
 */
@Component
internal class ShapeProvider(private val repository: ShapeRepository, private val mapper: ShapeMapper) {

    fun persist(name: String, boundary: Rectangle, geometry: Geometry): Long {
        val entity = mapper.toEntity(name, boundary, geometry)
        return repository.saveAndFlush(entity).id
        // should never happen as upon persist an id must be provided by DB automatically
            ?: throw IllegalStateException("Persisted $entity has no id")
    }

    fun isNamePersisted(name: String) = repository.existsByName(name)

    /**
     * @param area to search for shapes in. If not provided - search for all the shapes
     *
     * @return all shapes within (or boundaries intersecting with) the provided area
     */
    fun getShapes(area: Rectangle?, pageable: Pageable) = getEntities(area, pageable).map { mapper.toShape(it) }

    private fun getEntities(area: Rectangle?, pageable: Pageable) =
        area?.let { repository.findIntersections(area.x1, area.x2, area.y1, area.y2, pageable) }
            ?: repository.findAll(pageable)

}
