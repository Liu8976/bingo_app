package com.bingo.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingo.app.R
import com.bingo.app.model.FatMonsterState
import com.bingo.app.model.MuscleBuddyState
import com.bingo.app.ui.theme.AppColors
import kotlin.math.roundToInt

enum class BingoTab(
    val title: String,
    val selectedIconRes: Int,
    val unselectedIconRes: Int
) {
    Today("今日", R.drawable.nav_today_selected, R.drawable.nav_today_unselected),
    Training("训练", R.drawable.nav_training_selected, R.drawable.nav_training_unselected),
    Records("记录", R.drawable.nav_records_selected, R.drawable.nav_records_unselected),
    Community("广场", R.drawable.nav_community_selected, R.drawable.nav_community_unselected),
    Profile("我的", R.drawable.nav_profile_selected, R.drawable.nav_profile_unselected)
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
                    Image(
                        painter = painterResource(
                            if (selected) tab.selectedIconRes else tab.unselectedIconRes
                        ),
                        contentDescription = tab.title,
                        modifier = Modifier.size(22.dp),
                        contentScale = ContentScale.Fit
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
internal fun ScreenList(content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) {
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
internal fun BingoCard(
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
internal fun SectionTitle(title: String, action: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(AppColors.PrimaryOrange))
            Text(title, color = AppColors.TextNavy, fontSize = 18.sp, fontWeight = FontWeight.Black)
        }
        if (action != null) Text(action, color = AppColors.TextSecondary, fontSize = 12.sp)
    }
}

@Composable
internal fun HomeGoalText(text: String) {
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

@Composable
internal fun PillButton(text: String, selected: Boolean, onClick: () -> Unit) {
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
internal fun RewardChip(text: String, color: Color, textColor: Color = AppColors.TextNavy, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(99.dp))
            .background(color)
            .padding(horizontal = 12.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
internal fun CircleIcon(text: String, background: Color, textColor: Color = AppColors.TextNavy, size: Dp = 44.dp) {
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
internal fun ImageCircleIcon(iconResId: Int, contentDescription: String, size: Dp = 44.dp) {
    Image(
        painter = painterResource(iconResId),
        contentDescription = contentDescription,
        modifier = Modifier.size(size),
        contentScale = ContentScale.Fit
    )
}

@Composable
internal fun SpeechBubble(
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
        textAlign = TextAlign.Start
    )
}

@Composable
internal fun CharacterAvatar(state: MuscleBuddyState, size: Dp = 58.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(AppColors.SoftBlue)
            .border(1.dp, AppColors.GrowthGreen, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        MuscleBuddyView(state, modifier = Modifier.size(size * 0.92f))
    }
}

@Composable
internal fun FatMonsterView(state: FatMonsterState, modifier: Modifier = Modifier.size(108.dp)) {
    CharacterImage(
        resourceId = state.drawableResId(),
        contentDescription = "Fat monster ${state.name.lowercase()} state",
        modifier = modifier
    )
}

@Composable
internal fun MuscleBuddyView(state: MuscleBuddyState, modifier: Modifier = Modifier.size(108.dp)) {
    CharacterImage(
        resourceId = state.drawableResId(),
        contentDescription = "Muscle buddy ${state.name.lowercase()} state",
        modifier = modifier
    )
}

@Composable
internal fun CharacterImage(resourceId: Int, contentDescription: String, modifier: Modifier) {
    Image(
        painter = painterResource(resourceId),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

internal fun FatMonsterState.drawableResId(): Int = when (this) {
    FatMonsterState.Normal -> R.drawable.fat_monster_normal
    FatMonsterState.Lazy -> R.drawable.fat_monster_lazy
    FatMonsterState.Teasing -> R.drawable.fat_monster_teasing
    FatMonsterState.Nervous -> R.drawable.fat_monster_nervous
    FatMonsterState.Weakened -> R.drawable.fat_monster_weakened
    FatMonsterState.Defeated -> R.drawable.fat_monster_defeated
}

internal fun MuscleBuddyState.drawableResId(): Int = when (this) {
    MuscleBuddyState.Normal -> R.drawable.muscle_buddy_normal
    MuscleBuddyState.Sleepy -> R.drawable.muscle_buddy_sleepy
    MuscleBuddyState.Ready -> R.drawable.muscle_buddy_ready
    MuscleBuddyState.Active -> R.drawable.muscle_buddy_active
    MuscleBuddyState.Powered -> R.drawable.muscle_buddy_powered
    MuscleBuddyState.Victory -> R.drawable.muscle_buddy_victory
}

@Composable
internal fun BingoLogo(color: Color = AppColors.PrimaryOrange) {
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
internal fun PrimaryGradientButton(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(58.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.horizontalGradient(listOf(AppColors.OrangeDeep, AppColors.PrimaryOrange))),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
internal fun OutlineOrangeButton(text: String, onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .border(1.5.dp, AppColors.PrimaryOrange, RoundedCornerShape(22.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = AppColors.PrimaryOrange, fontWeight = FontWeight.Black)
    }
}

@Composable
internal fun MetricPill(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
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
internal fun TaskScrollIndicator(scrollState: ScrollState, modifier: Modifier = Modifier) {
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
internal fun BattleImageBubble(
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
