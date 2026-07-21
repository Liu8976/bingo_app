package com.bingo.app.ui.screen

import android.content.pm.ApplicationInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingo.app.R
import com.bingo.app.mock.MockBingoData
import com.bingo.app.model.CharacterBattleState
import com.bingo.app.model.MuscleBuddyState
import com.bingo.app.model.TodayFitnessSummary
import com.bingo.app.ui.BattleImageBubble
import com.bingo.app.ui.BingoCard
import com.bingo.app.ui.BingoLogo
import com.bingo.app.ui.CharacterAvatar
import com.bingo.app.ui.FatMonsterView
import com.bingo.app.ui.HomeGoalText
import com.bingo.app.ui.MuscleBuddyView
import com.bingo.app.ui.PillButton
import com.bingo.app.ui.PrimaryGradientButton
import com.bingo.app.ui.RewardChip
import com.bingo.app.ui.ScreenList
import com.bingo.app.ui.SectionTitle
import com.bingo.app.ui.TaskScrollIndicator
import com.bingo.app.ui.illustration.BattleStageBackground
import com.bingo.app.ui.theme.AppColors

@Composable
fun HomeScreen(
    summary: TodayFitnessSummary,
    battleState: CharacterBattleState,
    debugMinutes: Int,
    onDebugMinutesChanged: (Int) -> Unit
) {
    val isDebuggable = (LocalContext.current.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    ScreenList {
        item {
            PageHeader(
                title = "早上好，今天准备反击了吗？",
                subtitle = "今日目标：运动 30 分钟 · 饮水 2L",
                trailing = { CharacterAvatar(MuscleBuddyState.Powered) }
            )
        }
        item { TodayBattleCard(summary = summary, state = battleState) }
        if (isDebuggable) {
            item { DebugStateSwitcher(selectedMinutes = debugMinutes, onSelected = onDebugMinutesChanged) }
        }
        item { DailyTaskCard(summary = summary) }
        item { HomeSummaryCards(summary = summary) }
        item {
            if (battleState.primaryButtonText == "开始今日反击") {
                HomeStartButton(modifier = Modifier.fillMaxWidth())
            } else {
                PrimaryGradientButton(text = battleState.primaryButtonText, modifier = Modifier.fillMaxWidth())
            }
        }
        item { ReminderCard(text = battleState.reminderText) }
    }
}

@Composable
internal fun PageHeader(
    title: String,
    subtitle: String,
    brandColor: Color = AppColors.PrimaryOrange,
    trailing: @Composable BoxScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            BingoLogo(brandColor)
            Text(title, color = AppColors.TextNavy, fontSize = 21.sp, lineHeight = 27.sp, fontWeight = FontWeight.Black)
            HomeGoalText(subtitle)
        }
        Box(modifier = Modifier.padding(start = 12.dp), contentAlignment = Alignment.TopEnd, content = trailing)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TodayBattleCard(summary: TodayFitnessSummary, state: CharacterBattleState) {
    BingoCard(contentPadding = 6.dp, radius = 24.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White, Color(0xFFFFFBF2), AppColors.LightOrange.copy(alpha = 0.58f))
                    )
                )
        ) {
            BattleStageBackground()
            FatMonsterView(
                state.fatMonsterState,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 40.dp, start = 40.dp)
                    .width(115.dp)
                    .height(115.dp)
            )
            MuscleBuddyView(
                state.muscleBuddyState,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 40.dp)
                    .width(115.dp)
                    .height(115.dp)
            )
            Image(
                painter = painterResource(R.drawable.today_vs),
                contentDescription = "VS",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 50.dp)
                    .width(38.5.dp)
                    .height(81.dp),
                contentScale = ContentScale.FillBounds
            )
            BattleImageBubble(
                text = state.fatMonsterBubbleText,
                bubbleResId = R.drawable.fat_monster_bubble,
                Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 10.dp, top = 10.dp)
                    .width(90.dp),
                horizontalPadding = 6.dp,
                verticalPadding = 6.dp,
                textOffsetY = (-3).dp,
                textAlign = TextAlign.Start
            )
            BattleImageBubble(
                text = state.muscleBuddyBubbleText,
                bubbleResId = R.drawable.muscle_buddy_bubble,
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 10.dp, top = 10.dp)
                    .width(90.dp),
                horizontalPadding = 6.dp,
                verticalPadding = 6.dp,
                textOffsetY = (-3).dp,
                textAlign = TextAlign.Start
            )
            Column(
                modifier = Modifier.padding(start = 5.dp, top = 10.dp, end = 5.dp, bottom = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.today_battle_title),
                    contentDescription = "今日战况",
                    modifier = Modifier
                        .width(127.dp)
                        .height(22.8.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(115.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatMetricCard(
                        iconResId = R.drawable.health,
                        label = "脂肪怪\n血量：",
                        value = "${state.fatMonsterHealthPercent}%",
                        color = AppColors.PrimaryOrange,
                        progress = state.fatMonsterHealthPercent / 100f,
                        tuning = StatMetricCardTuning(
                            valueOffsetX = 2.dp,
                            valueOffsetY = (-3).dp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        iconResId = R.drawable.growth,
                        label = "肌肉伙伴\n成长：",
                        value = "+${state.muscleGrowthValue}",
                        color = AppColors.GrowthGreen,
                        progress = (state.muscleGrowthValue / 25f).coerceIn(0f, 1f),
                        tuning = StatMetricCardTuning(
                            valueOffsetX = 2.dp,
                            valueOffsetY = (-3).dp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        iconResId = R.drawable.sport,
                        label = "今日运动：",
                        value = "${summary.exerciseMinutes} min",
                        color = AppColors.EnergyYellow,
                        progress = state.progressPercent,
                        tuning = StatMetricCardTuning
                        (
                            progressOffsetX = 3.dp,
                            progressOffsetY = 3.dp,
                            valueOffsetX = 3.dp,
                            valueOffsetY = 3.dp,
                            iconOffsetX = 3.dp,
                            iconOffsetY = 3.dp,
                            labelOffsetX = 3.dp,
                            labelOffsetY= 3.dp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        iconResId = R.drawable.burn,
                        label = "今日消耗：",
                        value = "${summary.caloriesBurned} kcal",
                        color = AppColors.PrimaryOrange,
                        progress = (summary.caloriesBurned / 500f).coerceIn(0f, 1f),
                        tuning = StatMetricCardTuning
                        (
                            progressOffsetX = 3.dp,
                            progressOffsetY = 3.dp,
                            valueOffsetX = 3.dp,
                            valueOffsetY = 3.dp,
                            iconOffsetX = 3.dp,
                            iconOffsetY = 3.dp,
                            labelOffsetX = 3.dp,
                            labelOffsetY= 3.dp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(10.dp))
                BattleReportButton(text = state.battleTitle)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DebugStateSwitcher(selectedMinutes: Int, onSelected: (Int) -> Unit) {
    BingoCard(contentPadding = 12.dp, radius = 20.dp) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Debug 状态", color = AppColors.TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                MockBingoData.debugMinutes.forEach { minutes ->
                    val selected = selectedMinutes == minutes
                    PillButton(
                        text = "${minutes}分",
                        selected = selected,
                        onClick = { onSelected(minutes) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyTaskCard(summary: TodayFitnessSummary) {
    val tasks = buildDailyTasks(summary)
        .sortedWith(compareBy<DailyTaskUiItem> { it.done }.thenBy { it.order })
    val shouldScroll = tasks.size > 3

    BingoCard {
        SectionTitle("今日任务")
        if (shouldScroll) {
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(196.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tasks.forEach { task ->
                        TaskRow(task.iconResId, task.label, task.reward, task.done, task.color)
                    }
                }
                TaskScrollIndicator(scrollState, modifier = Modifier.fillMaxHeight())
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tasks.forEach { task ->
                    TaskRow(task.iconResId, task.label, task.reward, task.done, task.color)
                }
            }
        }
        Text(
            "已完成 ${summary.completedTaskCount}/${summary.totalTaskCount}，连续 ${summary.streakDays} 天保持节奏。",
            color = AppColors.TextSecondary,
            fontSize = 12.sp
        )
    }
}

private data class DailyTaskUiItem(
    val order: Int,
    val iconResId: Int,
    val label: String,
    val reward: String,
    val done: Boolean,
    val color: Color
)

private fun buildDailyTasks(summary: TodayFitnessSummary): List<DailyTaskUiItem> = listOf(
    DailyTaskUiItem(
        order = 0,
        iconResId = R.drawable.today_task_exercise,
        label = "完成30分钟运动",
        reward = "奖励：脂肪怪 -8%",
        done = summary.exerciseMinutes >= summary.targetExerciseMinutes,
        color = AppColors.LightGreen
    ),
    DailyTaskUiItem(
        order = 1,
        iconResId = R.drawable.today_task_food,
        label = "记录一次饮食",
        reward = "奖励：肌肉伙伴 +5",
        done = summary.hasFoodLog,
        color = AppColors.SoftPurple
    ),
    DailyTaskUiItem(
        order = 2,
        iconResId = R.drawable.today_task_water,
        label = "喝水 8 杯",
        reward = "奖励：恢复 +3",
        done = summary.completedTaskCount >= 3,
        color = AppColors.SoftBlue
    ),
    DailyTaskUiItem(
        order = 3,
        iconResId = R.drawable.data_overview_weight,
        label = "记录今日体重",
        reward = "奖励：趋势 +1",
        done = summary.hasWeightLog,
        color = AppColors.LightGreen
    ),
    DailyTaskUiItem(
        order = 4,
        iconResId = R.drawable.data_overview_burn,
        label = "完成消耗目标",
        reward = "奖励：脂肪怪 -5%",
        done = summary.caloriesBurned >= summary.targetCalories,
        color = AppColors.LightOrange
    )
)

@Composable
private fun HomeSummaryCards(summary: TodayFitnessSummary) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        HomeSummaryCard(R.drawable.data_overview_weight, "体重(kg)", "72.0", AppColors.LightGreen, Modifier.weight(1f))
        HomeSummaryCard(R.drawable.data_overview_exercise, "运动(min)", "${summary.exerciseMinutes}", AppColors.SoftBlue, Modifier.weight(1f))
        HomeSummaryCard(R.drawable.data_overview_burn, "消耗(kcal)", "${summary.caloriesBurned}", AppColors.LightOrange, Modifier.weight(1f))
    }
}

@Composable
internal fun ReminderCard(title: String = "今日提醒：", text: String) {
    BingoCard(contentPadding = 12.dp, radius = 22.dp) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Image(
                painter = painterResource(R.drawable.tip_fat_icon),
                contentDescription = title,
                modifier = Modifier.size(54.dp),
                contentScale = ContentScale.Fit
            )
            Column {
                Text(title, color = AppColors.TextNavy, fontWeight = FontWeight.Black)
                Text(text, color = AppColors.TextNavy, fontSize = 13.sp, lineHeight = 19.sp)
            }
        }
    }
}

@Composable
private fun HomeStartButton(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.home_start_btn),
        contentDescription = "开始今日反击",
        modifier = modifier.height(75.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun BattleReportButton(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.76f)
            .height(34.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, AppColors.PrimaryOrange.copy(alpha = 0.78f), RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.38f))
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = AppColors.PrimaryOrange, fontSize = 16.sp, fontWeight = FontWeight.Black)
        Text("›", color = AppColors.BorderWarm, fontSize = 28.sp, modifier = Modifier.align(Alignment.CenterEnd))
    }
}

private data class StatMetricCardTuning(
    val cardHeight: Dp = 54.dp,
    val cardPaddingHorizontal: Dp = 5.dp,
    val cardPaddingVertical: Dp = 5.dp,
    val iconSize: Dp = 20.dp,
    val iconOffsetX: Dp = 0.dp,
    val iconOffsetY: Dp = 0.dp,
    val labelOffsetX: Dp = 0.dp,
    val labelOffsetY: Dp = 0.dp,
    val labelFontSize: TextUnit = 10.sp,
    val labelLineHeight: TextUnit = 12.sp,
    val titleGap: Dp = 2.dp,
    val progressValueGap: Dp = 4.dp,
    val progressHeight: Dp = 4.dp,
    val progressOffsetX: Dp = 0.dp,
    val progressOffsetY: Dp = 0.dp,
    val valueOffsetX: Dp = 0.dp,
    val valueOffsetY: Dp = 0.dp,
    val valueFontSize: TextUnit = 10.sp
)

@Composable
private fun StatMetricCard(
    iconResId: Int,
    label: String,
    value: String,
    color: Color,
    progress: Float,
    tuning: StatMetricCardTuning,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .height(tuning.cardHeight)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.94f))
            .border(1.dp, AppColors.BorderWarm.copy(alpha = 0.58f), RoundedCornerShape(14.dp))
            .padding(horizontal = tuning.cardPaddingHorizontal, vertical = tuning.cardPaddingVertical),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(tuning.titleGap))
        {
            Image(
                painter = painterResource(iconResId),
                contentDescription = label,
                modifier = Modifier
                    .size(tuning.iconSize)
                    .offset(x = tuning.iconOffsetX, y = tuning.iconOffsetY),
                contentScale = ContentScale.Fit
            )
            Text(
                label,
                modifier = Modifier.offset(x = tuning.labelOffsetX, y = tuning.labelOffsetY),
                color = AppColors.TextNavy,
                fontSize = tuning.labelFontSize,
                lineHeight = tuning.labelLineHeight,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(tuning.progressValueGap))
        {
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .weight(1f)
                    .height(tuning.progressHeight)
                    .offset(x = tuning.progressOffsetX, y = tuning.progressOffsetY)
                    .clip(RoundedCornerShape(99.dp)),
                color = color,
                trackColor = AppColors.BorderWarm.copy(alpha = 0.54f)
            )
            Text(
                value,
                color = AppColors.TextNavy,
                fontSize = tuning.valueFontSize,
                fontWeight = FontWeight.Black,
                modifier = Modifier.offset(x = tuning.valueOffsetX, y = tuning.valueOffsetY),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TaskRow(iconResId: Int, label: String, reward: String, done: Boolean, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, AppColors.BorderWarm.copy(alpha = 0.65f), RoundedCornerShape(16.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(iconResId),
                contentDescription = label,
                modifier = Modifier.size(30.dp),
                contentScale = ContentScale.Fit
            )
        }
        Text(label,
            color = AppColors.TextNavy,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            modifier = Modifier.weight(1f)
        )
        RewardChip(reward, color)
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .border(1.5.dp, if (done) AppColors.GrowthGreen else AppColors.BorderWarm, CircleShape)
                .background(if (done) AppColors.GrowthGreen else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (done) Text("✓", color = Color.White, fontSize = 12.sp)
        }
    }
}

@Composable
private fun HomeSummaryCard(iconResId: Int, label: String, value: String, color: Color, modifier: Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(AppColors.CardWhite)
            .border(1.dp, AppColors.BorderWarm.copy(alpha = 0.72f), RoundedCornerShape(18.dp))
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(iconResId),
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.Fit
            )
        }
        Column {
            Text(
                label,
                color = AppColors.TextSecondary,
                fontSize = 10.sp,
                lineHeight = 12.sp,
                maxLines = 1,
                softWrap = false
            )
            Text(value, color = AppColors.TextNavy, fontSize = 13.sp, fontWeight = FontWeight.Black)
        }
    }
}
