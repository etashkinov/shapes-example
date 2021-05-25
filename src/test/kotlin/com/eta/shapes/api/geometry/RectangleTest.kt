package com.eta.shapes.api.geometry

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class RectangleTest {
    @Test
    fun `should require ordered x`() {
        assertThatIllegalArgumentException()
            .isThrownBy { Rectangle(2, 1, 5, 6) }
    }

    @Test
    fun `should require ordered y`() {
        assertThatIllegalArgumentException()
            .isThrownBy { Rectangle(1, 2, 6, 5) }
    }

    @Test
    fun `should reject same x`() {
        assertThatIllegalArgumentException()
            .isThrownBy { Rectangle(1, 1, 5, 6) }
    }

    @Test
    fun `should reject same y`() {
        assertThatIllegalArgumentException()
            .isThrownBy { Rectangle(1, 2, 5, 5) }
    }

    @Test
    fun `should allow ordered coordinates`() {
        assertDoesNotThrow { Rectangle(1, 2, 5, 6) }
    }
}
