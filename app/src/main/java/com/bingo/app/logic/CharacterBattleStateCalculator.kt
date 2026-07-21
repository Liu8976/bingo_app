package com.bingo.app.logic

import com.bingo.app.model.CharacterBattleState
import com.bingo.app.model.FatMonsterState
import com.bingo.app.model.MuscleBuddyState
import com.bingo.app.model.TodayFitnessSummary
import kotlin.math.min

object CharacterBattleStateCalculator {
    fun calculate(summary: TodayFitnessSummary): CharacterBattleState {
        val progress = min(
            1f,
            summary.exerciseMinutes.toFloat() / summary.targetExerciseMinutes.coerceAtLeast(1)
        )
        val isGoalCompleted = summary.exerciseMinutes >= summary.targetExerciseMinutes

        return when (summary.exerciseMinutes) {
            0 -> CharacterBattleState(
                fatMonsterState = FatMonsterState.Normal,
                muscleBuddyState = MuscleBuddyState.Normal,
                fatMonsterHealthPercent = 100,
                muscleGrowthValue = 0,
                fatMonsterBubbleText = "今天不动？那我可就住下了。",
                muscleBuddyBubbleText = "别急，先动5分钟也算开局。",
                battleTitle = "脂肪怪正在嚣张",
                battleSummary = "今天还没开始运动，先完成一个小目标。",
                primaryButtonText = "开始今日反击",
                reminderText = "不用很猛，先动一下。Bingo 会把每一步都算数。",
                progressPercent = progress,
                isGoalCompleted = isGoalCompleted
            )

            in 1..14 -> CharacterBattleState(
                fatMonsterState = FatMonsterState.Teasing,
                muscleBuddyState = MuscleBuddyState.Ready,
                fatMonsterHealthPercent = 85,
                muscleGrowthValue = 5,
                fatMonsterBubbleText = "才刚开始？我先不慌。",
                muscleBuddyBubbleText = "已经开局了，继续一点点。",
                battleTitle = "反击刚刚开始",
                battleSummary = "你已经动起来了，再坚持一会儿效果更明显。",
                primaryButtonText = "继续训练",
                reminderText = "小动作也算数，别让脂肪怪笑太久。",
                progressPercent = progress,
                isGoalCompleted = isGoalCompleted
            )

            in 15..29 -> CharacterBattleState(
                fatMonsterState = FatMonsterState.Nervous,
                muscleBuddyState = MuscleBuddyState.Active,
                fatMonsterHealthPercent = 65,
                muscleGrowthValue = 12,
                fatMonsterBubbleText = "等一下，你今天有点认真。",
                muscleBuddyBubbleText = "继续，快接近今日目标了。",
                battleTitle = "脂肪怪开始慌了",
                battleSummary = "距离今日目标已经不远，再补一点就能完成。",
                primaryButtonText = "冲完今日目标",
                reminderText = "还差一点点，今天很适合 Bingo。",
                progressPercent = progress,
                isGoalCompleted = isGoalCompleted
            )

            in 30..44 -> CharacterBattleState(
                fatMonsterState = FatMonsterState.Weakened,
                muscleBuddyState = MuscleBuddyState.Powered,
                fatMonsterHealthPercent = 40,
                muscleGrowthValue = 18,
                fatMonsterBubbleText = "我承认，今天你有点东西。",
                muscleBuddyBubbleText = "目标完成，身体已经收到信号。",
                battleTitle = "今日目标已完成",
                battleSummary = "你完成了今日运动目标，脂肪怪已经开始虚弱。",
                primaryButtonText = "查看今日战报",
                reminderText = "今天动了，就算赢。晚上别和夜宵谈恋爱，脂肪怪会吃醋。",
                progressPercent = progress,
                isGoalCompleted = isGoalCompleted
            )

            else -> CharacterBattleState(
                fatMonsterState = FatMonsterState.Defeated,
                muscleBuddyState = MuscleBuddyState.Victory,
                fatMonsterHealthPercent = 20,
                muscleGrowthValue = 25,
                fatMonsterBubbleText = "今天先撤，我明天再来。",
                muscleBuddyBubbleText = "漂亮，今天是高质量反击。",
                battleTitle = "脂肪怪今日撤退",
                battleSummary = "你已经明显超过今日目标，注意补水和恢复。",
                primaryButtonText = "生成燃脂战报",
                reminderText = "强度不错，恢复也要安排上。",
                progressPercent = progress,
                isGoalCompleted = isGoalCompleted
            )
        }
    }
}
