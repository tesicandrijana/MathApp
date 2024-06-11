package com.example.mathapp.ui

import androidx.annotation.DrawableRes
import com.example.mathapp.R

data class GameUiState(
    val num1: String = "",
    val num2: String = "",
    val operation: String = "+",
    val isGuessedResultWrong: Boolean = false,
    val score: Int = 0,
    val currentProblemCount: Int = 0,
    val solvedProblemCount: Int = 0,
    val lives: Int = 5,
    val timeLeft: Int = 10,
    val isGameOver: Boolean = false,
    @DrawableRes val imageResId: Int = R.drawable.stage1,
)
