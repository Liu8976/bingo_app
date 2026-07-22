package com.bingo.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingo.app.R
import com.bingo.app.ui.theme.AppColors

@Composable
internal fun PageHeader(
    title: String,
    subtitle: String,
    trailing: @Composable BoxScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            BingoLogo()
            Text(title, color = AppColors.TextNavy, fontSize = 21.sp, lineHeight = 27.sp, fontWeight = FontWeight.Black)
            HomeGoalText(subtitle)
        }
        Box(modifier = Modifier.padding(start = 12.dp), contentAlignment = Alignment.TopEnd, content = trailing)
    }
}

@Composable
internal fun ReminderCard(title: String = "今日提醒：", text: String) {
    BingoCard(contentPadding = 12.dp, radius = 22.dp) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Image(
                painter = painterResource(R.drawable.tip_fat_icon),
                contentDescription = null,
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
