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
}