package org.example

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.io.IOException
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

// Data classes for the game
data class Game(
    val id: String = UUID.randomUUID().toString(),
    val board: MutableList<String?> = MutableList(9) { null },
    var currentPlayer: String = "X",
    var winner: String? = null,
    var isGameOver: Boolean = false
)

data class MoveRequest(val position: Int, val player: String)
data class GameResponse(val id: String, val board: List<String?>, val currentPlayer: String, val winner: String?, val isGameOver: Boolean)

// Game repository
object GameRepository {
    private val games = ConcurrentHashMap<String, Game>()

    fun createGame(): Game {
        val game = Game()
        games[game.id] = game
        return game
    }

    fun getGame(id: String): Game? = games[id]

    fun makeMove(gameId: String, position: Int, player: String): Game? {
        val game = games[gameId] ?: return null

        // Check if the move is valid
        if (game.isGameOver || game.board[position] != null || game.currentPlayer != player) {
            return game
        }

        // Make the move
        game.board[position] = player

        // Check for a winner
        val winner = calculateWinner(game.board)
        if (winner != null) {
            game.winner = winner
            game.isGameOver = true
        } else if (game.board.all { it != null }) {
            // Check for a draw
            game.isGameOver = true
        } else {
            // Switch player
            game.currentPlayer = if (player == "X") "O" else "X"
        }

        return game
    }

    private fun calculateWinner(board: List<String?>): String? {
        val lines = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )

        for (line in lines) {
            val (a, b, c) = line
            if (board[a] != null && board[a] == board[b] && board[a] == board[c]) {
                return board[a]
            }
        }

        return null
    }
}

// HTTP handlers
class NewGameHandler : HttpHandler {
    @Throws(IOException::class)
    override fun handle(exchange: HttpExchange) {
        // Set CORS headers
        exchange.responseHeaders.add("Access-Control-Allow-Origin", "*")
        exchange.responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        exchange.responseHeaders.add("Access-Control-Allow-Headers", "Content-Type,Authorization")

        if (exchange.requestMethod == "OPTIONS") {
            exchange.sendResponseHeaders(204, -1)
            return
        }

        if (exchange.requestMethod != "POST") {
            exchange.sendResponseHeaders(405, -1)
            return
        }

        val game = GameRepository.createGame()
        val response = """{"id":"${game.id}","board":[${game.board.map { it?.let { "\"$it\"" } ?: "null" }.joinToString(",")}],"currentPlayer":"${game.currentPlayer}","winner":${game.winner?.let { "\"$it\"" } ?: "null"},"isGameOver":${game.isGameOver}}"""

        exchange.responseHeaders.add("Content-Type", "application/json")
        exchange.sendResponseHeaders(200, response.length.toLong())

        val os = exchange.responseBody
        os.write(response.toByteArray())
        os.close()
    }
}

class MakeMoveHandler : HttpHandler {
    @Throws(IOException::class)
    override fun handle(exchange: HttpExchange) {
        // Set CORS headers
        exchange.responseHeaders.add("Access-Control-Allow-Origin", "*")
        exchange.responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        exchange.responseHeaders.add("Access-Control-Allow-Headers", "Content-Type,Authorization")

        if (exchange.requestMethod == "OPTIONS") {
            exchange.sendResponseHeaders(204, -1)
            return
        }

        if (exchange.requestMethod != "POST") {
            exchange.sendResponseHeaders(405, -1)
            return
        }

        val path = exchange.requestURI.path
        val gameId = path.substringAfterLast("/game/").substringBefore("/move")

        val requestBody = exchange.requestBody.bufferedReader().use { it.readText() }
        val position = requestBody.substringAfter("\"position\":").substringBefore(",").trim().toInt()
        val player = requestBody.substringAfter("\"player\":\"").substringBefore("\"").trim()

        val game = GameRepository.makeMove(gameId, position, player)

        if (game == null) {
            exchange.sendResponseHeaders(404, -1)
            return
        }

        val response = """{"id":"${game.id}","board":[${game.board.map { it?.let { "\"$it\"" } ?: "null" }.joinToString(",")}],"currentPlayer":"${game.currentPlayer}","winner":${game.winner?.let { "\"$it\"" } ?: "null"},"isGameOver":${game.isGameOver}}"""

        exchange.responseHeaders.add("Content-Type", "application/json")
        exchange.sendResponseHeaders(200, response.length.toLong())

        val os = exchange.responseBody
        os.write(response.toByteArray())
        os.close()
    }
}

fun main() {
    val server = HttpServer.create(InetSocketAddress(8080), 0)
    server.executor = Executors.newCachedThreadPool()

    // Register handlers
    server.createContext("/api/game/new", NewGameHandler())
    server.createContext("/api/game/", MakeMoveHandler())

    // Start the server
    server.start()
    println("Server started on port 8080")
}
