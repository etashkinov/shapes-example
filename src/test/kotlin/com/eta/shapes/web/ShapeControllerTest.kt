package com.eta.shapes.web

import com.eta.shapes.api.Shape
import com.eta.shapes.api.ShapeService
import com.eta.shapes.api.exception.IntersectionException
import com.eta.shapes.api.exception.NameAlreadyExistsException
import com.eta.shapes.api.geometry.GeometryType.RECTANGLE
import com.eta.shapes.api.geometry.Rectangle
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders


internal class ShapeControllerTest {

    private val geometry = Rectangle(1, 2, 3, 4)
    private val geometryString =
        """
            { 
                "x1": ${geometry.x1}, 
                "x2": ${geometry.x2}, 
                "y1": ${geometry.y1}, 
                "y2": ${geometry.y2}
            }
        """

    private val shapeService: ShapeService = mock()

    private val mvc = MockMvcBuilders
        .standaloneSetup(ShapeController(shapeService))
        .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
        .setControllerAdvice(ShapeExceptionHandler())
        .build()

    @Test
    fun `should get shapes`() {
        //given
        val shape = Shape(1, "shape1", RECTANGLE, geometry)
        given { shapeService.getShapes(null, PageRequest.of(0, 20)) }
            .willReturn(listOf(shape))

        //when
        val response = mvc.get("/")

        //then
        response.andExpect {
            status { isOk() }
            content {
                json(
                    """[
                    {
                        "id": ${shape.id}, 
                        "name": "${shape.name}", 
                        "type": "${shape.type.name}", 
                        "geometry": $geometryString
                    }
                    ]"""
                )
            }
        }
    }

    @Test
    fun `should add shape`() {
        //given
        val name = "shape1"
        val id: Long = 1
        given { shapeService.addShape(name, geometry) }.willReturn(id)

        //when
        val response = postShape(name)

        //then
        response.andExpect {
            status { isCreated() }
            content { string(id.toString()) }
        }
    }

    @Test
    fun `should conflict on the same name`() {
        //given
        val name = "shape1"
        given { shapeService.addShape(name, geometry) }.willThrow(NameAlreadyExistsException(name))

        //when
        val response = postShape(name)

        //then
        response.andExpect {
            status { isConflict() }
            content {
                // Inline type string is used here (instead of extracting to a constant)
                // to make sure the contract stays the same.
                // Should we use the constant from the exception handler,
                // changing it will not break the test, but will break the contract
                jsonPath("$.type", equalTo("NAME_CONFLICT"))
                jsonPath("$.message", not(empty<String>()))
                jsonPath("$.details", equalTo(name))
            }
        }
    }

    @Test
    fun `should conflict on the intersection`() {
        //given
        val name = "shape2"
        val intersection = Shape(1, "shape1", RECTANGLE, geometry)
        given { shapeService.addShape(name, geometry) }.willThrow(IntersectionException(listOf(intersection)))

        //when
        val response = postShape(name)

        //then
        response.andExpect {
            status { isConflict() }
            content {
                jsonPath("$.type", equalTo("INTERSECTION"))
                jsonPath("$.message", not(empty<String>()))
                jsonPath("$.details[0].name", equalTo(intersection.name))
            }
        }
    }

    @Test
    fun `should response with bad request on malformed geometry`() {
        //given
        val malformedGeometryString = """
            { 
                "x1": 0, 
                "x2": 0, 
                "y1": 0, 
                "y2": 0
            }
        """

        //when
        val response = mvc.post("/rectangles") {
            param("name", "shape1")
            content = malformedGeometryString
            contentType = MediaType.APPLICATION_JSON
        }

        //then
        response.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.type", equalTo("BAD_GEOMETRY"))
                jsonPath("$.message", not(empty<String>()))
                jsonPath("$.details", nullValue())
            }
        }
    }

    @Test
    fun `should require name`() {
        //when
        val response = mvc.post("/rectangles") {
            content = geometryString
            contentType = MediaType.APPLICATION_JSON
        }

        //then
        response.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `should handle 500`() {
        //given
        val name = "shape1"
        given { shapeService.addShape(name, geometry) }.willThrow(NullPointerException())

        //when
        val response = postShape(name)

        //then
        response.andExpect {
            status { isInternalServerError() }
            content {
                jsonPath("$.type", equalTo("INTERNAL_ERROR"))
                jsonPath("$.message", not(empty<String>()))
                jsonPath("$.details", nullValue())
            }
        }
    }

    private fun postShape(name: String) = mvc.post("/rectangles") {
        param("name", name)
        content = geometryString
        contentType = MediaType.APPLICATION_JSON
    }
}
