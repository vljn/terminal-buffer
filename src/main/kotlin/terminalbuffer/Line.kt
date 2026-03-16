package io.github.vljn.terminalbuffer

class Line(width: Int) {
    val cells: Array<Cell> = Array(width) { Cell() }

    fun clear() {
        for (i in cells.indices) {
            cells[i] = Cell()
        }
    }

    operator fun get(index: Int): Cell {
        return cells[index]
    }

    operator fun set(index: Int, cell: Cell) {
        cells[index] = cell
    }

    override fun toString(): String {
        return cells.joinToString("")
    }
}