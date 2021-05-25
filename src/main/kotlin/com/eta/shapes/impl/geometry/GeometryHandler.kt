package com.eta.shapes.impl.geometry

import com.eta.shapes.api.geometry.Geometry
import com.eta.shapes.api.geometry.GeometryType
import com.eta.shapes.api.geometry.Rectangle

/**
 * Base class for specific geometry logic, like calculating two geometries intersection or a boundary box
 *
 * This allows us to work with any geometry types in a generic way
 * by encapsulating all the specifics in the children's implementation
 *
 * Potentially all this logic could be moved to corresponding geometry classes,
 * but this will break the concept of .api. package where no logic resides,
 * only the definition of the app interface.
 *
 * So these handlers is a small overhead for the sake of a clean architecture
 */
internal abstract class GeometryHandler<T : Geometry>(val type: GeometryType) {

    /**
     * @return bounding box of the geometry
     */
    abstract fun getBoundary(geometry: T): Rectangle

    /**
     * TODO("Not yet implemented")
     * Not implemented properly, added here as just an example of a further system evolution direction
     *
     * @return geometry which represents an intersection of the provided parameters
     */
    abstract fun intersect(geometry1: T, geometry2: Geometry): Geometry?

    protected fun intersectRectangle(r1: Rectangle, r2: Rectangle) =
        if (isIntersecting(r1, r2)) {
            val xCoordinates = listOf(r1.x1, r1.x2, r2.x1, r2.x2).sorted()
            val yCoordinates = listOf(r1.y1, r1.y2, r2.y1, r2.y2).sorted()
            Rectangle(xCoordinates[1], xCoordinates[2], yCoordinates[1], yCoordinates[2])
        } else {
            null
        }

    private fun isIntersecting(r1: Rectangle, r2: Rectangle) =
        (r1.x1 < r2.x2 && r2.x1 < r1.x2) && (r1.y1 < r2.y2 && r2.y1 < r1.y2)
}
