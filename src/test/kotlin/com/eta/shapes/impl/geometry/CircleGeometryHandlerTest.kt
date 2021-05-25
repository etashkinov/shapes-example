package com.eta.shapes.impl.geometry

import com.eta.shapes.api.geometry.Circle
import com.eta.shapes.api.geometry.Point
import com.eta.shapes.api.geometry.Rectangle
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CircleGeometryHandlerTest {
    private val polygonHandler: PolygonGeometryHandler = mock()
    private val handler = CircleGeometryHandler(polygonHandler)

    @Test
    fun `should calculate boundary`() {
        //given
        val circle = Circle(Point(0, 0), 10)

        //when
        val boundary = handler.getBoundary(circle)

        //then
        assertThat(boundary).isEqualTo(Rectangle(-10, 10, -10, 10))
    }
}
