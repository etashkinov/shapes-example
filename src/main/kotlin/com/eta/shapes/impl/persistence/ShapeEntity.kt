package com.eta.shapes.impl.persistence

import javax.persistence.*

/**
 * Persistence entity,
 * Represents a generic shape.
 *
 * Standard SQL DB is used in this implementation as no specific needs for a particular NoSQL exist
 *
 * Depending on the particular DB used in production environment, indexes should be fine tuned properly
 *
 * But for the moment they just represent here the fact that we plan to query a lot these fields,
 * hence they require a proper index. Also unique indexes tend to have better performance usually
 */
@Entity
@Table(
    indexes = [
        Index(name = "name", columnList = "name", unique = true),
        Index(name = "bounding", columnList = "x1, x2, y1, y2", unique = true),
    ]
)
internal data class ShapeEntity(
    /**
     * Would personally prefer to avoid such an ugly nullable id,
     * but, alas, that's how JPA works with auto-generated ids
     */
    @Id
    @GeneratedValue
    val id: Long? = null,
    val name: String,

    val type: String,

    /**
     * These columns define a shape boundary
     *
     * Even though not all the shapes are going to be rectangles it's a good idea store a bounding box in DB.
     *
     * This allows us to perform a quick DB level query to find at least boundary intersections first,
     * and do the fine intersection calculations on the app level if needed after that,
     * not to query for all the shapes in the DB
     */
    val x1: Int,
    val x2: Int,
    val y1: Int,
    val y2: Int,

    /**
     * Serialised geometry.
     *
     * Each geometry type gonna have a unique set of features,
     * so to store them all in one place it's reasonable to serialise the particular geometry to json.
     *
     * In production DBs (like PostgreSQL) column types like
     * <i>jsonb</i> could be used instead of a plain generic string
     */
    val geometry: String,
)
