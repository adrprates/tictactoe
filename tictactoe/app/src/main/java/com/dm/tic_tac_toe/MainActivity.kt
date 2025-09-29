package com.dm.tic_tac_toe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var board = IntArray(9) { 0 }

    private var isPlayerOneTurn = true
    private var isGameActive = true

    private var scoreX = 0
    private var scoreO = 0

    private lateinit var textStatus: TextView
    private lateinit var textScoreX: TextView
    private lateinit var textScoreO: TextView
    private lateinit var btnRestart: Button
    private lateinit var btnResetScore: Button
    private lateinit var allButtons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textStatus = findViewById(R.id.text_status)
        textScoreX = findViewById(R.id.text_score_x)
        textScoreO = findViewById(R.id.text_score_o)
        btnRestart = findViewById(R.id.btn_restart)
        btnResetScore = findViewById(R.id.btn_reset_score)

        allButtons = listOf(
            findViewById(R.id.btn_0), findViewById(R.id.btn_1), findViewById(R.id.btn_2),
            findViewById(R.id.btn_3), findViewById(R.id.btn_4), findViewById(R.id.btn_5),
            findViewById(R.id.btn_6), findViewById(R.id.btn_7), findViewById(R.id.btn_8)
        )

        setupBoardListeners()
        setupControlButtons()

        updateStatus("Vez do Jogador X", Color.BLUE)
    }


    private fun setupBoardListeners() {
        allButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                onCellClick(button, index)
            }
        }
    }

    private fun setupControlButtons() {
        btnRestart.setOnClickListener { resetGame() }
        btnResetScore.setOnClickListener { resetScore() }
    }


    private fun onCellClick(button: Button, cellId: Int) {
        if (!isGameActive || board[cellId] != 0) return

        val symbol: String
        val color: Int
        if (isPlayerOneTurn) {
            symbol = "X"
            board[cellId] = 1
            color = Color.BLUE
            updateStatus("Vez do Jogador O", Color.RED)
        } else {
            symbol = "O"
            board[cellId] = 2
            color = Color.RED
            updateStatus("Vez do Jogador X", Color.BLUE)
        }

        // Atualiza célula
        button.text = symbol
        button.setTextColor(color)
        button.isEnabled = false

        // Verifica fim de jogo
        if (checkForWinner()) {
            isGameActive = false
            updateStatus("Jogador $symbol venceu!", Color.GREEN)
            updateScore(symbol)
            disableAllButtons()
        } else if (isBoardFull()) {
            isGameActive = false
            updateStatus("Empate!", Color.DKGRAY)
            disableAllButtons()
        } else {
            // Troca o turno
            isPlayerOneTurn = !isPlayerOneTurn
        }
    }

    private fun checkForWinner(): Boolean {
        val winPositions = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
        )

        for (pos in winPositions) {
            val a = board[pos[0]]
            val b = board[pos[1]]
            val c = board[pos[2]]

            if (a != 0 && a == b && b == c) {
                highlightWinningCells(pos)
                return true
            }
        }
        return false
    }

    private fun isBoardFull(): Boolean {
        return !board.contains(0)
    }

    private fun updateStatus(message: String, color: Int) {
        textStatus.text = message
        textStatus.setTextColor(color)
    }

    private fun highlightWinningCells(indices: IntArray) {
        val winColor = Color.parseColor("#4CAF50")
        indices.forEach { allButtons[it].setBackgroundColor(winColor) }
    }

    private fun disableAllButtons() {
        allButtons.forEach { it.isEnabled = false }
    }

    private fun updateScore(symbol: String) {
        if (symbol == "X") {
            scoreX++
            textScoreX.text = "Jogador X: $scoreX"
        } else {
            scoreO++
            textScoreO.text = "Jogador O: $scoreO"
        }
    }

    private fun resetGame() {
        board.fill(0)
        isPlayerOneTurn = true
        isGameActive = true
        updateStatus("Vez do Jogador X", Color.BLUE)

        val defaultBgColor = Color.parseColor("#F0F0F0")
        allButtons.forEach { button ->
            button.text = ""
            button.isEnabled = true
            button.setBackgroundColor(defaultBgColor)
        }
    }

    private fun resetScore() {
        scoreX = 0
        scoreO = 0
        textScoreX.text = "Jogador X: 0"
        textScoreO.text = "Jogador O: 0"
    }
}
