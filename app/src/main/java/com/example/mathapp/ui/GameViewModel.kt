package com.example.mathapp.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathapp.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()


    private lateinit var num1: String
    private lateinit var num2: String
    private var timerJob: Job? = null


    private val treeGrowthStages = listOf(
        R.drawable.stage1,
        R.drawable.stage2,
        R.drawable.stage3,
        R.drawable.stage4,
        R.drawable.stage5,
    )

    var userGuess by mutableStateOf("")
        private set
    var message by mutableStateOf("")
    private fun randomOperationAndNumbers(): Pair<String, Pair<String, String>> {
        val operation = randomOperation()
        val numbers = randomNumbers(operation)
        return Pair(operation, numbers)
    }

    private fun randomNumbers(operation: String): Pair<String, String> {
        if (operation == "-") {
            num1 = (1..9).random().toString()
            num2 = (0..num1.toInt()).random().toString()
        } else {
            num1 = (1..9).random().toString()
            num2 = (1..9).random().toString()
        }
        Log.d("RandomNumbers", "Generated num1: $num1, num2: $num2 for operation: $operation")
        return Pair(num1, num2)
    }


    private fun randomOperation(): String {
        return if((0..1).random() == 0) "+" else "-"
    }

    fun updateUserGuess(guessedResult: String) {
        userGuess = guessedResult
    }

    private var currentStageIndex = 0

    private fun increaseStageIndex() {
        currentStageIndex = (currentStageIndex + 1).coerceAtMost(treeGrowthStages.size - 1)
        _uiState.update { currentState ->
            currentState.copy(imageResId = treeGrowthStages[currentStageIndex])
        }
    }

    private fun decreaseStageIndex() {
        currentStageIndex = (currentStageIndex - 1).coerceAtLeast(0)
        _uiState.update { currentState ->
            currentState.copy(imageResId = treeGrowthStages[currentStageIndex])
        }
    }

    fun checkUserGuess() {
        val expectedSum = if (_uiState.value.operation == "+")
                (num1.toIntOrNull() ?: 0) + (num2.toIntOrNull() ?: 0)
        else
            (num1.toIntOrNull() ?: 0) - (num2.toIntOrNull() ?: 0)

        val userGuessInt = userGuess.toIntOrNull() ?: 0


        if(userGuessInt == expectedSum){
            val updatedScore = _uiState.value.score.plus(20)
            val newSolvedProblemCount = _uiState.value.solvedProblemCount.inc()
            message = "Correct!"

            updateGameState(score = updatedScore, solvedProblemCount = newSolvedProblemCount, newProblem = true)


            if(_uiState.value.solvedProblemCount % 3 == 0)
                increaseStageIndex()

            resetTimer()
        }
        else {
            val newLives = _uiState.value.lives.dec()
            val updatedScore = if(_uiState.value.score - 30 < 0) 0 else _uiState.value.score - 30
            val newSolvedProblemCount= _uiState.value.solvedProblemCount.minus(3)
            message ="Incorrect!"

            updateGameState(isGuessedWrong = true, lives = newLives, score = updatedScore, solvedProblemCount = newSolvedProblemCount, newProblem = false)

            decreaseStageIndex()
        }
        updateUserGuess("")
        viewModelScope.launch {
            delay(500)
            message = ""
        }
    }

    private fun updateGameState(
        isGuessedWrong: Boolean = false,
        lives: Int = _uiState.value.lives,
        solvedProblemCount: Int = _uiState.value.solvedProblemCount,
        score: Int,
        newProblem: Boolean,
    ){
        if(newProblem) {
            val generatedOperationAndNumbers = randomOperationAndNumbers()

            _uiState.update{ currentState ->
                currentState.copy(
                    operation = generatedOperationAndNumbers.first,
                    isGuessedResultWrong = false,
                    num1 = generatedOperationAndNumbers.second.first,
                    num2 = generatedOperationAndNumbers.second.second,
                    lives = lives,
                    score = score,
                    currentProblemCount = currentState.currentProblemCount.inc(),
                    solvedProblemCount = solvedProblemCount,
                )

            }

        }
        else{
            _uiState.update { currentState ->

                currentState.copy(
                    isGuessedResultWrong = isGuessedWrong,
                    lives = lives,
                    solvedProblemCount = solvedProblemCount,
                    score = score,
                )
            }

        }
        if(_uiState.value.lives == 0 || _uiState.value.solvedProblemCount == 12){
            viewModelScope.launch {
                delay(2000)
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = true
                )
            }
            }
            stopTimer()
        }

    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeft > 0) {
                delay(1000L)
                _uiState.update { currentState ->
                    currentState.copy(timeLeft = currentState.timeLeft - 1)
                }
            }
            if (_uiState.value.timeLeft == 0) {
                onTimeOut()
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    private fun resetTimer() {
        timerJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(timeLeft = 10)
        }
        if(!_uiState.value.isGameOver )
            startTimer()
    }

    private fun onTimeOut() {
        val newLives = _uiState.value.lives.dec()
        val newScore = if(_uiState.value.score - 30 < 0) 0 else _uiState.value.score - 30
        updateGameState(lives = newLives, isGuessedWrong = true,score = newScore, newProblem = true)
        if (_uiState.value.lives > 0) {
            resetTimer()
        }
    }


    fun resetGame() {
        val generatedOperationAndNumbers = randomOperationAndNumbers()

        _uiState.value = GameUiState(
            operation = generatedOperationAndNumbers.first,
            num1 = generatedOperationAndNumbers.second.first,
            num2 = generatedOperationAndNumbers.second.second,
        )
        updateUserGuess("")
        currentStageIndex = 0
        resetTimer()
    }

    init {
        resetGame()
    }
}
