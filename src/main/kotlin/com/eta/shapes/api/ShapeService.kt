package com.eta.shapes.api

import com.eta.shapes.api.exception.IntersectionException
import com.eta.shapes.api.exception.NameAlreadyExistsException
import com.eta.shapes.api.geometry.Geometry
import com.eta.shapes.api.geometry.Rectangle
import org.springframework.data.domain.Pageable
import kotlin.jvm.Throws

/**
 * Main API entry point of the application
 *
 * Provides methods to work with shapes
 */
interface ShapeService {

    /**
     * Add a new shape
     *
     * @param name unique shape name
     * @param geometry shape geometry, must not intersect with any existing shapes
     *
     * @return id of the created shape
     *
     * @throws NameAlreadyExistsException if provided [name] already exists
     * @throws IntersectionException if provided [geometry] intersects with existing shapes
     */
    @Throws(NameAlreadyExistsException::class, IntersectionException::class)
    fun addShape(name: String, geometry: Geometry): Long

    /**
     * Get the existing shapes
     *
     * @param area rectangle within (or at least partially within) which to search for shapes,
     * if null - all shapes are returned,
     * with the number of shapes in the system growing this parameter should be made required
     *
     * @param pageable standard pagination parameters,
     * with the number of shapes in the system growing this parameter should be made required
     *
     * @return iterable of the existing shapes
     */
    fun getShapes(area: Rectangle?, pageable: Pageable): List<Shape>
}