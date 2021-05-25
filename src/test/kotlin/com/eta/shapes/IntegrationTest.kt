package com.eta.shapes

import com.eta.shapes.api.Shape
import com.eta.shapes.api.geometry.GeometryType
import com.eta.shapes.api.geometry.Rectangle
import com.eta.shapes.impl.persistence.ShapeEntity
import com.eta.shapes.impl.persistence.ShapeRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.given
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
internal class IntegrationTest @Autowired constructor(
    private val mvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    @Test
    fun `should add and get shapes`() {
        //given
        val name = "shape1"

        //when
        mvc.post("/rectangles") {
            param("name", name)
            content = objectMapper.writeValueAsString(Rectangle(1, 2, 3, 4))
            contentType = MediaType.APPLICATION_JSON
        }

        val response = mvc.get("/").andReturn().response.contentAsString

        //then
        val shapes = objectMapper.readValue(response, ArrayList<Shape>(0)::class.java)
        assertThat(shapes).hasSize(1)
    }
}
