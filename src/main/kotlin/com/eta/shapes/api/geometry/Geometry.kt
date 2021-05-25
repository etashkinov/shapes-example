package com.eta.shapes.api.geometry

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Parent class for all the different geometries.
 * This approach allows us working not only with squares or rectangles,
 * but also with  polygons and even more complex shapes like circles, ellipsis or composite shapes,
 * in a generic way.
 *
 * Children must provide the geometry type which uniquely defines it
 */
abstract class Geometry(@JsonIgnore val type: GeometryType)
