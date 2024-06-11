package com.example.mathapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mathapp.R

@Composable
fun GameScreen(
    windowSize: WindowHeightSizeClass,
    gameViewModel: GameViewModel = viewModel(),
    onSendButtonAction: (String, String) -> Unit,
    onNavigateToStart: () -> Unit,
    ) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    when (windowSize) {
        WindowHeightSizeClass.Compact -> {
            Column(modifier = Modifier.padding(mediumPadding),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally){
                GameStatus(
                    score = gameUiState.score,
                    lives = gameUiState.lives,
                    timeLeft = gameUiState.timeLeft,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row(){
                    Box( modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .fillMaxHeight()){
                        GameLayout(
                            onUserGuessChanged = { gameViewModel.updateUserGuess(it) },
                            onKeyboardDone = { gameViewModel.checkUserGuess() },
                            userGuess = gameViewModel.userGuess,
                            operation = gameUiState.operation,
                            isGuessedWrong = gameUiState.isGuessedResultWrong,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(mediumPadding),
                            num1 = gameUiState.num1,
                            num2 = gameUiState.num2,
                            message = gameViewModel.message,
                        )
                    }
                    Box( modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .fillMaxHeight()){
                        Button(
                            onClick = { gameViewModel.checkUserGuess() },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(0.5f)
                        ) {
                            Text(stringResource(R.string.submit))
                        }
                    }
                    Box( modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()){
                        Image(
                            painter = painterResource(gameUiState.imageResId),
                            contentDescription = null,
                            modifier = Modifier
                                .height(80.dp)
                                .align(Alignment.TopCenter)
                        )
                    }

                }
            }
        }
        else -> {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(mediumPadding),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GameStatus(
                    score = gameUiState.score,
                    lives = gameUiState.lives,
                    timeLeft = gameUiState.timeLeft,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameLayout(
                        onUserGuessChanged = { gameViewModel.updateUserGuess(it) },
                        onKeyboardDone = { gameViewModel.checkUserGuess() },
                        userGuess = gameViewModel.userGuess,
                        operation = gameUiState.operation,
                        isGuessedWrong = gameUiState.isGuessedResultWrong,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(mediumPadding),
                        num1 = gameUiState.num1,
                        num2 = gameUiState.num2,
                        message = gameViewModel.message,
                    )
                    Button(
                        onClick = { gameViewModel.checkUserGuess() },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(0.5f)
                    ) {
                        Text(stringResource(R.string.submit))
                    }
                }

                Image(
                    painter = painterResource(gameUiState.imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                )
            }
        }
    }


    if (gameUiState.isGameOver) {
        FinalScoreDialog(
            score = gameUiState.score,
            onPlayAgain = { gameViewModel.resetGame() },
            onSendButtonAction = { subject: String, summary: String ->
                onSendButtonAction(subject, summary)
            },
            onNavigateToStart = { onNavigateToStart() },
        )
    }
}

@Composable
fun GameContent(){

}


@Composable
fun GameLayout(
    onUserGuessChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    operation: String,
    userGuess: String,
    isGuessedWrong: Boolean,
    num1: String,
    num2: String,
    message: String,
    modifier: Modifier = Modifier
) {
    val textColor = if (isGuessedWrong) Color.Red else Color.Green

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.problem, num1, operation, num2),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )
        OutlinedTextField(
            value = userGuess,
            onValueChange = onUserGuessChanged,
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            isError = isGuessedWrong,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number,
            ),
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardDone() }
            ),
            modifier = Modifier
                .width(100.dp)
                .padding(start = 8.dp, bottom = 0.dp)

        )
    }
    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = textColor,
        modifier = Modifier.padding(start = 8.dp, top = 0.dp)
    )
}

@Composable
fun GameStatus(score: Int, lives: Int, timeLeft: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .weight(1.3f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = stringResource(R.string.score, score),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(8.dp)
            )
        }
        Card(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                text = stringResource(R.string.livesLeft, lives),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(8.dp)
            )
        }
        Card(
            modifier = Modifier
                .padding(8.dp)
                .weight(1.3f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Text(
                text = stringResource(R.string.time_left, timeLeft),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun FinalScoreDialog(
    onPlayAgain: () -> Unit,
    onSendButtonAction: (String, String) -> Unit,
    onNavigateToStart: () -> Unit,
    score: Int,
    modifier: Modifier = Modifier
) {
    val subject = stringResource(R.string.share_subject)
    val summary = stringResource(R.string.share_summary, score)

        AlertDialog(
            onDismissRequest = {},
            title = { Text(stringResource(R.string.game_ended)) },
            text = { Text(stringResource(R.string.alert_dialog_score, score)) },
            modifier = modifier,
            confirmButton = {
                Row {
                    TextButton(
                        onClick = { onPlayAgain() }
                    ) {
                        Text(stringResource(R.string.play_again))
                    }
                    TextButton(
                        onClick = { onSendButtonAction(subject, summary) })
                    {
                        Text(stringResource(R.string.share_score))
                    }
                }
            },
            dismissButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = { onNavigateToStart() },
                    ) {
                        Text(text = stringResource(R.string.home))
                    }
                }
            },
        )
    }


