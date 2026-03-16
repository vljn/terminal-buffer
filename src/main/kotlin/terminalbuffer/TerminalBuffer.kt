package io.github.vljn.terminalbuffer

import java.util.EnumSet

class TerminalBuffer(
    val width: Int,
    val height: Int,
    val maxScrollback: Int
) {
    private val allLines = ArrayDeque<Line>()

    private var activeForeground = Color.DEFAULT
    private var activeBackground = Color.DEFAULT
    private var activeStyles = EnumSet.noneOf(Style::class.java)

    private val cursor = Cursor(width, height)

    init {
        repeat(height) { allLines.add(Line(width)) }
    }

    private val scrollbackSize: Int
        get() {
            return allLines.size - height
        }

    private fun getAbsoluteRow(screenRow: Int): Int {
        return scrollbackSize + screenRow
    }

    fun setAttributes(fg: Color, bg: Color, styles: EnumSet<Style>) {
        activeForeground = fg
        activeBackground = bg
        activeStyles = EnumSet.copyOf(styles)
    }

    fun getCursorPosition(): Pair<Int, Int> {
        return cursor.row to cursor.column
    }

    fun moveCursor(row: Int, column: Int) {
        cursor.moveTo(row, column)
    }

    fun moveCursorRelative(row: Int, column: Int) {
        cursor.moveRowBy(row)
        cursor.moveColumnBy(column)
    }

    fun advanceCursor() {
        if (cursor.column < width - 1) {
            cursor.nextColumn()
        } else {
            cursor.column = 0
            if (cursor.row < height - 1) {
                cursor.row++
            } else {
                insertBottom()
            }
        }
    }

    fun writeText(text: String) {
        val stylesSnapshot = EnumSet.copyOf(activeStyles)
        val currentAttributes = Attributes(activeForeground, activeBackground, stylesSnapshot)
        for (char in text) {
            val absRow = getAbsoluteRow(cursor.row)
            val line = allLines[absRow]

            line[cursor.column] = Cell(
                character = char,
                currentAttributes
            )

            advanceCursor()
        }
    }

    fun insertBottom() {
        allLines.add(Line(width))
        if (allLines.size > maxScrollback + height) {
            allLines.removeFirst()
        }
    }

    fun clearScreen() {
        for (i in 0 until height) {
            allLines[getAbsoluteRow(i)].clear()
        }
        cursor.moveTo(0, 0)
    }

    fun clearAll() {
        allLines.clear()
        repeat(height) { allLines.add(Line(width)) }
        cursor.moveTo(0, 0)
    }

    fun fillLine(char: Char) {
        val row = getAbsoluteRow(cursor.row)
        val line = allLines[row]
        val styles = EnumSet.copyOf(activeStyles)
        val attributes = Attributes(activeForeground, activeBackground, styles)
        for (i in 0..<width) {
            line[i] = Cell(char, attributes)
        }
    }

    fun getScreenAsString(): String {
        val sb = StringBuilder()
        for (i in 0 until height) {
            val line = allLines[getAbsoluteRow(i)]
            sb.append(line.toString()).append("\n")
        }
        sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }

    fun getFullContentAsString(): String {
        return allLines.joinToString("\n") { it.toString() }
    }

    fun getLine(index: Int): String {
        if (index < 0 || index >= allLines.size) {
            throw IndexOutOfBoundsException("Line index must be between 0 and ${allLines.size}")
        }

        return allLines[index].toString()
    }

    private fun validateIndices(absoluteRow: Int, absoluteColumn: Int) {
        if (absoluteRow !in 0..<allLines.size) {
            throw IndexOutOfBoundsException("Row $absoluteRow is out of bounds (max: ${allLines.size - 1})")
        }
        if (absoluteColumn !in 0..<width) {
            throw IndexOutOfBoundsException("Column $absoluteColumn is out of bounds (max: ${width - 1})")
        }
    }

    fun getCharacterAt(absoluteRow: Int, absoluteColumn: Int): Char {
        validateIndices(absoluteRow, absoluteColumn)
        return allLines[absoluteRow][absoluteColumn].character
    }

    fun getAttributesAt(absoluteRow: Int, absoluteColumn: Int): Attributes {
        validateIndices(absoluteRow, absoluteColumn)

        return allLines[absoluteRow][absoluteColumn].attributes
    }
}