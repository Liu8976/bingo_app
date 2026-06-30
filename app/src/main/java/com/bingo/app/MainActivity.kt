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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bingo.app.logic.CharacterBattleStateCalculator
import com.bingo.app.mock.MockBingoData
import com.bingo.app.ui.BingoBottomBar
import com.bingo.app.ui.BingoTab
import com.bingo.app.ui.CommunityScreen
import com.bingo.app.ui.HomeScreen
import com.bingo.app.ui.ProfileScreen
import com.bingo.app.ui.RecordsScreen
import com.bingo.app.ui.TrainingScreen
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
    var selectedTab by remember { mutableStateOf(BingoTab.Today) }
    var debugMinutes by remember { mutableIntStateOf(25) }
    val summary = MockBingoData.todaySummary(debugMinutes)
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
                    debugMinutes = debugMinutes,
                    onDebugMinutesChanged = { debugMinutes = it }
                )
                BingoTab.Training -> TrainingScreen()
                BingoTab.Records -> RecordsScreen()
                BingoTab.Community -> CommunityScreen()
                BingoTab.Profile -> ProfileScreen()
            }
        }
    }
}
