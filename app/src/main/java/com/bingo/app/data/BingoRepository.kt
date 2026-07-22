package com.bingo.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bingo.app.logic.FitnessSummaryCalculator
import com.bingo.app.model.BodyRecordsSummary
import com.bingo.app.model.ExerciseRecord
import com.bingo.app.model.TodayFitnessSummary
import com.bingo.app.model.WeightRecord
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.bingoDataStore: DataStore<Preferences> by preferencesDataStore(name = "bingo_today")

/** 本地离线 MVP 的唯一数据来源。所有按日数据都带日期，跨天后不会继续显示为“今日”。 */
class BingoRepository(private val context: Context) {

    private object Keys {
        val EXERCISE_MINUTES = intPreferencesKey("exercise_minutes")
        val EXERCISE_DATE = longPreferencesKey("exercise_date")
        val EXERCISE_HISTORY = stringPreferencesKey("exercise_history")
        val WEIGHT_KG = doublePreferencesKey("weight_kg")
        val WEIGHT_HISTORY = stringPreferencesKey("weight_history")
        val FOOD_INTAKE_KCAL = intPreferencesKey("food_intake_kcal")
        val FOOD_DATE = longPreferencesKey("food_date")
        val FOOD_TARGET_KCAL = intPreferencesKey("food_target_kcal")
        val WATER_CUPS = intPreferencesKey("water_cups")
        val WATER_DATE = longPreferencesKey("water_date")
    }

    private companion object {
        const val DEFAULT_FOOD_TARGET_KCAL = 1700
        const val MAX_HISTORY_RECORDS = 180
    }

    val todaySummary: Flow<TodayFitnessSummary> = context.bingoDataStore.data.map { prefs ->
        val today = LocalDate.now().toEpochDay()
        val minutes = prefs.todayExerciseMinutes(today)
        val exerciseHistory = prefs.exerciseHistoryWithToday(today, minutes)
        val weightHistory = decodeWeightHistory(prefs[Keys.WEIGHT_HISTORY])
        FitnessSummaryCalculator.calculate(
            exerciseMinutes = minutes,
            hasFoodLog = prefs[Keys.FOOD_DATE] == today,
            hasWeightLog = weightHistory.any { it.epochDay == today },
            waterCups = if (prefs[Keys.WATER_DATE] == today) prefs[Keys.WATER_CUPS] ?: 0 else 0,
            exerciseHistory = exerciseHistory,
            todayEpochDay = today
        )
    }

    val bodyRecords: Flow<BodyRecordsSummary> = context.bingoDataStore.data.map { prefs ->
        val today = LocalDate.now().toEpochDay()
        val minutes = prefs.todayExerciseMinutes(today)
        val exerciseHistory = prefs.exerciseHistoryWithToday(today, minutes)
        val weightHistory = decodeWeightHistory(prefs[Keys.WEIGHT_HISTORY]).sortedBy { it.epochDay }
        val recentWeights = weightHistory.filter { it.epochDay >= today - 6 }
        val currentWeight = weightHistory.lastOrNull()?.weightKg ?: prefs[Keys.WEIGHT_KG]
        val weightChange = if (recentWeights.size >= 2) {
            recentWeights.last().weightKg - recentWeights.first().weightKg
        } else {
            null
        }
        val foodIntake = if (prefs[Keys.FOOD_DATE] == today) prefs[Keys.FOOD_INTAKE_KCAL] else null
        val foodTarget = prefs[Keys.FOOD_TARGET_KCAL] ?: DEFAULT_FOOD_TARGET_KCAL
        BodyRecordsSummary(
            weightKg = currentWeight,
            weightChangeWeekKg = weightChange,
            weightHistory = weightHistory,
            foodIntakeKcal = foodIntake,
            foodTargetKcal = foodTarget,
            foodRemainingKcal = foodIntake?.let { foodTarget - it },
            exerciseMinutes = minutes,
            exerciseCaloriesBurned = minutes * FitnessSummaryCalculator.CALORIES_PER_MINUTE,
            exerciseHistory = exerciseHistory
        )
    }

    /** Debug 工具使用：替换今天的运动记录。 */
    suspend fun setExerciseMinutes(minutes: Int) {
        val safeMinutes = minutes.coerceAtLeast(0)
        val today = LocalDate.now().toEpochDay()
        context.bingoDataStore.edit { prefs ->
            prefs[Keys.EXERCISE_MINUTES] = safeMinutes
            prefs[Keys.EXERCISE_DATE] = today
            val otherDays = decodeExerciseHistory(prefs[Keys.EXERCISE_HISTORY])
                .filterNot { it.epochDay == today }
            val updated = if (safeMinutes > 0) {
                otherDays + ExerciseRecord(
                    epochDay = today,
                    durationMinutes = safeMinutes,
                    caloriesBurned = safeMinutes * FitnessSummaryCalculator.CALORIES_PER_MINUTE
                )
            } else {
                otherDays
            }
            prefs[Keys.EXERCISE_HISTORY] = encodeExerciseHistory(updated)
        }
    }

    suspend fun completeTraining(durationMinutes: Int, caloriesBurned: Int) {
        val safeMinutes = durationMinutes.coerceAtLeast(1)
        val safeCalories = caloriesBurned.coerceAtLeast(0)
        val today = LocalDate.now().toEpochDay()
        context.bingoDataStore.edit { prefs ->
            val previousMinutes = prefs.todayExerciseMinutes(today)
            prefs[Keys.EXERCISE_MINUTES] = previousMinutes + safeMinutes
            prefs[Keys.EXERCISE_DATE] = today
            val updated = decodeExerciseHistory(prefs[Keys.EXERCISE_HISTORY]) + ExerciseRecord(
                epochDay = today,
                durationMinutes = safeMinutes,
                caloriesBurned = safeCalories
            )
            prefs[Keys.EXERCISE_HISTORY] = encodeExerciseHistory(updated.takeLast(MAX_HISTORY_RECORDS))
        }
    }

    suspend fun logWeight(weightKg: Double) {
        require(weightKg in 20.0..400.0)
        val today = LocalDate.now().toEpochDay()
        context.bingoDataStore.edit { prefs ->
            prefs[Keys.WEIGHT_KG] = weightKg
            val updated = decodeWeightHistory(prefs[Keys.WEIGHT_HISTORY])
                .filterNot { it.epochDay == today } + WeightRecord(today, weightKg)
            prefs[Keys.WEIGHT_HISTORY] = encodeWeightHistory(updated.takeLast(MAX_HISTORY_RECORDS))
        }
    }

    suspend fun logFoodIntake(calories: Int) {
        require(calories in 0..10_000)
        val today = LocalDate.now().toEpochDay()
        context.bingoDataStore.edit { prefs ->
            prefs[Keys.FOOD_INTAKE_KCAL] = calories
            prefs[Keys.FOOD_DATE] = today
        }
    }

    suspend fun addWaterCup() {
        val today = LocalDate.now().toEpochDay()
        context.bingoDataStore.edit { prefs ->
            val current = if (prefs[Keys.WATER_DATE] == today) prefs[Keys.WATER_CUPS] ?: 0 else 0
            prefs[Keys.WATER_CUPS] = (current + 1).coerceAtMost(FitnessSummaryCalculator.TARGET_WATER_CUPS)
            prefs[Keys.WATER_DATE] = today
        }
    }

    private fun Preferences.todayExerciseMinutes(today: Long): Int =
        if (this[Keys.EXERCISE_DATE] == today) (this[Keys.EXERCISE_MINUTES] ?: 0).coerceAtLeast(0) else 0

    private fun Preferences.exerciseHistoryWithToday(today: Long, minutes: Int): List<ExerciseRecord> {
        val history = decodeExerciseHistory(this[Keys.EXERCISE_HISTORY])
        if (minutes <= 0 || history.any { it.epochDay == today }) return history
        return history + ExerciseRecord(
            epochDay = today,
            durationMinutes = minutes,
            caloriesBurned = minutes * FitnessSummaryCalculator.CALORIES_PER_MINUTE
        )
    }

    private fun decodeWeightHistory(raw: String?): List<WeightRecord> = raw.orEmpty()
        .split(';')
        .mapNotNull { item ->
            val parts = item.split(',')
            val day = parts.getOrNull(0)?.toLongOrNull()
            val weight = parts.getOrNull(1)?.toDoubleOrNull()
            if (day != null && weight != null) WeightRecord(day, weight) else null
        }

    private fun encodeWeightHistory(records: List<WeightRecord>): String = records
        .sortedBy { it.epochDay }
        .joinToString(";") { "${it.epochDay},${it.weightKg}" }

    private fun decodeExerciseHistory(raw: String?): List<ExerciseRecord> = raw.orEmpty()
        .split(';')
        .mapNotNull { item ->
            val parts = item.split(',')
            val day = parts.getOrNull(0)?.toLongOrNull()
            val minutes = parts.getOrNull(1)?.toIntOrNull()
            val calories = parts.getOrNull(2)?.toIntOrNull()
            if (day != null && minutes != null && calories != null) {
                ExerciseRecord(day, minutes, calories)
            } else {
                null
            }
        }

    private fun encodeExerciseHistory(records: List<ExerciseRecord>): String = records
        .sortedBy { it.epochDay }
        .joinToString(";") { "${it.epochDay},${it.durationMinutes},${it.caloriesBurned}" }
}
