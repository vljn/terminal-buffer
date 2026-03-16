package io.github.vljn.terminalbuffer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.EnumSet

class LineTest {
    @Test
    fun `line should be initialized with spaces`() {
        val width = 10
        val line = Line(width)

        for (i in 0 until width) {
            assertEquals(' ', line[i].character, "Cell on position $i should be empty")
        }
    }

    @Test
    fun `clear should set all cells to space`() {
        val width = 10
        val line = Line(width)

        for (i in 0..<width) {
            line[i].character = 'a'
        }

        line.clear()

        for(i in 0..<width) {
            assertEquals(' ', line[i].character, "Cell on position $i should be empty")
        }
    }

    @Test
    fun `toString should represent character content correctly`(){
        val line = Line(3)
        line[0] = Cell('A')
        line[2] = Cell('B')

        assertEquals("A B", line.toString())
    }

    @Test
    fun `line should throw exception on out of bounds access`() {
        val line = Line(3)
        assertThrows<IndexOutOfBoundsException> {
            line[3]
        }
        assertThrows<IndexOutOfBoundsException> {
            line[-1]
        }
    }

    @Test
    fun `line should correctly store and retrieve cells`() {
        val line = Line(3)
        line[2] = Cell('A', Attributes(Color.RED, style = EnumSet.of(Style.BOLD)))

        assertEquals('A', line[2].character)
        assertEquals(Attributes(Color.RED, style = EnumSet.of(Style.BOLD)), line[2].attributes)
    }

    @Test
    fun `cells with same attributes should share the same instance`() {
        val line = Line(2)
        val attributes = Attributes(Color.BLUE)

        line[0] = Cell('A', attributes)
        line[1] = Cell('B', attributes)

        assertNotSame(line[0], line[1])
        assertSame(line[0].attributes, line[1].attributes)
    }
}