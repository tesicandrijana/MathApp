package com.example.mathapp

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mathapp.ui.GameScreen
import com.example.mathapp.ui.GameViewModel
import com.example.mathapp.ui.Instructions
import com.example.mathapp.ui.StartGameScreen

enum class LearnMathScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Game(title = R.string.play_game),
    Instructions(title = R.string.instructions),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnMathAppBar(
    currentScreen: LearnMathScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text(stringResource(currentScreen.title))},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun LearnMathApp(
    windowSize: WindowHeightSizeClass,
    viewModel: GameViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
    ){
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = LearnMathScreen.valueOf(
        backStackEntry?.destination?.route ?: LearnMathScreen.Start.name
    )

    Scaffold(
        topBar = {LearnMathAppBar(
            currentScreen = currentScreen,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() }
        )
        }
    ){ innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = LearnMathScreen.Start.name,
            modifier = Modifier.padding(innerPadding),
        ){
            composable(route = LearnMathScreen.Start.name){

                StartGameScreen(
                    windowSize = windowSize,
                    onStartButtonClicked = {
                        navController.navigate(LearnMathScreen.Game.name)
                    },
                    onInstructionsButtonClicked = {
                        navController.navigate(LearnMathScreen.Instructions.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = LearnMathScreen.Game.name){
                val context = LocalContext.current
                GameScreen(
                    windowSize = windowSize,
                    onSendButtonAction = { subject: String, summary: String ->
                        shareOrder(context, subject = subject, summary = summary)
                    }
                ) {
                    navController.popBackStack(
                        LearnMathScreen.Start.name,
                        false
                    )
                } // Navigate to start screen

            }
            composable(route = LearnMathScreen.Instructions.name){
                Instructions()
            }

        }


    }

}

fun shareOrder(context: Context, subject: String, summary: String){
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.share_score)
        )
    )
}

