package com.example.mathapp.ui

import android.content.res.Resources
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mathapp.R
import com.example.mathapp.ui.theme.MathAppTheme

@Composable
fun Instructions(modifier: Modifier = Modifier) {

    Text(
        text = stringResource(R.string.game_instructions),
        modifier = modifier.padding(8.dp)
    )
}


