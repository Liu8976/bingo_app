package com.bingo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bingo.app.data.BingoViewModel
import com.bingo.app.logic.CharacterBattleStateCalculator
import com.bingo.app.ui.BingoBottomBar
import com.bingo.app.ui.BingoTab
import com.bingo.app.ui.screen.CommunityScreen
import com.bingo.app.ui.screen.HomeScreen
import com.bingo.app.ui.screen.ProfileScreen
import com.bingo.app.ui.screen.RecordsScreen
import com.bingo.app.ui.screen.TrainingScreen
import com.bingo.app.ui.theme.AppColors
import com.bingo.app.ui.theme.BingoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BingoTheme {
                BingoApp()
            }
        }
    }
}

@Composable
fun BingoApp() {
    val bingoViewModel: BingoViewModel = viewModel()
    val summary by bingoViewModel.todaySummary.collectAsStateWithLifecycle()
    val records by bingoViewModel.bodyRecords.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableStateOf(BingoTab.Today) }
    val battleState = CharacterBattleStateCalculator.calculate(summary)

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = {
            BingoBottomBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = AppColors.Background
        ) {
            when (selectedTab) {
                BingoTab.Today -> HomeScreen(
                    summary = summary,
                    battleState = battleState,
                    records = records,
                    debugMinutes = summary.exerciseMinutes,
                    onDebugMinutesChanged = bingoViewModel::setDebugMinutes,
                    onStartTraining = { selectedTab = BingoTab.Training },
                    onOpenRecords = { selectedTab = BingoTab.Records },
                    onAddWater = bingoViewModel::addWaterCup
                )
                BingoTab.Training -> TrainingScreen(
                    onTrainingCompleted = { training ->
                        bingoViewModel.completeTraining(training)
                        selectedTab = BingoTab.Today
                    }
                )
                BingoTab.Records -> RecordsScreen(
                    records = records,
                    onLogWeight = bingoViewModel::logWeight,
                    onLogFoodIntake = bingoViewModel::logFoodIntake
                )
                BingoTab.Community -> CommunityScreen()
                BingoTab.Profile -> ProfileScreen()
            }
        }
    }
}
