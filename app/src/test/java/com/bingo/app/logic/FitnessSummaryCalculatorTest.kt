package com.bingo.app.logic

import com.bingo.app.model.ExerciseRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FitnessSummaryCalculatorTest {
    private val today = 20_000L

    @Test
    fun completedTasksComeFromIndependentInputs() {
        val summary = FitnessSummaryCalculator.calculate(
            exerciseMinutes = 30,
            hasFoodLog = true,
            hasWeightLog = false,
            waterCups = 8,
            exerciseHistory = listOf(ExerciseRecord(today, 30, 240)),
            todayEpochDay = today
        )

        assertEquals(3, summary.completedTaskCount)
        assertTrue(summary.hasFoodLog)
        assertFalse(summary.hasWeightLog)
        assertEquals(240, summary.caloriesBurned)
    }

    @Test
    fun unsafeValuesAreClamped() {
        val summary = FitnessSummaryCalculator.calculate(
            exerciseMinutes = -5,
            hasFoodLog = false,
            hasWeightLog = false,
            waterCups = 12,
            todayEpochDay = today
        )

        assertEquals(0, summary.exerciseMinutes)
        assertEquals(8, summary.waterCups)
        assertEquals(1, summary.completedTaskCount)
    }

    @Test
    fun streakUsesConsecutiveExerciseDays() {
        val summary = FitnessSummaryCalculator.calculate(
            exerciseMinutes = 10,
            hasFoodLog = false,
            hasWeightLog = false,
            waterCups = 0,
            exerciseHistory = listOf(
                ExerciseRecord(today - 3, 20, 160),
                ExerciseRecord(today - 2, 20, 160),
                ExerciseRecord(today - 1, 20, 160),
                ExerciseRecord(today, 10, 80)
            ),
            todayEpochDay = today
        )

        assertEquals(4, summary.streakDays)
    }
}
