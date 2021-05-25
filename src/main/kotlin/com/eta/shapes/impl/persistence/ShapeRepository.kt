package com.eta.shapes.impl.persistence

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.Repository

/**
 * Standard JPA Spring Data [Repository]
 *
 * I prefer not to use [org.springframework.data.repository.CrudRepository]
 * because in most cases they expose too much not used functionality, which could be confusing,
 * if it is supposed to be used or is there just because of the inheritance
 */
internal interface ShapeRepository : Repository<ShapeEntity, Long> {

    /**
     * Because of the unique index constraints it's useful to flush straight away,
     * to hit the constraint error sooner than later.
     *
     * Of course, in case of high load writes and low possibility of an error, this could be changed to just <i>save</i>
     */
    fun saveAndFlush(shape: ShapeEntity): ShapeEntity

    /**
     * @return all shapes which boundaries intersect with the provided area
     */
    @Query("SELECT s FROM ShapeEntity s WHERE (s.x1 < ?2 AND ?1 < s.x2) AND (s.y1 < ?4 AND ?3 < s.y2)")
    fun findIntersections(x1: Int, x2: Int, y1: Int, y2: Int, pageable: Pageable): List<ShapeEntity>

    fun findAll(pageable: Pageable): List<ShapeEntity>

    fun existsByName(name: String): Boolean
}
