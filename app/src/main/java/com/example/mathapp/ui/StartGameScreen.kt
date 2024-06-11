package com.example.mathapp.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mathapp.R

@Composable
fun StartGameScreen(
    windowSize: WindowHeightSizeClass,
    onStartButtonClicked: () -> Unit,
    onInstructionsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (windowSize) {
        WindowHeightSizeClass.Compact -> {
            Row(modifier = modifier.fillMaxWidth()) {
                Box( modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .fillMaxHeight())
                {
                    StartScreenContent() }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .fillMaxHeight()
                    ) {
                    Column {
                        StartScreenButton(
                            labelResourceId = R.string.start_game,
                            onClick = onStartButtonClicked,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        StartScreenButton(
                            labelResourceId = R.string.instructions,
                            onClick = onInstructionsButtonClicked,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
        else -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.SpaceBetween
            ){
                StartScreenContent()
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(id = R.dimen.padding_medium)
                    )
                ) {
                    StartScreenButton(
                        labelResourceId = R.string.start_game,
                        onClick = onStartButtonClicked,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    StartScreenButton(
                        labelResourceId = R.string.instructions,
                        onClick = onInstructionsButtonClicked,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun StartScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
    Image(
        painter = painterResource(R.drawable.math_game_image),
        contentDescription = null,
        modifier = Modifier.width(300.dp)
    )
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
    Text(
        text = stringResource(R.string.welcome_to_math_game),
        style = MaterialTheme.typography.headlineSmall
    )
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
}

}



@Composable
fun StartScreenButton(
    @StringRes labelResourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = 250.dp)
    ) {
        Text(stringResource(labelResourceId))
    }
}

