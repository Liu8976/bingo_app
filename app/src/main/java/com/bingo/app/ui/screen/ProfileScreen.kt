package com.bingo.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingo.app.mock.MockBingoData
import com.bingo.app.model.MuscleBuddyState
import com.bingo.app.ui.BingoCard
import com.bingo.app.ui.CharacterAvatar
import com.bingo.app.ui.CircleIcon
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
                SectionTitle(stringResource(com.bingo.app.R.string.profile_titles))
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
                SectionTitle(stringResource(com.bingo.app.R.string.profile_achievements))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    MockBingoData.achievements.take(4).forEachIndexed { index, achievement ->
                        AchievementBadge(achievement.first, MockBingoData.achievementBadges[index], Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeroCard() {
    val profile = MockBingoData.profileHero
    BingoCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CharacterAvatar(MuscleBuddyState.Ready, size = 72.dp)
                Text(profile.displayName, color = AppColors.TextNavy, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Text(profile.levelLabel, color = AppColors.TextNavy, fontWeight = FontWeight.SemiBold)
                LinearProgressIndicator(
                    progress = { profile.currentXp.toFloat() / profile.targetXp.coerceAtLeast(1) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(99.dp)),
                    color = AppColors.PrimaryOrange,
                    trackColor = AppColors.LightOrange
                )
                Text("${profile.currentXp} / ${profile.targetXp} XP", color = AppColors.TextSecondary, fontSize = 12.sp)
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
