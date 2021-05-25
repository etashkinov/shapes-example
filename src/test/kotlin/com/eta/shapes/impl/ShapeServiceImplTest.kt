package com.eta.shapes.impl

import com.eta.shapes.api.Shape
import com.eta.shapes.api.exception.IntersectionException
import com.eta.shapes.api.exception.NameAlreadyExistsException
import com.eta.shapes.api.geometry.GeometryType.RECTANGLE
import com.eta.shapes.api.geometry.Rectangle
import com.eta.shapes.impl.geometry.GeometryHandlerFactory
import com.eta.shapes.impl.geometry.RectangleGeometryHandler
import com.eta.shapes.impl.persistence.ShapeProvider
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Pageable

internal class ShapeServiceImplTest {

    private val provider = mock<ShapeProvider>()
    private val handlerFactory = mock<GeometryHandlerFactory>()

    private val service = ShapeServiceImpl(provider, handlerFactory)

    private val handler = mock<RectangleGeometryHandler>()
    private val id = 1L
    private val geometry = Rectangle(1, 2, 3, 4)
    private val name = "name"

    @BeforeEach
    fun setUp() {
        given { handlerFactory.getHandler(geometry) }.willReturn(handler)
        given { handler.getBoundary(geometry) }.willReturn(geometry)
        given { provider.persist(name, geometry, geometry) }.willReturn(id)
    }

    @Test
    fun `should add shape`() {
        //given
        given { provider.getShapes(geometry, Pageable.unpaged()) }.willReturn(emptyList())

        //when
        val result = service.addShape(name, geometry)

        //then
        assertThat(result).isEqualTo(id)
    }

    @Test
    fun `should check shape name`() {
        //given
        given { provider.isNamePersisted(name) }.willReturn(true)

        //when
        assertThatThrownBy { service.addShape(name, geometry) }
            // then
            .isInstanceOf(NameAlreadyExistsException::class.java)
    }

    @Test
    fun `should check shape intersection`() {
        //given
        val shape = Shape(1L, name, RECTANGLE, geometry)

        given { provider.getShapes(geometry, Pageable.unpaged()) }.willReturn(listOf(shape))
        given { handler.intersect(geometry, geometry) }.willReturn(geometry)

        //when
        assertThatThrownBy { service.addShape(name, geometry) }
            // then
            .isInstanceOf(IntersectionException::class.java)
    }
}