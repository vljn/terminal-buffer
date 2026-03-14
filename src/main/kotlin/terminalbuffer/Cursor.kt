package io.github.vljn.terminalbuffer

class Cursor(val maxWidth: Int, val maxHeight: Int) {
    init {
        require(maxWidth > 0) { "maxWidth must be positive." }
        require(maxHeight > 0) { "maxHeight must be positive." }
    }

    var column = 0
        set(value) {
            field = value.coerceIn(0, maxWidth - 1)
        }

    var row = 0
        set(value) {
            field = value.coerceIn(0, maxHeight - 1)
        }


    fun moveColumnBy(move: Int) {
        column += move
    }

    fun moveRowBy(move: Int) {
        row += move
    }

    fun nextColumn() = moveColumnBy(1)
    fun nextRow() = moveRowBy(1)

    fun moveTo(column: Int, row: Int) {
        this.column = column
        this.row = row
    }
}