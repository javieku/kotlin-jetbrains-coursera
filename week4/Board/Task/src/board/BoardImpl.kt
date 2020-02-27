package board

import board.Direction.*

class BoardImpl: SquareBoard {

    override val width: Int
    val board: Array<Array<Cell>>


    constructor(width: Int) {
        this.width = width
        board = Array(width) { i -> Array(width) {j -> Cell(i +1, j + 1) } }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (!(i in 1 .. width )) return null
        if (!(j in 1 .. width )) return null
        return board[i -1][j -1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        return board[i-1][j-1]
    }

    override fun getAllCells(): Collection<Cell> {
        return board.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        var l: List<Cell> = mutableListOf()
        for (j in jRange) {
            if (j-1 < width)
                l += board[i-1][j-1]
        }
        return l
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        var l: List<Cell> = mutableListOf()
        for (i in iRange) {
            if (i-1 < width)
                l += board[i-1][j-1]
        }
        return l
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        when (direction) {
            UP -> if (i-2 >= 0) return board[this.i-1-1][this.j-1] else return null
            DOWN -> if (i < width) return board[this.i-1+1][this.j-1] else return null
            RIGHT -> if (j < width) return board[this.i-1][this.j-1+1] else return null
            LEFT -> if (j-2 >= 0) return board[this.i-1][this.j-1-1] else return null
        }
    }
}

class GameBoardImpl<T>(board: SquareBoard) : SquareBoard by board, GameBoard<T> {

    override val width: Int = board.width

    var map : MutableMap<Cell,T?> = this.getAllCells().associate{cell -> cell to null}.toMutableMap()

    override operator fun get(cell: Cell): T? {
        return map[cell]
    }
    override operator fun set(cell: Cell, value: T?) {
        map[cell] = value
    }
    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return map.filter { (_, value) ->  predicate(value) }.keys;
    }
    override fun find(predicate: (T?) -> Boolean): Cell? {
        return map.filter{(_, value) -> predicate(value)}.keys.first()
    }
    override fun any(predicate: (T?) -> Boolean): Boolean {
        return map.any{(_, value) -> predicate(value)}

    }
    override fun all(predicate: (T?) -> Boolean): Boolean {
        return map.all{(_, value) -> predicate(value)}
    }
}

fun createSquareBoard(width: Int): SquareBoard = BoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl<T>(BoardImpl(width))

