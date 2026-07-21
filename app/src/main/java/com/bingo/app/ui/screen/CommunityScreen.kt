package com.bingo.app.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import com.bingo.app.ui.PillButton
import com.bingo.app.ui.RewardChip
import com.bingo.app.ui.ScreenList
import com.bingo.app.ui.SpeechBubble
import com.bingo.app.ui.theme.AppColors

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
            Text("❤ $likes", color = if (imageType == 0) AppColors.PrimaryOrange else AppColors.GrowthGreen)
            Text("♡ $comments", color = AppColors.TextSecondary)
            Text("▱ 收藏", color = AppColors.TextSecondary)
            Text("☻ 同款翻车", color = AppColors.TextSecondary)
        }
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
                repeat(8) { i -> drawCircle(AppColors.GrowthGreen, radius = 16f, center = Offset(size.width * (0.18f + i * 0.07f), size.height * (0.62f - (i % 2) * 0.22f))) }
            }
        }
        if (imageType == 0) FatMonsterView(FatMonsterState.Nervous, modifier = Modifier.align(Alignment.BottomEnd).size(96.dp))
        else MuscleBuddyView(MuscleBuddyState.Victory, modifier = Modifier.align(Alignment.BottomStart).size(82.dp))
    }
}
