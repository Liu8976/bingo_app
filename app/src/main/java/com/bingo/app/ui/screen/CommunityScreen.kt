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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingo.app.R
import com.bingo.app.mock.MockBingoData
import com.bingo.app.mock.CommunityPost
import com.bingo.app.model.FatMonsterState
import com.bingo.app.model.MuscleBuddyState
import com.bingo.app.ui.BingoCard
import com.bingo.app.ui.CharacterAvatar
import com.bingo.app.ui.FatMonsterView
import com.bingo.app.ui.MuscleBuddyView
import com.bingo.app.ui.PillButton
import com.bingo.app.ui.PageHeader
import com.bingo.app.ui.RewardChip
import com.bingo.app.ui.ScreenList
import com.bingo.app.ui.SpeechBubble
import com.bingo.app.ui.theme.AppColors

@Composable
fun CommunityScreen() {
    var selectedCategory by rememberSaveable { mutableStateOf(MockBingoData.communityTabs.first()) }
    val visiblePosts = MockBingoData.communityPosts.filter { it.tag == selectedCategory }

    ScreenList {
        item {
            PageHeader(
                title = stringResource(R.string.community_title),
                subtitle = stringResource(R.string.community_subtitle),
                trailing = { CharacterAvatar(MuscleBuddyState.Active, size = 58.dp) }
            )
        }
        item { CommunityCategoryTabs(selectedCategory, onSelected = { selectedCategory = it }) }
        if (visiblePosts.isEmpty()) {
            item {
                BingoCard {
                    Text("这个分类还没有内容，稍后再来看看。", color = AppColors.TextSecondary)
                }
            }
        } else {
            items(visiblePosts.size) { index ->
                CommunityPostCard(visiblePosts[index])
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CommunityCategoryTabs(selectedCategory: String, onSelected: (String) -> Unit) {
    val tabs = MockBingoData.communityTabs
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        tabs.forEach { tab ->
            PillButton(text = tab, selected = tab == selectedCategory, onClick = { onSelected(tab) })
        }
    }
}

@Composable
private fun CommunityPostCard(post: CommunityPost) {
    BingoCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CharacterAvatar(if (post.imageType == 0) MuscleBuddyState.Ready else MuscleBuddyState.Active, size = 48.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(post.name, color = AppColors.TextNavy, fontWeight = FontWeight.Black)
                Text(post.time, color = AppColors.TextSecondary, fontSize = 12.sp)
            }
            RewardChip(post.tag, if (post.imageType == 0) AppColors.LightOrange else AppColors.LightGreen)
        }
        CommunityImagePlaceholder(post.imageType)
        Text(post.title, color = AppColors.TextNavy, fontSize = 20.sp, fontWeight = FontWeight.Black)
        Text(post.body, color = AppColors.TextSecondary, fontSize = 14.sp, lineHeight = 20.sp)
        SpeechBubble(post.reply, AppColors.LightOrange, AppColors.BorderWarm)
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("点赞 ${post.likes}", color = if (post.imageType == 0) AppColors.PrimaryOrange else AppColors.GrowthGreen)
            Text("评论 ${post.comments}", color = AppColors.TextSecondary)
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
