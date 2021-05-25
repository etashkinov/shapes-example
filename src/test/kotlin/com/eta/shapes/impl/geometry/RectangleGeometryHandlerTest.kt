package com.eta.shapes.impl.geometry

import com.eta.shapes.api.geometry.*
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RectangleGeometryHandlerTest {
    private val circleHandler: CircleGeometryHandler = mock()
    private val polygonHandler: PolygonGeometryHandler = mock()
    private val handler = RectangleGeometryHandler(circleHandler, polygonHandler)

    @Test
    fun `should calculate boundary`() {
        //given
        val rectangle = Rectangle(1, 2, 1, 4)

        //when
        val boundary = handler.getBoundary(rectangle)

        //then
        assertThat(boundary).isEqualTo(rectangle)
    }

    @Test
    fun `should find partial intersection`() = `should find intersection`(
        Rectangle(1, 10, 1, 10),
        Rectangle(2, 20, 5, 30),
        Rectangle(2, 10, 5, 10)
    )

    @Test
    fun `should find inner intersection`() = `should find intersection`(
        Rectangle(1, 10, 1, 10),
        Rectangle(2, 5, 2, 5),
        Rectangle(2, 5, 2, 5)
    )


    @Test
    fun `should find outer intersection`() = `should find intersection`(
        Rectangle(1, 10, 1, 10),
        Rectangle(0, 20, 0, 20),
        Rectangle(1, 10, 1, 10)
    )

    @Test
    fun `should ignore border intersection`() = `should find intersection`(
        Rectangle(1, 10, 1, 10),
        Rectangle(1, 10, 10, 30),
        null
    )

    @Test
    fun `should ignore non intersecting`() = `should find intersection`(
        Rectangle(1, 10, 1, 10),
        Rectangle(100, 110, 100, 300),
        null
    )

    @Test
    fun `should intersect circle`() {
        //given
        val rectangle = Rectangle(1, 2, 4, 5)
        val circle = Circle(Point(1, 1), 10)

        val expected: Geometry = mock()
        given(circleHandler.intersect(circle, rectangle)).willReturn(expected)

        //when
        val actual = handler.intersect(rectangle, circle)

        //then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should intersect polygon`() {
        //given
        val rectangle = Rectangle(1, 2, 4, 5)
        val polygon = Polygon(listOf(Point(1, 1)))

        val expected: Geometry = mock()
        given(polygonHandler.intersect(polygon, rectangle)).willReturn(expected)

        //when
        val actual = handler.intersect(rectangle, polygon)

        //then
        assertThat(actual).isEqualTo(expected)
    }

    private fun `should find intersection`(geometry1: Rectangle, geometry2: Rectangle, expected: Rectangle?) {
        //when
        val intersection = handler.intersect(geometry1, geometry2)

        //then
        assertThat(intersection).isEqualTo(expected)
    }
}
