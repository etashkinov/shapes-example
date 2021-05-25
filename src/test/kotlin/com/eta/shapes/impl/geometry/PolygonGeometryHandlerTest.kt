package com.eta.shapes.impl.geometry

import com.eta.shapes.api.geometry.Point
import com.eta.shapes.api.geometry.Polygon
import com.eta.shapes.api.geometry.Rectangle
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PolygonGeometryHandlerTest {
    private val handler = PolygonGeometryHandler()

    @Test
    fun `should calculate boundary`() {
        //given
        val polygon = Polygon(listOf(Point(0, 0), Point(0, 10), Point(10, 0)))

        //when
        val boundary = handler.getBoundary(polygon)

        //then
        assertThat(boundary).isEqualTo(Rectangle(0, 10, 0, 10))
    }

}
