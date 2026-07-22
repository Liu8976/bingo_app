package com.bingo.app.model

data class TodayFitnessSummary(
    val exerciseMinutes: Int,
    val caloriesBurned: Int,
    val targetExerciseMinutes: Int = 30,
    val targetCalories: Int = 300,
    val completedTaskCount: Int,
    val totalTaskCount: Int,
    val hasFoodLog: Boolean,
    val hasWeightLog: Boolean,
    val waterCups: Int,
    val streakDays: Int
)

data class WeightRecord(
    val epochDay: Long,
    val weightKg: Double
)

data class ExerciseRecord(
    val epochDay: Long,
    val durationMinutes: Int,
    val caloriesBurned: Int
)

// 身体档案只包含已经记录的数据；没有记录时由 UI 展示明确的空状态。
data class BodyRecordsSummary(
    val weightKg: Double? = null,
    val weightChangeWeekKg: Double? = null,
    val weightHistory: List<WeightRecord> = emptyList(),
    val foodIntakeKcal: Int? = null,
    val foodTargetKcal: Int = 1700,
    val foodRemainingKcal: Int? = null,
    val exerciseMinutes: Int = 0,
    val exerciseCaloriesBurned: Int = 0,
    val exerciseHistory: List<ExerciseRecord> = emptyList()
)

data class TrainingOption(
    val id: String,
    val title: String,
    val subtitle: String,
    val durationMinutes: Int,
    val intensity: String,
    val estimatedCalories: Int
)

enum class FatMonsterState {
    Normal,
    Lazy,
    Teasing,
    Nervous,
    Weakened,
    Defeated
}

enum class MuscleBuddyState {
    Normal,
    Sleepy,
    Ready,
    Active,
    Powered,
    Victory
}

data class CharacterBattleState(
    val fatMonsterState: FatMonsterState,
    val muscleBuddyState: MuscleBuddyState,
    val fatMonsterHealthPercent: Int,
    val muscleGrowthValue: Int,
    val fatMonsterBubbleText: String,
    val muscleBuddyBubbleText: String,
    val battleTitle: String,
    val battleSummary: String,
    val primaryButtonText: String,
    val reminderText: String,
    val progressPercent: Float,
    val isGoalCompleted: Boolean
)
