package com.bingo.app.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingo.app.R
import com.bingo.app.mock.LeaderboardRunner
import com.bingo.app.mock.MockBingoData
import com.bingo.app.model.MuscleBuddyState
import com.bingo.app.model.TrainingOption
import com.bingo.app.ui.BingoCard
import com.bingo.app.ui.CharacterAvatar
import com.bingo.app.ui.OutlineOrangeButton
import com.bingo.app.ui.PageHeader
import com.bingo.app.ui.PrimaryGradientButton
import com.bingo.app.ui.RewardChip
import com.bingo.app.ui.ScreenList
import com.bingo.app.ui.SectionTitle
import com.bingo.app.ui.SpeechBubble
import com.bingo.app.ui.theme.AppColors

private enum class LeaderboardCategory(
    val title: String,
    val subtitle: String,
    val metricLabel: String,
    val myRank: String,
    val myValue: String,
    val chaseValue: String,
    val tip: String,
    val color: Color,
    val background: Color
) {
    Overall(
        title = "总榜",
        subtitle = "看本周谁最会把跑路变成训练。",
        metricLabel = "本周距离",
        myRank = "第 18 名",
        myValue = "8.6 km",
        chaseValue = "1.2km",
        tip = "再跑 1.2km 可超越前一名，今天适合来一段轻松跑。",
        color = AppColors.PrimaryOrange,
        background = AppColors.LightOrange
    ),
    Progress(
        title = "进步榜",
        subtitle = "按本周距离相对上周的提升幅度排序。",
        metricLabel = "进步幅度",
        myRank = "第 9 名",
        myValue = "+32%",
        chaseValue = "+6%",
        tip = "再多完成一次 15 分钟训练，进步榜排名会更稳。",
        color = AppColors.GrowthGreen,
        background = AppColors.LightGreen
    ),
    Streak(
        title = "坚持榜",
        subtitle = "按连续完成训练打卡的天数排序。",
        metricLabel = "连续天数",
        myRank = "第 12 名",
        myValue = "6 天",
        chaseValue = "1 天",
        tip = "今天完成训练后，坚持榜可以继续向前挤一挤。",
        color = Color(0xFFE39A00),
        background = Color(0xFFFFF5CC)
    ),
    Burn(
        title = "燃脂榜",
        subtitle = "按本周训练消耗的热量排序。",
        metricLabel = "本周消耗",
        myRank = "第 15 名",
        myValue = "680 kcal",
        chaseValue = "90 kcal",
        tip = "一组中等强度燃脂训练，就够你追上前一名。",
        color = AppColors.Purple,
        background = AppColors.SoftPurple
    )
}

@Composable
fun TrainingScreen(onTrainingCompleted: (TrainingOption) -> Unit) {
    var activeLeaderboard by rememberSaveable { mutableStateOf<LeaderboardCategory?>(null) }
    var selectedTrainingId by rememberSaveable { mutableStateOf<String?>(null) }
    val selectedTraining = MockBingoData.trainingTypes.firstOrNull { it.id == selectedTrainingId }

    BackHandler(enabled = selectedTraining != null || activeLeaderboard != null) {
        if (selectedTraining != null) selectedTrainingId = null else activeLeaderboard = null
    }

    if (selectedTraining != null) {
        TrainingDetailScreen(
            training = selectedTraining,
            onBack = { selectedTrainingId = null },
            onComplete = {
                selectedTrainingId = null
                onTrainingCompleted(selectedTraining)
            }
        )
    } else if (activeLeaderboard == null) {
        ScreenList {
            item {
                PageHeader(
                    title = stringResource(R.string.training_title),
                    subtitle = stringResource(R.string.training_subtitle),
                    trailing = { CharacterAvatar(MuscleBuddyState.Ready) }
                )
            }
            item {
                TrainingHeroCard(
                    training = MockBingoData.recommendedTraining,
                    onClick = { selectedTrainingId = MockBingoData.recommendedTraining.id }
                )
            }
            item { TrainingTypeGrid(onTrainingSelected = { selectedTrainingId = it.id }) }
            item {
                RunningLeaderboardCard(
                    onCategoryClick = { activeLeaderboard = it },
                    onViewAllClick = { activeLeaderboard = LeaderboardCategory.Overall }
                )
            }
        }
    } else {
        RunningLeaderboardDetailScreen(
            selectedCategory = activeLeaderboard ?: LeaderboardCategory.Overall,
            onCategorySelected = { activeLeaderboard = it },
            onBack = { activeLeaderboard = null }
        )
    }
}

@Composable
private fun TrainingDetailScreen(
    training: TrainingOption,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    ScreenList {
        item {
            Text(
                text = stringResource(R.string.back),
                modifier = Modifier.clickable(onClick = onBack).padding(8.dp),
                color = AppColors.PrimaryOrange,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black
            )
        }
        item {
            PageHeader(
                title = training.title,
                subtitle = training.subtitle,
                trailing = { CharacterAvatar(MuscleBuddyState.Active) }
            )
        }
        item {
            BingoCard {
                SectionTitle("本次训练")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    MetricTile("时长", "${training.durationMinutes} 分钟", AppColors.PrimaryOrange, Modifier.weight(1f))
                    MetricTile("强度", training.intensity, AppColors.GrowthGreen, Modifier.weight(1f))
                    MetricTile("预计消耗", "${training.estimatedCalories} kcal", AppColors.Purple, Modifier.weight(1f))
                }
                Text(
                    "离线 MVP 暂不提供视频跟练。完成你选择的训练后，点击下方按钮记录本次成果。",
                    color = AppColors.TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }
        item {
            PrimaryGradientButton(
                text = stringResource(R.string.training_complete),
                modifier = Modifier.fillMaxWidth(),
                onClick = onComplete
            )
        }
    }
}

@Composable
private fun TrainingHeroCard(training: TrainingOption, onClick: () -> Unit) {
    BingoCard(contentPadding = 0.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Brush.horizontalGradient(listOf(Color.White, AppColors.LightOrange)))
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.illustration_training_hero),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(210.dp)
                    .offset(x = 18.dp,y = (-15).dp),
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier.fillMaxSize()) {
                RewardChip("今日推荐",
                    AppColors.LightOrange,
                    AppColors.PrimaryOrange,
                    modifier = Modifier.width(140.dp)
                        .height(30.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(training.title, color = AppColors.TextNavy, fontSize = 20.sp, lineHeight = 35.sp, fontWeight = FontWeight.Black)
                Text("${training.durationMinutes} 分钟 · ${training.intensity} · 预计 ${training.estimatedCalories} kcal", color = AppColors.TextSecondary, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
                SpeechBubble("肌肉伙伴已热身，脂肪怪还在嘴硬。", Color.White, AppColors.BorderWarm, Modifier.width(160.dp))
                Spacer(Modifier.weight(1f))
                PrimaryGradientButton(stringResource(R.string.training_view), modifier = Modifier.fillMaxWidth().height(45.dp), onClick = onClick)
            }
        }
    }
}

@Composable
private fun TrainingTypeGrid(onTrainingSelected: (TrainingOption) -> Unit) {
    BingoCard {
        SectionTitle(stringResource(R.string.training_types))
        val trainingIcons = listOf(
            R.drawable.training_type_run,
            R.drawable.training_type_burn,
            R.drawable.training_type_strength,
            R.drawable.training_type_stretch,
            R.drawable.training_type_core,
            R.drawable.training_type_quick
        )
        MockBingoData.trainingTypes.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEach { item ->
                    val itemIndex = MockBingoData.trainingTypes.indexOf(item)
                    TrainingTypeCard(
                        title = item.title,
                        subtitle = item.subtitle,
                        iconResId = trainingIcons[itemIndex],
                        modifier = Modifier.weight(1f),
                        onClick = { onTrainingSelected(item) }
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun RunningLeaderboardCard(
    onCategoryClick: (LeaderboardCategory) -> Unit,
    onViewAllClick: () -> Unit
) {
    val overview = LeaderboardCategory.Overall

    BingoCard {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionTitle("本周跑路榜")
                Text("这里的跑路是物理意义上的跑路。", color = AppColors.TextSecondary, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    LeaderboardChip(
                        category = LeaderboardCategory.Progress,
                        modifier = Modifier.weight(1f),
                        onClick = onCategoryClick
                    )
                    LeaderboardChip(
                        category = LeaderboardCategory.Streak,
                        modifier = Modifier.weight(1f),
                        onClick = onCategoryClick
                    )
                    LeaderboardChip(
                        category = LeaderboardCategory.Burn,
                        modifier = Modifier.weight(1f),
                        onClick = onCategoryClick
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(0.9f)) {
                        Image(
                            painter = painterResource(R.drawable.training_leaderboard_trophy),
                            contentDescription = "本周跑路榜奖杯",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(135.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1.1f)) {
                        LeaderMetric(R.drawable.leaderboard_metric_rank_transparent, "当前排名：", overview.myRank, AppColors.GrowthGreen)
                        LeaderMetric(R.drawable.leaderboard_metric_distance_transparent, "本周距离：", overview.myValue, AppColors.Blue)
                        LeaderMetric(R.drawable.leaderboard_metric_chase_transparent, "再跑", overview.chaseValue, AppColors.PrimaryOrange, "可超越前一名")
                    }
                }
                OutlineOrangeButton("查看排行榜", onClick = onViewAllClick)
            }
        }
    }
}

@Composable
private fun RunningLeaderboardDetailScreen(
    selectedCategory: LeaderboardCategory,
    onCategorySelected: (LeaderboardCategory) -> Unit,
    onBack: () -> Unit
) {
    ScreenList {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.back),
                    modifier = Modifier.clickable(onClick = onBack).padding(8.dp),
                    color = AppColors.PrimaryOrange,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )
                PageHeader(
                    title = "跑路排行榜",
                    subtitle = selectedCategory.subtitle,
                    trailing = { CharacterAvatar(MuscleBuddyState.Powered) }
                )
            }
        }
        item { LeaderboardSummaryCard(selectedCategory) }
        item {
            BingoCard {
                SectionTitle("榜单分类")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    LeaderboardChip(
                        category = LeaderboardCategory.Overall,
                        modifier = Modifier.weight(1f),
                        selected = selectedCategory == LeaderboardCategory.Overall,
                        onClick = onCategorySelected
                    )
                    LeaderboardChip(
                        category = LeaderboardCategory.Progress,
                        modifier = Modifier.weight(1f),
                        selected = selectedCategory == LeaderboardCategory.Progress,
                        onClick = onCategorySelected
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    LeaderboardChip(
                        category = LeaderboardCategory.Streak,
                        modifier = Modifier.weight(1f),
                        selected = selectedCategory == LeaderboardCategory.Streak,
                        onClick = onCategorySelected
                    )
                    LeaderboardChip(
                        category = LeaderboardCategory.Burn,
                        modifier = Modifier.weight(1f),
                        selected = selectedCategory == LeaderboardCategory.Burn,
                        onClick = onCategorySelected
                    )
                }
            }
        }
        item {
            BingoCard {
                SectionTitle(selectedCategory.title)
                MockBingoData.leaderboardRows[selectedCategory.title].orEmpty().forEachIndexed { index, row ->
                    LeaderboardRow(rank = index + 1, row = row, category = selectedCategory)
                }
            }
        }
    }
}

@Composable
private fun LeaderboardSummaryCard(category: LeaderboardCategory) {
    BingoCard {
        SectionTitle("我的本周战况")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MetricTile("当前排名", category.myRank, category.color, Modifier.weight(1f))
            MetricTile(category.metricLabel, category.myValue, AppColors.TextNavy, Modifier.weight(1f))
            MetricTile("距上一名", category.chaseValue, AppColors.PrimaryOrange, Modifier.weight(1f))
        }
        Text(category.tip, color = AppColors.TextSecondary, fontSize = 13.sp, lineHeight = 19.sp)
    }
}

@Composable
private fun MetricTile(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(AppColors.Background)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(label, color = AppColors.TextSecondary, fontSize = 11.sp, textAlign = TextAlign.Center)
        Text(value, color = color, fontSize = 17.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
    }
}

@Composable
private fun LeaderboardChip(
    category: LeaderboardCategory,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (LeaderboardCategory) -> Unit
) {
    val background = if (selected) category.color else category.background
    val textColor = if (selected) Color.White else category.color

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(99.dp))
            .clickable { onClick(category) }
            .background(background)
            .border(1.dp, category.color.copy(alpha = if (selected) 0f else 0.28f), RoundedCornerShape(99.dp))
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(category.title, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
private fun LeaderboardRow(rank: Int, row: LeaderboardRunner, category: LeaderboardCategory) {
    val rankColor = when (rank) {
        1 -> AppColors.PrimaryOrange
        2 -> AppColors.GrowthGreen
        3 -> AppColors.EnergyYellow
        else -> AppColors.TextSecondary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (row.isMe) AppColors.LightOrange else AppColors.Background)
            .border(
                1.dp,
                if (row.isMe) AppColors.PrimaryOrange.copy(alpha = 0.45f) else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = rank.toString(),
            modifier = Modifier.width(26.dp),
            color = rankColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(row.name, color = AppColors.TextNavy, fontWeight = FontWeight.Black, fontSize = 15.sp)
            Text(row.note, color = AppColors.TextSecondary, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(row.value, color = category.color, fontWeight = FontWeight.Black, fontSize = 16.sp)
            Text(row.badge, color = AppColors.TextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
private fun TrainingTypeCard(
    title: String,
    subtitle: String,
    iconResId: Int,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .background(Color.White)
            .border(1.dp, AppColors.BorderWarm.copy(alpha = 0.72f), RoundedCornerShape(20.dp))
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(iconResId),
            contentDescription = title,
            modifier = Modifier.size(35.dp),
            contentScale = ContentScale.Fit
        )
        Column(modifier = Modifier.weight(1f))
        {
            Text(title, color = AppColors.TextNavy, fontWeight = FontWeight.Black, fontSize = 15.sp,lineHeight = 8.sp)
            Text(subtitle, color = AppColors.TextSecondary, fontSize = 12.sp, lineHeight = 20.sp)
        }
    }
}

@Composable
private fun LeaderMetric(iconResId: Int, label: String, value: String, color: Color, hint: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Image(
            painter = painterResource(iconResId),
            contentDescription = label.trimEnd('：'),
            modifier = Modifier.size(30.dp),
            contentScale = ContentScale.Fit
        )
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(label, color = AppColors.TextSecondary, fontSize = 13.sp)
                Text(value, color = color, fontWeight = FontWeight.Black)
            }
            hint?.let {
                Text(it, color = AppColors.TextSecondary, fontSize = 13.sp)
            }
        }
    }
}
