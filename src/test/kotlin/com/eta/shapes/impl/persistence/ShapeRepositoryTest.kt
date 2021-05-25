package com.eta.shapes.impl.persistence

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*

@DataJpaTest
internal class ShapeRepositoryTest @Autowired constructor(private val repository: ShapeRepository) {

    private lateinit var shape1: ShapeEntity
    private lateinit var shape2: ShapeEntity
    private lateinit var shape3: ShapeEntity
    private lateinit var shape4: ShapeEntity

    @BeforeEach
    internal fun setUp() {
        shape1 = persistRectangle(1, 6, 1, 6)
        shape2 = persistRectangle(12, 20, 2, 8)
        shape3 = persistRectangle(2, 8, 20, 22)
        shape4 = persistRectangle(12, 20, 20, 22)
    }

    @ParameterizedTest
    @MethodSource("boundaries")
    fun `should find intersection`(x1: Int, x2: Int, y1: Int, y2: Int, intersect: Int) {
        //when
        val result = repository.findIntersections(x1, x2, y1, y2, Pageable.unpaged())

        //then
        assertThat(result).hasSize(intersect)
    }

    companion object {
        @JvmStatic
        fun boundaries() = listOf(
            Arguments.of(1, 6, 1, 6, 1),
            Arguments.of(0, 7, 0, 7, 1),
            Arguments.of(2, 5, 2, 5, 1),
            Arguments.of(8, 10, 9, 19, 0),
            Arguments.of(1, 6, 6, 7, 0),
            Arguments.of(6, 7, 6, 7, 0),
            Arguments.of(0, 100, 0, 100, 4),
        )
    }

    @Test
    fun `should store shape`() {
        //given
        val entity = createRectangle(100, 101, 100, 101)

        //when
        val persistedEntity = repository.saveAndFlush(entity)

        //then
        assertThat(persistedEntity.id).isNotNull
        assertThat(persistedEntity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(entity)
    }

    @Test
    fun `should allow non intersecting boundary`() {
        //when
        val result = repository.findIntersections(8, 10, 9, 19, Pageable.unpaged())

        //then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should constrict same boundaries`() {
        //given
        val entity = createRectangle(shape1.x1, shape1.x2, shape1.y1, shape1.y2)

        //when
        assertThatThrownBy { repository.saveAndFlush(entity) }
            // then
            .isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    fun `should constrict same name`() {
        //given
        val entity = ShapeEntity(null, shape1.name, "shape_type", 100, 100, 101, 101, "geometry")

        //when
        assertThatThrownBy { repository.saveAndFlush(entity) }
            // then
            .isInstanceOf(DataIntegrityViolationException::class.java)
    }

    @Test
    fun `should find all shapes`() {
        //when
        val shapes = repository.findAll(Pageable.unpaged())

        // then
        assertThat(shapes).containsExactly(shape1, shape2, shape3, shape4)
    }

    @Test
    fun `should find pages shapes`() {
        //when
        val shapes = repository.findAll(PageRequest.of(1, 2))

        // then
        assertThat(shapes).hasSize(2)
    }

    private fun createRectangle(x1: Int, x2: Int, y1: Int, y2: Int) =
        ShapeEntity(null, UUID.randomUUID().toString(), "type", x1, x2, y1, y2, "geometry")

    private fun persistRectangle(x1: Int, x2: Int, y1: Int, y2: Int): ShapeEntity {
        val entity = createRectangle(x1, x2, y1, y2)
        return repository.saveAndFlush(entity)
    }

}
