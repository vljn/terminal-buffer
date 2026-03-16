package io.github.vljn.terminalbuffer

import java.util.EnumSet

enum class Color {
    DEFAULT,
    BLACK,
    RED,
    GREEN,
    YELLOW,
    BLUE,
    MAGENTA,
    CYAN,
    WHITE,
    BRIGHT_BLACK,
    BRIGHT_RED,
    BRIGHT_GREEN,
    BRIGHT_YELLOW,
    BRIGHT_BLUE,
    BRIGHT_MAGENTA,
    BRIGHT_CYAN,
    BRIGHT_WHITE,
}

enum class Style {
    BOLD,
    ITALIC,
    UNDERLINE,
}

data class Attributes(var foregroundColor: Color = Color.DEFAULT,
                      var backgroundColor: Color = Color.DEFAULT,
                      var style: EnumSet<Style> = EnumSet.noneOf(Style::class.java)) {}

data class Cell (
    var character: Char = ' ',
    var attributes: Attributes = Attributes()
) {
    override fun toString(): String {
        return character.toString()
    }
}