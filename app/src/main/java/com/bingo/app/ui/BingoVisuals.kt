package com.bingo.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingo.app.R
import com.bingo.app.mock.MockBingoData
import com.bingo.app.model.CharacterBattleState
import com.bingo.app.model.FatMonsterState
import com.bingo.app.model.MuscleBuddyState
import com.bingo.app.model.TodayFitnessSummary
import com.bingo.app.ui.theme.AppColors
import kotlin.math.roundToInt

enum class BingoTab(val title: String, val icon: String) {
    Today("今日", "⌂"),
    Training("训练", "◆"),
    Records("记录", "▣"),
    Community("广场", "◉"),
    Profile("我的", "♙")
}

@Composable
fun BingoBottomBar(selectedTab: BingoTab, onTabSelected: (BingoTab) -> Unit) {
    NavigationBar(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .shadow(10.dp, RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(30.dp)),
        containerColor = AppColors.CardWhite,
        tonalElevation = 0.dp
    ) {
        BingoTab.entries.forEach { tab ->
            val selected = selectedTab == tab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Text(
                        text = tab.icon,
                        color = if (selected) AppColors.PrimaryOrange else AppColors.TextSecondary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        color = if (selected) AppColors.PrimaryOrange else AppColors.TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = AppColors.LightOrange,
                    selectedIconColor = AppColors.PrimaryOrange,
                    unselectedIconColor = AppColors.TextSecondary
                )
            )
        }
    }
}

@Composable
fun HomeScreen(
    summary: TodayFitnessSummary,
    battleState: CharacterBattleState,
    debugMinutes: Int,
    onDebugMinutesChanged: (Int) -> Unit
) {
    ScreenList {
        item {
            PageHeader(
                title = "早上好，今天准备反击了吗？",
                subtitle = "今日目标：运动 30 分钟 · 饮水 2L",
                trailing = { CharacterAvatar(MuscleBuddyState.Powered) }
            )
        }
        item { TodayBattleCard(summary = summary, state = battleState) }
        item { DebugStateSwitcher(selectedMinutes = debugMinutes, onSelected = onDebugMinutesChanged) }
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
fun TrainingScreen() {
    ScreenList {
        item {
            PageHeader(
                title = "今天想怎么反击？",
                subtitle = "选一个训练，给脂肪怪来点压力。",
                trailing = {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        CircleIcon("▣", AppColors.CardWhite)
                        CircleIcon("♕", AppColors.CardWhite)
                    }
                }
            )
        }
        item { TrainingHeroCard() }
        item { TrainingTypeGrid() }
        item { RunningLeaderboardCard() }
    }
}

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
fun CommunityScreen() {
    ScreenList {
        item {
            PageHeader(
                brandColor = AppColors.TextNavy,
                title = "反脂广场",
                subtitle = "晒真实生活，不晒完美人设。",
                trailing = {
                    Box(contentAlignment = Alignment.Center) {
                        MiniFriendIllustration()
                        CircleIcon("+", AppColors.PrimaryOrange, Color.White, 58.dp)
                    }
                }
            )
        }
        item { CommunityCategoryTabs() }
        item {
            CommunityPostCard(
                name = "火锅幸存者",
                time = "2小时前",
                tag = "放纵餐",
                title = "火锅局幸存报告",
                body = "今天吃了火锅，但没喝奶茶，算不算赢一半？",
                reply = "脂肪怪：这局我本来优势很大。",
                imageType = 0,
                likes = 236,
                comments = 38
            )
        }
        item {
            CommunityPostCard(
                name = "奶茶戒断中",
                time = "4小时前",
                tag = "训练日餐",
                title = "今天给肌肉伙伴发工资",
                body = "鸡蛋、牛肉、玉米，蛋白质安排上了。",
                reply = "肌肉伙伴：这顿我认可。",
                imageType = 1,
                likes = 198,
                comments = 26
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen() {
    ScreenList {
        item { ProfileHeroCard() }
        item {
            BingoCard {
                SectionTitle("我的称号", "全部 >")
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MockBingoData.titles.forEachIndexed { index, title ->
                        val colors = listOf(AppColors.LightOrange, AppColors.LightGreen, AppColors.SoftPurple, AppColors.SoftBlue)
                        RewardChip(text = title, color = colors[index % colors.size])
                    }
                }
            }
        }
        item {
            BingoCard {
                SectionTitle("我的成就", "全部 >")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    MockBingoData.achievements.take(4).forEachIndexed { index, achievement ->
                        AchievementBadge(achievement.first, listOf("7", "🔥", "☀", "♕")[index], Modifier.weight(1f))
                    }
                }
            }
        }
        item {
            BingoCard {
                listOf("个人资料", "目标设置", "通知提醒", "关于 Bingo").forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(it, color = AppColors.TextNavy, fontWeight = FontWeight.SemiBold)
                        Text("›", color = AppColors.TextSecondary, fontSize = 22.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreenList(content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background),
        contentPadding = PaddingValues(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        content = content
    )
}

@Composable
private fun PageHeader(
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

@Composable
fun BingoLogo(color: Color = AppColors.PrimaryOrange) {
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = "Bingo",
        modifier = Modifier
            .width(75.dp)
            .height(30.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun HomeGoalText(text: String) {
    val targetText = "今日目标：运动30分钟 · 饮水2L"
    val displayText = if (text.contains("今日目标")) targetText else text
    val annotatedText = buildAnnotatedString {
        append(displayText)
        listOf("30", "2L").forEach { accent ->
            val start = displayText.indexOf(accent)
            if (start >= 0) {
                addStyle(
                    SpanStyle(color = AppColors.OrangeDeep, fontWeight = FontWeight.Black),
                    start = start,
                    end = start + accent.length
                )
            }
        }
    }
    Text(
        text = annotatedText,
        color = AppColors.TextSecondary,
        fontSize = 15.sp,
        lineHeight = 21.sp,
        fontWeight = FontWeight.Medium
    )
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
                        color = AppColors.HealthyGreen,
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
private fun TaskScrollIndicator(scrollState: ScrollState, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.width(4.dp)) {
        val radius = CornerRadius(size.width / 2f, size.width / 2f)
        drawRoundRect(
            color = AppColors.BorderWarm.copy(alpha = 0.42f),
            size = Size(size.width, size.height),
            cornerRadius = radius
        )

        val thumbHeight = size.height * 0.34f
        val progress = if (scrollState.maxValue == 0) {
            0f
        } else {
            scrollState.value.toFloat() / scrollState.maxValue.toFloat()
        }
        drawRoundRect(
            color = AppColors.PrimaryOrange.copy(alpha = 0.8f),
            topLeft = Offset(0f, (size.height - thumbHeight) * progress),
            size = Size(size.width, thumbHeight),
            cornerRadius = radius
        )
    }
}

@Composable
private fun HomeSummaryCards(summary: TodayFitnessSummary) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        HomeSummaryCard(R.drawable.data_overview_weight, "体重(kg)", "72.0", AppColors.LightGreen, Modifier.weight(1f))
        HomeSummaryCard(R.drawable.data_overview_exercise, "运动(min)", "${summary.exerciseMinutes}", AppColors.SoftBlue, Modifier.weight(1f))
        HomeSummaryCard(R.drawable.data_overview_burn, "消耗(kcal)", "${summary.caloriesBurned}", AppColors.LightOrange, Modifier.weight(1f))
    }
}

@Composable
private fun ReminderCard(title: String = "今日提醒：", text: String) {
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
private fun TrainingHeroCard() {
    BingoCard(contentPadding = 0.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(292.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Brush.horizontalGradient(listOf(Color.White, AppColors.LightOrange)))
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.illustration_training_hero),
                contentDescription = "Training hero illustration",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(210.dp)
                    .offset(x = 18.dp),
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier.fillMaxSize()) {
                RewardChip("今日推荐", AppColors.LightOrange, AppColors.PrimaryOrange)
                Spacer(Modifier.height(12.dp))
                Text("全身燃脂训练", color = AppColors.TextNavy, fontSize = 29.sp, lineHeight = 35.sp, fontWeight = FontWeight.Black)
                Text("30 分钟 · 中等强度 · 预计 220 kcal", color = AppColors.TextSecondary, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
                SpeechBubble("肌肉伙伴已热身，脂肪怪还在嘴硬。", Color.White, AppColors.BorderWarm, Modifier.width(160.dp))
                Spacer(Modifier.weight(1f))
                PrimaryGradientButton("开始训练", modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun TrainingTypeGrid() {
    BingoCard {
        SectionTitle("训练类型")
        MockBingoData.trainingTypes.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEachIndexed { index, item ->
                    TrainingTypeCard(
                        title = item.first,
                        subtitle = item.second,
                        icon = listOf("♟", "⌂", "▮", "♙", "◔", "⚡")[MockBingoData.trainingTypes.indexOf(item)],
                        color = listOf(AppColors.LightGreen, AppColors.LightOrange, AppColors.SoftPurple, AppColors.SoftBlue, Color(0xFFFFF5CC), AppColors.LightGreen)[MockBingoData.trainingTypes.indexOf(item)],
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun RunningLeaderboardCard() {
    BingoCard {
        SectionTitle("本周跑路榜")
        Text("注意：这里的跑路是物理意义上的跑路。", color = AppColors.TextSecondary, fontSize = 13.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            TrophyIllustration(Modifier.weight(0.9f).height(148.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1.1f)) {
                LeaderMetric("★", "当前排名：", "第 18 名", AppColors.HealthyGreen)
                LeaderMetric("●", "本周距离：", "8.6 km", AppColors.HealthyGreen)
                LeaderMetric("⚑", "再跑", "1.2 km 可超过前一名", AppColors.PrimaryOrange)
                OutlineOrangeButton("查看排行榜")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            RewardChip("进步榜", AppColors.LightGreen, AppColors.HealthyGreen, Modifier.weight(1f))
            RewardChip("坚持榜", Color(0xFFFFF5CC), Color(0xFFE39A00), Modifier.weight(1f))
            RewardChip("燃脂榜", AppColors.SoftPurple, AppColors.Purple, Modifier.weight(1f))
        }
    }
}

@Composable
private fun WeightTrendCard() {
    BingoCard {
        SectionTitle("体重趋势")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MetricPill("当前体重", "72.0 kg", AppColors.TextNavy, Modifier.weight(1f))
            MetricPill("近 7 天", "-0.6 kg", AppColors.HealthyGreen, Modifier.weight(1f))
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
            CircleIcon("↗", AppColors.LightGreen, AppColors.HealthyGreen, 72.dp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CommunityCategoryTabs() {
    val tabs = MockBingoData.communityTabs
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        tabs.forEachIndexed { index, tab ->
            PillButton(text = tab, selected = index == 0, onClick = {})
        }
    }
}

@Composable
private fun CommunityPostCard(
    name: String,
    time: String,
    tag: String,
    title: String,
    body: String,
    reply: String,
    imageType: Int,
    likes: Int,
    comments: Int
) {
    BingoCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CharacterAvatar(if (imageType == 0) MuscleBuddyState.Ready else MuscleBuddyState.Active, size = 48.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(name, color = AppColors.TextNavy, fontWeight = FontWeight.Black)
                Text(time, color = AppColors.TextSecondary, fontSize = 12.sp)
            }
            RewardChip(tag, if (imageType == 0) AppColors.LightOrange else AppColors.LightGreen)
            Text("•••", color = AppColors.TextSecondary)
        }
        CommunityImagePlaceholder(imageType)
        Text(title, color = AppColors.TextNavy, fontSize = 20.sp, fontWeight = FontWeight.Black)
        Text(body, color = AppColors.TextSecondary, fontSize = 14.sp, lineHeight = 20.sp)
        SpeechBubble(reply, AppColors.LightOrange, AppColors.BorderWarm)
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("❤ $likes", color = if (imageType == 0) AppColors.PrimaryOrange else AppColors.HealthyGreen)
            Text("♡ $comments", color = AppColors.TextSecondary)
            Text("▱ 收藏", color = AppColors.TextSecondary)
            Text("☻ 同款翻车", color = AppColors.TextSecondary)
        }
    }
}

@Composable
private fun ProfileHeroCard() {
    BingoCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CharacterAvatar(MuscleBuddyState.Ready, size = 72.dp)
                Text("小B同学 ✎", color = AppColors.TextNavy, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Text("Lv.3 初级脂肪猎人", color = AppColors.TextNavy, fontWeight = FontWeight.SemiBold)
                LinearProgressIndicator(
                    progress = { 560f / 1200f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(99.dp)),
                    color = AppColors.PrimaryOrange,
                    trackColor = AppColors.LightOrange
                )
                Text("560 / 1200 XP", color = AppColors.TextSecondary, fontSize = 12.sp)
            }
            MuscleBuddyView(MuscleBuddyState.Powered, modifier = Modifier.size(130.dp))
        }
    }
}

@Composable
private fun BingoCard(
    modifier: Modifier = Modifier,
    radius: Dp = 26.dp,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(radius), ambientColor = AppColors.BorderWarm.copy(alpha = 0.28f), spotColor = AppColors.BorderWarm.copy(alpha = 0.28f))
            .clip(RoundedCornerShape(radius))
            .background(AppColors.CardWhite)
            .border(1.dp, AppColors.BorderWarm.copy(alpha = 0.72f), RoundedCornerShape(radius))
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content
    )
}

@Composable
private fun SectionTitle(title: String, action: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(AppColors.PrimaryOrange))
            Text(title, color = AppColors.TextNavy, fontSize = 18.sp, fontWeight = FontWeight.Black)
        }
        if (action != null) Text(action, color = AppColors.TextSecondary, fontSize = 12.sp)
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
private fun PrimaryGradientButton(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(58.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.horizontalGradient(listOf(AppColors.OrangeDeep, AppColors.PrimaryOrange))),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
        Text("›", color = Color.White, fontSize = 32.sp, modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
private fun OutlineOrangeButton(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .border(1.5.dp, AppColors.PrimaryOrange, RoundedCornerShape(22.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = AppColors.PrimaryOrange, fontWeight = FontWeight.Black)
    }
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
                .border(1.5.dp, if (done) AppColors.HealthyGreen else AppColors.BorderWarm, CircleShape)
                .background(if (done) AppColors.HealthyGreen else Color.Transparent),
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

@Composable
private fun TrainingTypeCard(title: String, subtitle: String, icon: String, color: Color, modifier: Modifier) {
    Row(
        modifier = modifier
            .height(88.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, AppColors.BorderWarm.copy(alpha = 0.72f), RoundedCornerShape(20.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CircleIcon(icon, color, AppColors.PrimaryOrange, 48.dp)
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = AppColors.TextNavy, fontWeight = FontWeight.Black)
            Text(subtitle, color = AppColors.TextSecondary, fontSize = 12.sp)
        }
        Text("›", color = AppColors.BorderWarm, fontSize = 24.sp)
    }
}

@Composable
private fun LeaderMetric(icon: String, label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CircleIcon(icon, color.copy(alpha = 0.18f), color, 30.dp)
        Text(label, color = AppColors.TextSecondary, fontSize = 13.sp)
        Text(value, color = color, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun MetricPill(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(AppColors.Background)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(label, color = AppColors.TextSecondary, fontSize = 12.sp)
        Text(value, color = color, fontSize = 22.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun RewardChip(text: String, color: Color, textColor: Color = AppColors.TextNavy, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(99.dp))
            .background(color)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
private fun PillButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(99.dp))
            .clickable(onClick = onClick)
            .background(if (selected) AppColors.PrimaryOrange else AppColors.CardWhite)
            .border(1.dp, if (selected) AppColors.PrimaryOrange else AppColors.BorderWarm, RoundedCornerShape(99.dp))
            .padding(horizontal = 14.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else AppColors.TextNavy,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SpeechBubble(
    text: String,
    color: Color,
    border: Color,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 10.dp,
    verticalPadding: Dp = 8.dp
) {
    Text(
        text = text,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color)
            .border(1.dp, border, RoundedCornerShape(14.dp))
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        color = AppColors.TextNavy,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun BattleImageBubble(
    text: String,
    bubbleResId: Int,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 10.dp,
    verticalPadding: Dp = 8.dp,
    textOffsetX: Dp = 0.dp,
    textOffsetY: Dp = 0.dp,
    textAlign: TextAlign = TextAlign.Center
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(bubbleResId),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .offset(x = textOffsetX, y = textOffsetY),
            color = AppColors.TextNavy,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            textAlign = textAlign
        )
    }
}

@Composable
private fun CircleIcon(text: String, background: Color, textColor: Color = AppColors.TextNavy, size: Dp = 44.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(background)
            .border(1.dp, AppColors.BorderWarm.copy(alpha = 0.6f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontSize = (size.value * 0.42f).roundToInt().sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun CharacterAvatar(state: MuscleBuddyState, size: Dp = 58.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(AppColors.SoftBlue)
            .border(1.dp, AppColors.HealthyGreen, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        MuscleBuddyView(state, modifier = Modifier.size(size * 0.92f))
    }
}

@Composable
private fun FatMonsterView(state: FatMonsterState, modifier: Modifier = Modifier.size(108.dp)) {
    CharacterImage(
        resourceId = state.drawableResId(),
        contentDescription = "Fat monster ${state.name.lowercase()} state",
        modifier = modifier
    )
}

@Composable
private fun MuscleBuddyView(state: MuscleBuddyState, modifier: Modifier = Modifier.size(108.dp)) {
    CharacterImage(
        resourceId = state.drawableResId(),
        contentDescription = "Muscle buddy ${state.name.lowercase()} state",
        modifier = modifier
    )
}

@Composable
private fun CharacterImage(resourceId: Int, contentDescription: String, modifier: Modifier) {
    Image(
        painter = painterResource(resourceId),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

private fun FatMonsterState.drawableResId(): Int = when (this) {
    FatMonsterState.Normal -> R.drawable.fat_monster_normal
    FatMonsterState.Lazy -> R.drawable.fat_monster_lazy
    FatMonsterState.Teasing -> R.drawable.fat_monster_teasing
    FatMonsterState.Nervous -> R.drawable.fat_monster_nervous
    FatMonsterState.Weakened -> R.drawable.fat_monster_weakened
    FatMonsterState.Defeated -> R.drawable.fat_monster_defeated
}

private fun MuscleBuddyState.drawableResId(): Int = when (this) {
    MuscleBuddyState.Normal -> R.drawable.muscle_buddy_normal
    MuscleBuddyState.Sleepy -> R.drawable.muscle_buddy_sleepy
    MuscleBuddyState.Ready -> R.drawable.muscle_buddy_ready
    MuscleBuddyState.Active -> R.drawable.muscle_buddy_active
    MuscleBuddyState.Powered -> R.drawable.muscle_buddy_powered
    MuscleBuddyState.Victory -> R.drawable.muscle_buddy_victory
}

@Composable
private fun BattleStageBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        drawOval(AppColors.LightOrange.copy(alpha = 0.7f), topLeft = Offset(-w * 0.15f, h * 0.13f), size = Size(w * 1.3f, h * 0.58f))
        repeat(18) { i ->
            val x = (i * 53 % w.toInt()).toFloat()
            val y = h * (0.12f + (i % 7) * 0.09f)
            drawCircle(listOf(AppColors.EnergyYellow, AppColors.HealthyGreen, AppColors.PrimaryOrange)[i % 3].copy(alpha = 0.5f), radius = 4f, center = Offset(x, y))
        }
        drawLine(AppColors.BorderWarm.copy(alpha = 0.5f), Offset(0f, h * 0.68f), Offset(w, h * 0.68f), strokeWidth = 2f)
    }
}

@Composable
private fun GymBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(Color.White.copy(alpha = 0.42f))
        repeat(5) { i ->
            drawLine(AppColors.BorderWarm.copy(alpha = 0.42f), Offset(size.width * (0.46f + i * 0.1f), 0f), Offset(size.width * (0.4f + i * 0.1f), size.height), 2f)
        }
        drawOval(AppColors.LightGreen.copy(alpha = 0.54f), topLeft = Offset(size.width * 0.45f, size.height * 0.55f), size = Size(size.width * 0.42f, size.height * 0.18f))
        drawCircle(Color(0xFF2E2E2E), radius = 14f, center = Offset(size.width * 0.73f, size.height * 0.67f))
        drawCircle(Color(0xFF2E2E2E), radius = 14f, center = Offset(size.width * 0.86f, size.height * 0.67f))
        drawLine(Color(0xFF666666), Offset(size.width * 0.73f, size.height * 0.67f), Offset(size.width * 0.86f, size.height * 0.67f), 8f)
    }
}

@Composable
private fun TrophyIllustration(modifier: Modifier) {
    Canvas(modifier = modifier.clip(RoundedCornerShape(22.dp)).background(AppColors.LightOrange)) {
        val w = size.width
        val h = size.height
        drawRect(Color(0xFFFFC58E), topLeft = Offset(w * 0.28f, h * 0.68f), size = Size(w * 0.44f, h * 0.18f))
        drawOval(AppColors.EnergyYellow, topLeft = Offset(w * 0.28f, h * 0.16f), size = Size(w * 0.44f, h * 0.38f))
        drawRect(AppColors.EnergyYellow, topLeft = Offset(w * 0.44f, h * 0.5f), size = Size(w * 0.12f, h * 0.2f))
        drawCircle(Color.White.copy(alpha = 0.5f), radius = w * 0.09f, center = Offset(w * 0.5f, h * 0.35f))
        drawCircle(AppColors.PrimaryOrange, radius = w * 0.035f, center = Offset(w * 0.5f, h * 0.35f))
        repeat(8) { i -> drawCircle(listOf(AppColors.PrimaryOrange, AppColors.HealthyGreen, AppColors.EnergyYellow)[i % 3], 4f, Offset((i + 1) * w / 9, h * (0.1f + (i % 2) * 0.18f))) }
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
            drawLine(AppColors.HealthyGreen, points[i], points[i + 1], strokeWidth = 5f)
        }
        points.forEach { point ->
            drawCircle(Color.White, radius = 8f, center = point)
            drawCircle(AppColors.HealthyGreen, radius = 5f, center = point)
        }
    }
}

@Composable
private fun FoodBowlIllustration(modifier: Modifier) {
    Canvas(modifier = modifier) {
        drawOval(AppColors.LightGreen, topLeft = Offset(size.width * 0.12f, size.height * 0.28f), size = Size(size.width * 0.76f, size.height * 0.44f))
        drawArc(AppColors.PrimaryOrange, 0f, 180f, true, topLeft = Offset(size.width * 0.16f, size.height * 0.36f), size = Size(size.width * 0.68f, size.height * 0.46f))
        drawCircle(AppColors.HealthyGreen, radius = size.width * 0.09f, center = Offset(size.width * 0.36f, size.height * 0.36f))
        drawCircle(AppColors.EnergyYellow, radius = size.width * 0.08f, center = Offset(size.width * 0.55f, size.height * 0.32f))
        drawCircle(Color(0xFFFF6B6B), radius = size.width * 0.07f, center = Offset(size.width * 0.64f, size.height * 0.44f))
    }
}

@Composable
private fun CommunityImagePlaceholder(imageType: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(174.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    if (imageType == 0) listOf(Color(0xFFFFC68A), Color(0xFFFFF0DC)) else listOf(Color(0xFFE8F8DD), Color(0xFFFFF0DC))
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (imageType == 0) {
                drawOval(Color(0xFF9A4E23), topLeft = Offset(size.width * 0.16f, size.height * 0.22f), size = Size(size.width * 0.58f, size.height * 0.46f))
                drawOval(Color(0xFFED4B22), topLeft = Offset(size.width * 0.2f, size.height * 0.28f), size = Size(size.width * 0.5f, size.height * 0.34f))
                repeat(14) { i -> drawCircle(Color(0xFFFFD09A), radius = 10f, center = Offset(size.width * (0.12f + (i % 7) * 0.11f), size.height * (0.22f + (i / 7) * 0.5f))) }
            } else {
                drawOval(Color.White, topLeft = Offset(size.width * 0.14f, size.height * 0.18f), size = Size(size.width * 0.62f, size.height * 0.54f))
                drawCircle(AppColors.EnergyYellow, radius = 24f, center = Offset(size.width * 0.42f, size.height * 0.42f))
                repeat(8) { i -> drawCircle(AppColors.HealthyGreen, radius = 16f, center = Offset(size.width * (0.18f + i * 0.07f), size.height * (0.62f - (i % 2) * 0.22f))) }
            }
        }
        if (imageType == 0) FatMonsterView(FatMonsterState.Nervous, modifier = Modifier.align(Alignment.BottomEnd).size(96.dp))
        else MuscleBuddyView(MuscleBuddyState.Victory, modifier = Modifier.align(Alignment.BottomStart).size(82.dp))
    }
}

@Composable
private fun MiniFriendIllustration() {
    Row(
        modifier = Modifier
            .width(128.dp)
            .height(86.dp)
            .padding(end = 42.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        FatMonsterView(FatMonsterState.Lazy, modifier = Modifier.size(68.dp))
    }
}

@Composable
private fun AchievementBadge(title: String, badge: String, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        CircleIcon(badge, AppColors.LightGreen, AppColors.HealthyGreen, 54.dp)
        Text(title, color = AppColors.TextNavy, fontSize = 11.sp, textAlign = TextAlign.Center, lineHeight = 14.sp)
    }
}
