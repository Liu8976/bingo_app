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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingo.app.model.BodyRecordsSummary
import com.bingo.app.model.ExerciseRecord
import com.bingo.app.model.MuscleBuddyState
import com.bingo.app.model.WeightRecord
import com.bingo.app.ui.BingoCard
import com.bingo.app.ui.CharacterAvatar
import com.bingo.app.ui.MetricPill
import com.bingo.app.ui.OutlineOrangeButton
import com.bingo.app.ui.PillButton
import com.bingo.app.ui.PageHeader
import com.bingo.app.ui.ReminderCard
import com.bingo.app.ui.ScreenList
import com.bingo.app.ui.SectionTitle
import com.bingo.app.ui.theme.AppColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private enum class ExerciseRange(val label: String, val days: Int) {
    Day("今日", 1), Week("本周", 7), Month("本月", 30)
}

@Composable
fun RecordsScreen(
    records: BodyRecordsSummary,
    onLogWeight: (Double) -> Unit,
    onLogFoodIntake: (Int) -> Unit
) {
    var weightRangeDays by rememberSaveable { mutableIntStateOf(7) }
    var exerciseRange by rememberSaveable { mutableStateOf(ExerciseRange.Day) }
    var showWeightInput by rememberSaveable { mutableStateOf(false) }
    var showFoodInput by rememberSaveable { mutableStateOf(false) }

    ScreenList {
        item {
            PageHeader(
                title = stringResource(com.bingo.app.R.string.records_title),
                subtitle = stringResource(com.bingo.app.R.string.records_subtitle),
                trailing = { CharacterAvatar(MuscleBuddyState.Ready) }
            )
        }
        item {
            WeightTrendCard(
                records = records,
                rangeDays = weightRangeDays,
                onRangeSelected = { weightRangeDays = it },
                onAddWeight = { showWeightInput = true }
            )
        }
        item { FoodRecordCard(records, onAddFood = { showFoodInput = true }) }
        item {
            ExerciseRecordCard(
                records = records,
                selectedRange = exerciseRange,
                onRangeSelected = { exerciseRange = it }
            )
        }
        item { ReminderCard(title = "趋势提醒：", text = buildTrendReminder(records)) }
    }

    if (showWeightInput) {
        NumberInputDialog(
            title = "记录今日体重",
            label = "体重（kg）",
            initialValue = records.weightKg?.toString().orEmpty(),
            allowDecimal = true,
            validator = { it.toDoubleOrNull()?.let { value -> value in 20.0..400.0 } == true },
            onDismiss = { showWeightInput = false },
            onConfirm = {
                onLogWeight(it.toDouble())
                showWeightInput = false
            }
        )
    }
    if (showFoodInput) {
        NumberInputDialog(
            title = "记录今日饮食",
            label = "今日总摄入（kcal）",
            initialValue = records.foodIntakeKcal?.toString().orEmpty(),
            allowDecimal = false,
            validator = { it.toIntOrNull()?.let { value -> value in 0..10_000 } == true },
            onDismiss = { showFoodInput = false },
            onConfirm = {
                onLogFoodIntake(it.toInt())
                showFoodInput = false
            }
        )
    }
}

@Composable
private fun WeightTrendCard(
    records: BodyRecordsSummary,
    rangeDays: Int,
    onRangeSelected: (Int) -> Unit,
    onAddWeight: () -> Unit
) {
    val visibleRecords = records.weightHistory.weightRecordsInLastDays(rangeDays)
    val current = visibleRecords.lastOrNull()?.weightKg ?: records.weightKg
    val change = if (visibleRecords.size >= 2) visibleRecords.last().weightKg - visibleRecords.first().weightKg else null

    BingoCard {
        SectionTitle(stringResource(com.bingo.app.R.string.records_weight))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            listOf(7, 14, 30).forEach { days ->
                PillButton("${days}天", selected = rangeDays == days, onClick = { onRangeSelected(days) })
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MetricPill("当前体重", current?.let { "${formatOneDecimal(it)} kg" } ?: "未记录", AppColors.TextNavy, Modifier.weight(1f))
            MetricPill("周期变化", change?.let { "${formatSignedOneDecimal(it)} kg" } ?: "数据不足", AppColors.GrowthGreen, Modifier.weight(1f))
        }
        if (visibleRecords.isEmpty()) {
            EmptyRecordText("还没有体重记录，添加第一条记录后会生成真实趋势。")
        } else {
            WeightChart(visibleRecords)
        }
        OutlineOrangeButton("记录今日体重", onClick = onAddWeight)
    }
}

@Composable
private fun FoodRecordCard(records: BodyRecordsSummary, onAddFood: () -> Unit) {
    BingoCard {
        SectionTitle(stringResource(com.bingo.app.R.string.records_food))
        if (records.foodIntakeKcal == null) {
            EmptyRecordText("今天还没有饮食记录。")
        } else {
            val remaining = records.foodRemainingKcal ?: 0
            Text("今日总摄入 ${records.foodIntakeKcal} kcal", color = AppColors.TextNavy, fontWeight = FontWeight.Bold)
            Text(
                if (remaining >= 0) "距离目标还可摄入 $remaining kcal" else "已超过目标 ${-remaining} kcal",
                color = if (remaining >= 0) AppColors.TextSecondary else AppColors.AlertRed,
                fontSize = 13.sp
            )
        }
        OutlineOrangeButton(if (records.foodIntakeKcal == null) "记录今日饮食" else "更新今日饮食", onClick = onAddFood)
    }
}

@Composable
private fun ExerciseRecordCard(
    records: BodyRecordsSummary,
    selectedRange: ExerciseRange,
    onRangeSelected: (ExerciseRange) -> Unit
) {
    val selectedRecords = records.exerciseHistory.exerciseRecordsInLastDays(selectedRange.days)
    val minutes = if (selectedRange == ExerciseRange.Day) records.exerciseMinutes else selectedRecords.sumOf { it.durationMinutes }
    val calories = if (selectedRange == ExerciseRange.Day) records.exerciseCaloriesBurned else selectedRecords.sumOf { it.caloriesBurned }

    BingoCard {
        SectionTitle(stringResource(com.bingo.app.R.string.records_exercise))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            ExerciseRange.entries.forEach { range ->
                PillButton(range.label, selected = selectedRange == range, onClick = { onRangeSelected(range) })
            }
        }
        if (minutes == 0) {
            EmptyRecordText("${selectedRange.label}还没有运动记录，从训练页完成一次训练吧。")
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                MetricPill("运动时长", "$minutes 分钟", AppColors.GrowthGreen, Modifier.weight(1f))
                MetricPill("累计消耗", "$calories kcal", AppColors.PrimaryOrange, Modifier.weight(1f))
            }
            Text("共记录 ${selectedRecords.size.coerceAtLeast(1)} 次训练", color = AppColors.TextSecondary, fontSize = 13.sp)
        }
    }
}

private fun buildTrendReminder(records: BodyRecordsSummary): String = when {
    records.weightHistory.isEmpty() && records.exerciseHistory.isEmpty() -> "先完成一条真实记录，肌肉伙伴才能帮你判断趋势。"
    records.weightChangeWeekKg != null && records.weightChangeWeekKg < 0 -> "近 7 天体重趋势正在下降，继续保持稳定节奏。"
    records.exerciseHistory.exerciseRecordsInLastDays(7).size >= 3 -> "这周已经多次完成训练，稳定比偶尔拼命更重要。"
    else -> "数据正在积累中，不用盯住一天的波动。"
}

private fun List<WeightRecord>.weightRecordsInLastDays(days: Int): List<WeightRecord> {
    val firstDay = LocalDate.now().toEpochDay() - days + 1
    return filter { it.epochDay >= firstDay }.sortedBy { it.epochDay }
}

private fun List<ExerciseRecord>.exerciseRecordsInLastDays(days: Int): List<ExerciseRecord> {
    val firstDay = LocalDate.now().toEpochDay() - days + 1
    return filter { it.epochDay >= firstDay }.sortedBy { it.epochDay }
}

private fun formatOneDecimal(value: Double): String = String.format("%.1f", value)

private fun formatSignedOneDecimal(value: Double): String {
    val formatted = formatOneDecimal(value)
    return if (value > 0) "+$formatted" else formatted
}

@Composable
private fun WeightChart(records: List<WeightRecord>) {
    val minValue = records.minOf { it.weightKg }
    val maxValue = records.maxOf { it.weightKg }
    val valueRange = (maxValue - minValue).takeIf { it > 0.0 } ?: 1.0
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.Background)
            .padding(14.dp)
    ) {
        repeat(4) { index ->
            val y = size.height * index / 3f
            drawLine(AppColors.BorderWarm.copy(alpha = 0.7f), Offset(0f, y), Offset(size.width, y), 1.5f)
        }
        val denominator = (records.size - 1).coerceAtLeast(1)
        val points = records.mapIndexed { index, record ->
            val normalized = ((record.weightKg - minValue) / valueRange).toFloat()
            Offset(size.width * index / denominator, size.height * (1f - normalized))
        }
        for (index in 0 until points.lastIndex) {
            drawLine(AppColors.GrowthGreen, points[index], points[index + 1], strokeWidth = 5f)
        }
        points.forEach { point ->
            drawCircle(Color.White, radius = 8f, center = point)
            drawCircle(AppColors.GrowthGreen, radius = 5f, center = point)
        }
    }
    val formatter = DateTimeFormatter.ofPattern("M/d")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(LocalDate.ofEpochDay(records.first().epochDay).format(formatter), color = AppColors.TextSecondary, fontSize = 11.sp)
        Text(LocalDate.ofEpochDay(records.last().epochDay).format(formatter), color = AppColors.TextSecondary, fontSize = 11.sp)
    }
}

@Composable
private fun EmptyRecordText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.Background)
            .padding(14.dp),
        color = AppColors.TextSecondary,
        fontSize = 13.sp
    )
}

@Composable
private fun NumberInputDialog(
    title: String,
    label: String,
    initialValue: String,
    allowDecimal: Boolean,
    validator: (String) -> Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var value by rememberSaveable(title) { mutableStateOf(initialValue) }
    val valid = validator(value)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Black) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text(label) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (allowDecimal) KeyboardType.Decimal else KeyboardType.Number
                    ),
                    isError = value.isNotEmpty() && !valid
                )
                if (value.isNotEmpty() && !valid) {
                    Text("请输入有效数值。", color = AppColors.AlertRed, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { if (valid) onConfirm(value) }, enabled = valid) { Text(stringResource(com.bingo.app.R.string.records_save)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(com.bingo.app.R.string.cancel)) } }
    )
}
