package com.bingo.app.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingo.app.ui.BingoCard
import com.bingo.app.ui.CircleIcon
import com.bingo.app.ui.MetricPill
import com.bingo.app.ui.ScreenList
import com.bingo.app.ui.SectionTitle
import com.bingo.app.ui.theme.AppColors

@Composable
fun RecordsScreen() {
    ScreenList {
        item {
            PageHeader(
                title = "身体档案",
                subtitle = "别只盯一天，看趋势才靠谱。",
                trailing = { CircleIcon("⇧", AppColors.CardWhite) }
            )
        }
        item { WeightTrendCard() }
        item { FoodRecordCard() }
        item { ExerciseRecordCard() }
        item {
            ReminderCard(
                title = "趋势提醒：",
                text = "别只盯一天，看趋势才靠谱。肌肉伙伴会帮你记住长期进步。"
            )
        }
    }
}

@Composable
private fun WeightTrendCard() {
    BingoCard {
        SectionTitle("体重趋势")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MetricPill("当前体重", "72.0 kg", AppColors.TextNavy, Modifier.weight(1f))
            MetricPill("近 7 天", "-0.6 kg", AppColors.GrowthGreen, Modifier.weight(1f))
        }
        WeightChart()
    }
}

@Composable
private fun FoodRecordCard() {
    BingoCard {
        SectionTitle("饮食记录")
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column {
                Text("今日总摄入 1,420 kcal", color = AppColors.TextNavy, fontWeight = FontWeight.Bold)
                Text("还可以吃 280 kcal", color = AppColors.TextSecondary, fontSize = 13.sp)
            }
            FoodBowlIllustration(Modifier.size(82.dp))
        }
    }
}

@Composable
private fun ExerciseRecordCard() {
    BingoCard {
        SectionTitle("运动记录")
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column {
                Text("今日运动 40 分钟", color = AppColors.TextNavy, fontWeight = FontWeight.Bold)
                Text("消耗 280 kcal", color = AppColors.TextSecondary, fontSize = 13.sp)
            }
            CircleIcon("↗", AppColors.LightGreen, AppColors.GrowthGreen, 72.dp)
        }
    }
}

@Composable
private fun WeightChart() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.Background)
            .padding(10.dp)
    ) {
        val values = listOf(0.82f, 0.68f, 0.58f, 0.52f, 0.43f, 0.34f, 0.45f)
        val left = size.width * 0.08f
        val top = size.height * 0.12f
        val chartW = size.width * 0.84f
        val chartH = size.height * 0.7f
        repeat(4) {
            val y = top + chartH * it / 3
            drawLine(AppColors.BorderWarm.copy(alpha = 0.7f), Offset(left, y), Offset(left + chartW, y), 1.5f)
        }
        val points = values.mapIndexed { index, v -> Offset(left + chartW * index / (values.size - 1), top + chartH * v) }
        for (i in 0 until points.lastIndex) {
            drawLine(AppColors.GrowthGreen, points[i], points[i + 1], strokeWidth = 5f)
        }
        points.forEach { point ->
            drawCircle(Color.White, radius = 8f, center = point)
            drawCircle(AppColors.GrowthGreen, radius = 5f, center = point)
        }
    }
}

@Composable
private fun FoodBowlIllustration(modifier: Modifier) {
    Canvas(modifier = modifier) {
        drawOval(AppColors.LightGreen, topLeft = Offset(size.width * 0.12f, size.height * 0.28f), size = Size(size.width * 0.76f, size.height * 0.44f))
        drawArc(AppColors.PrimaryOrange, 0f, 180f, true, topLeft = Offset(size.width * 0.16f, size.height * 0.36f), size = Size(size.width * 0.68f, size.height * 0.46f))
        drawCircle(AppColors.GrowthGreen, radius = size.width * 0.09f, center = Offset(size.width * 0.36f, size.height * 0.36f))
        drawCircle(AppColors.EnergyYellow, radius = size.width * 0.08f, center = Offset(size.width * 0.55f, size.height * 0.32f))
        drawCircle(Color(0xFFFF6B6B), radius = size.width * 0.07f, center = Offset(size.width * 0.64f, size.height * 0.44f))
    }
}
