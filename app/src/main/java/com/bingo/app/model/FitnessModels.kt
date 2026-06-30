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
    val streakDays: Int
)

enum class FatMonsterState {
    Lazy,
    Teasing,
    Nervous,
    Weakened,
    Defeated
}

enum class MuscleBuddyState {
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

enum class CharacterAssetKey(val resourceName: String) {
    FatMonsterLazy("fat_monster_lazy"),
    FatMonsterTeasing("fat_monster_teasing"),
    FatMonsterNervous("fat_monster_nervous"),
    FatMonsterWeakened("fat_monster_weakened"),
    FatMonsterDefeated("fat_monster_defeated"),
    MuscleBuddySleepy("muscle_buddy_sleepy"),
    MuscleBuddyReady("muscle_buddy_ready"),
    MuscleBuddyActive("muscle_buddy_active"),
    MuscleBuddyPowered("muscle_buddy_powered"),
    MuscleBuddyVictory("muscle_buddy_victory")
}
