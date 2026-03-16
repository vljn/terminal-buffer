package io.github.vljn.terminalbuffer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CursorTest {
    @Test
    fun `cursor should start at position (0,0)`() {
        val cursor = Cursor(10, 5)
        assertEquals(0, cursor.row)
        assertEquals(0, cursor.column)
    }

    @Test
    fun `moveTo should move the cursor within bounds`() {
        val cursor = Cursor(10, 5)
        cursor.moveTo(4, 5)
        assertEquals(4, cursor.row)
        assertEquals(5, cursor.column)
    }

    @Test
    fun `moveTo should clamp the cursor values to its dimensions`() {
        val cursor = Cursor(10, 5)
        cursor.moveTo(100, 100)
        assertEquals(4, cursor.row)
        assertEquals(9, cursor.column)

        cursor.moveTo(-1, -5)
        assertEquals(0, cursor.row)
        assertEquals(0, cursor.column)
    }

    @Test
    fun `moveRowBy and moveColumnBy should move the cursor relatively`() {
        val cursor = Cursor(10, 5)
        cursor.moveRowBy(3)
        cursor.moveColumnBy(-3)
        assertEquals(3, cursor.row)
        assertEquals(0, cursor.column)
    }
}