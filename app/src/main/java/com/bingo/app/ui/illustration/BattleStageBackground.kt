package com.bingo.app.ui.illustration

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.bingo.app.ui.theme.AppColors

@Composable
internal fun BattleStageBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        drawOval(AppColors.LightOrange.copy(alpha = 0.7f), topLeft = Offset(-w * 0.15f, h * 0.13f), size = Size(w * 1.3f, h * 0.58f))
        repeat(18) { i ->
            val x = (i * 53 % w.toInt()).toFloat()
            val y = h * (0.12f + (i % 7) * 0.09f)
            drawCircle(listOf(AppColors.EnergyYellow, AppColors.GrowthGreen, AppColors.PrimaryOrange)[i % 3].copy(alpha = 0.5f), radius = 4f, center = Offset(x, y))
        }
        drawLine(AppColors.BorderWarm.copy(alpha = 0.5f), Offset(0f, h * 0.68f), Offset(w, h * 0.68f), strokeWidth = 2f)
    }
}
