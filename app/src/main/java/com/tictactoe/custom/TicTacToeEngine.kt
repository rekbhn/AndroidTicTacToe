package com.tictactoe.custom

private val WIN_LINES: Array<IntArray> = arrayOf(
    intArrayOf(0, 1, 2),
    intArrayOf(3, 4, 5),
    intArrayOf(6, 7, 8),
    intArrayOf(0, 3, 6),
    intArrayOf(1, 4, 7),
    intArrayOf(2, 5, 8),
    intArrayOf(0, 4, 8),
    intArrayOf(2, 4, 6),
)

fun winnerIndex(board: Array<Int?>): Int? {
    for (line in WIN_LINES) {
        val a = board[line[0]] ?: continue
        if (a == board[line[1]] && a == board[line[2]]) return a
    }
    return null
}

/** The three cell indices that form the winning line, or null if no winner yet. */
fun winningLineIndices(board: Array<Int?>): IntArray? {
    for (line in WIN_LINES) {
        val a = board[line[0]] ?: continue
        if (a == board[line[1]] && a == board[line[2]]) return line
    }
    return null
}

fun isBoardFull(board: Array<Int?>): Boolean = board.all { it != null }

fun emptyIndices(board: Array<Int?>): List<Int> =
    board.mapIndexedNotNull { i, v -> if (v == null) i else null }

sealed class GameOutcome {
    data object Playing : GameOutcome()
    data class Win(val player: Int) : GameOutcome()
    data object Draw : GameOutcome()
}

fun outcome(board: Array<Int?>): GameOutcome {
    val w = winnerIndex(board)
    if (w != null) return GameOutcome.Win(w)
    if (isBoardFull(board)) return GameOutcome.Draw
    return GameOutcome.Playing
}

/**
 * Unbeatable move for [aiPlayer] using minimax (prefers shorter wins / longer losses).
 */
fun bestAiMove(board: Array<Int?>, aiPlayer: Int, humanPlayer: Int): Int? {
    val empties = emptyIndices(board)
    if (empties.isEmpty()) return null

    var bestScore = Int.MIN_VALUE
    var bestMove: Int? = null
    val depthStart = empties.size

    for (i in empties) {
        board[i] = aiPlayer
        val score = minimax(
            board = board,
            depth = depthStart - 1,
            isAiTurn = false,
            aiPlayer = aiPlayer,
            humanPlayer = humanPlayer,
        )
        board[i] = null
        if (score > bestScore) {
            bestScore = score
            bestMove = i
        }
    }
    return bestMove
}

private fun minimax(
    board: Array<Int?>,
    depth: Int,
    isAiTurn: Boolean,
    aiPlayer: Int,
    humanPlayer: Int,
): Int {
    when (val o = outcome(board)) {
        is GameOutcome.Win -> {
            return if (o.player == aiPlayer) 10 - depth else depth - 10
        }
        GameOutcome.Draw -> return 0
        GameOutcome.Playing -> Unit
    }

    if (isAiTurn) {
        var best = Int.MIN_VALUE
        for (i in emptyIndices(board)) {
            board[i] = aiPlayer
            best = maxOf(
                best,
                minimax(board, depth - 1, false, aiPlayer, humanPlayer),
            )
            board[i] = null
        }
        return best
    } else {
        var best = Int.MAX_VALUE
        for (i in emptyIndices(board)) {
            board[i] = humanPlayer
            best = minOf(
                best,
                minimax(board, depth - 1, true, aiPlayer, humanPlayer),
            )
            board[i] = null
        }
        return best
    }
}
