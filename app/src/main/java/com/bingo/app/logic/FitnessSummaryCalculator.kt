package com.bingo.app.logic

import com.bingo.app.model.ExerciseRecord
import com.bingo.app.model.TodayFitnessSummary

object FitnessSummaryCalculator {
    fun calculate(
        exerciseMinutes: Int,
        hasFoodLog: Boolean,
        hasWeightLog: Boolean,
        waterCups: Int,
        exerciseHistory: List<ExerciseRecord> = emptyList(),
        todayEpochDay: Long
    ): TodayFitnessSummary {
        val safeMinutes = exerciseMinutes.coerceAtLeast(0)
        val safeWaterCups = waterCups.coerceIn(0, 8)
        val caloriesBurned = safeMinutes * CALORIES_PER_MINUTE
        val completedTaskCount = listOf(
            safeMinutes >= TARGET_EXERCISE_MINUTES,
            hasFoodLog,
            safeWaterCups >= TARGET_WATER_CUPS,
            hasWeightLog,
            caloriesBurned >= TARGET_CALORIES
        ).count { it }

        return TodayFitnessSummary(
            exerciseMinutes = safeMinutes,
            caloriesBurned = caloriesBurned,
            completedTaskCount = completedTaskCount,
            totalTaskCount = TOTAL_TASK_COUNT,
            hasFoodLog = hasFoodLog,
            hasWeightLog = hasWeightLog,
            waterCups = safeWaterCups,
            streakDays = calculateStreak(exerciseHistory, todayEpochDay)
        )
    }

    private fun calculateStreak(records: List<ExerciseRecord>, todayEpochDay: Long): Int {
        val completedDays = records
            .filter { it.durationMinutes > 0 }
            .map { it.epochDay }
            .toSet()
        val startDay = if (todayEpochDay in completedDays) todayEpochDay else todayEpochDay - 1
        return generateSequence(startDay) { it - 1 }
            .takeWhile { it in completedDays }
            .count()
    }

    const val TARGET_EXERCISE_MINUTES = 30
    const val TARGET_CALORIES = 300
    const val TARGET_WATER_CUPS = 8
    const val TOTAL_TASK_COUNT = 5
    const val CALORIES_PER_MINUTE = 8
}
