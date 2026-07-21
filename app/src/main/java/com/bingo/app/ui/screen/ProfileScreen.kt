package com.bingo.app.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingo.app.mock.MockBingoData
import com.bingo.app.model.FatMonsterState
import com.bingo.app.model.MuscleBuddyState
import com.bingo.app.ui.BingoCard
import com.bingo.app.ui.CharacterAvatar
import com.bingo.app.ui.CircleIcon
import com.bingo.app.ui.FatMonsterView
import com.bingo.app.ui.MuscleBuddyView
import com.bingo.app.ui.RewardChip
import com.bingo.app.ui.ScreenList
import com.bingo.app.ui.SectionTitle
import com.bingo.app.ui.theme.AppColors

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
private fun AchievementBadge(title: String, badge: String, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        CircleIcon(badge, AppColors.LightGreen, AppColors.GrowthGreen, 54.dp)
        Text(title, color = AppColors.TextNavy, fontSize = 11.sp, textAlign = TextAlign.Center, lineHeight = 14.sp)
    }
}

@Composable
internal fun MiniFriendIllustration() {
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
private fun TrophyIllustration(modifier: Modifier) {
    Canvas(modifier = modifier.clip(RoundedCornerShape(22.dp)).background(AppColors.LightOrange)) {
        val w = size.width
        val h = size.height
        drawRect(Color(0xFFFFC58E), topLeft = Offset(w * 0.28f, h * 0.68f), size = Size(w * 0.44f, h * 0.18f))
        drawOval(AppColors.EnergyYellow, topLeft = Offset(w * 0.28f, h * 0.16f), size = Size(w * 0.44f, h * 0.38f))
        drawRect(AppColors.EnergyYellow, topLeft = Offset(w * 0.44f, h * 0.5f), size = Size(w * 0.12f, h * 0.2f))
        drawCircle(Color.White.copy(alpha = 0.5f), radius = w * 0.09f, center = Offset(w * 0.5f, h * 0.35f))
        drawCircle(AppColors.PrimaryOrange, radius = w * 0.035f, center = Offset(w * 0.5f, h * 0.35f))
        repeat(8) { i -> drawCircle(listOf(AppColors.PrimaryOrange, AppColors.GrowthGreen, AppColors.EnergyYellow)[i % 3], 4f, Offset((i + 1) * w / 9, h * (0.1f + (i % 2) * 0.18f))) }
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
