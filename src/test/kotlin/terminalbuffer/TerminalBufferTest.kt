package io.github.vljn.terminalbuffer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.EnumSet

class TerminalBufferTest {
    @Test
    fun `buffer should be initialized with empty lines`() {
        val buffer = TerminalBuffer(5, 5, 5)
        for (i in 0..4) {
            assertEquals("     ", buffer.getLine(i))
        }
    }

    @Test
    fun `fillLine should populate entire line with specific character`() {
        val buffer = TerminalBuffer(10, 5, 5)
        buffer.moveCursor(2, 0)
        buffer.fillLine('a')

        assertEquals("aaaaaaaaaa", buffer.getLine(2))
        assertEquals(2 to 0, buffer.getCursorPosition())
    }

    @Test
    fun `first line should go into the scrollback after bottom is reached`() {
        val buffer = TerminalBuffer(width = 6, height = 2, maxScrollback = 5)

        buffer.writeText("LINE1\n")
        buffer.writeText("LINE2\n")
        buffer.writeText("LINE3")

        assertEquals("LINE1 ", buffer.getLine(0))
        assertEquals("LINE2 \nLINE3 ", buffer.getScreenAsString())
    }

    @Test
    fun `first line should disappear when maxScrollback is 0`() {
        val buffer = TerminalBuffer(width = 10, height = 2, maxScrollback = 0)

        buffer.writeText("LINE1\n")
        buffer.writeText("LINE2\n")
        buffer.writeText("LINE3")

        assertEquals("LINE2     ", buffer.getLine(0))
        assertEquals("LINE3     ", buffer.getLine(1))
    }

    @Test
    fun `clearScreen should reset only visible area`() {
        val buffer = TerminalBuffer(10, 2, 5)
        buffer.writeText("SCRL\n")
        buffer.writeText("VIS1\n")
        buffer.writeText("VIS2")

        buffer.clearScreen()

        assertEquals("SCRL      ", buffer.getLine(0))
        assertEquals("          ", buffer.getLine(1))
        assertEquals("          ", buffer.getLine(2))
        assertEquals(0 to 0, buffer.getCursorPosition())
    }

    @Test
    fun `writeText should wrap to next line when width is exceeded`() {
        val buffer = TerminalBuffer(width = 5, height = 3, maxScrollback = 5)

        buffer.writeText("ABCDEFG")

        assertEquals("ABCDE", buffer.getLine(0))
        assertEquals("FG   ", buffer.getLine(1))
        val (row, col) = buffer.getCursorPosition()
        assertEquals(1, row)
        assertEquals(2, col)
    }

    @Test
    fun `insertText should shift content and wrap correctly`() {
        val buffer = TerminalBuffer(width = 10, height = 5, maxScrollback = 5)
        buffer.writeText("Hello World")

        buffer.moveCursor(0, 0)
        buffer.insertText("TEST")

        assertEquals("TESTHello ", buffer.getLine(0))
    }

    @Test
    fun `getting cell data out of bounds should not be allowed`() {
        val buffer = TerminalBuffer(width = 10, height = 5, maxScrollback = 5)
        buffer.fillLine('a')

        assertEquals(Attributes(), buffer.getAttributesAt(0, 5))
        assertThrows<IndexOutOfBoundsException> {
             buffer.getCharacterAt(100, 100)
        }
        assertThrows<IndexOutOfBoundsException> {
            buffer.getCharacterAt(-1, -1)
        }
    }
}