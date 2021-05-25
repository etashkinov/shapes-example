package com.eta.shapes.impl.persistence

import com.eta.shapes.api.Shape
import com.eta.shapes.api.geometry.GeometryType.RECTANGLE
import com.eta.shapes.api.geometry.Rectangle
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Pageable
import java.util.stream.Stream
import kotlin.streams.toList

internal class ShapeProviderTest {

    private val repository = mock<ShapeRepository>()
    private val mapper = mock<ShapeMapper>()
    private val provider = ShapeProvider(repository, mapper)

    @Test
    fun `should persist shape`() {
        //given
        val name = "name"
        val boundary = Rectangle(1, 2, 1, 2)
        val geometry = Rectangle(1, 2, 3, 4)

        val entity = newEntity()

        given { mapper.toEntity(name, boundary, geometry) }.willReturn(entity)
        given { repository.saveAndFlush(entity) }.willReturn(entity)

        //when
        val result = provider.persist(name, boundary, geometry)

        //then
        assertThat(result).isEqualTo(entity.id)
    }

    @Test
    fun `should find shapes`() {
        //given
        val shape = Shape(1, "name", RECTANGLE, Rectangle(1, 2, 3, 4))
        given { mapper.toShape(any()) }.willReturn(shape)

        // Don't forget about [limit] here, otherwise it will try to generate an infinite list
        val entities = Stream.generate { newEntity() }.limit(5).toList()
        given { repository.findAll(Pageable.unpaged()) }.willReturn(entities)

        //when
        val result = provider.getShapes(null, Pageable.unpaged())

        //then
        assertThat(result).hasSize(entities.size)
        assertThat(result).containsOnly(shape)
    }


    private fun newEntity() = ShapeEntity(1L, "name", "type", 1, 2, 3, 4, "geometry")
}
