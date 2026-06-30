package com.bingo.app.mock

import com.bingo.app.model.TodayFitnessSummary

object MockBingoData {
    val debugMinutes = listOf(0, 10, 25, 35, 50)

    fun todaySummary(minutes: Int): TodayFitnessSummary {
        val safeMinutes = minutes.coerceAtLeast(0)
        return TodayFitnessSummary(
            exerciseMinutes = safeMinutes,
            caloriesBurned = safeMinutes * 8,
            completedTaskCount = when {
                safeMinutes >= 45 -> 5
                safeMinutes >= 30 -> 4
                safeMinutes >= 15 -> 3
                safeMinutes > 0 -> 2
                else -> 1
            },
            totalTaskCount = 5,
            hasFoodLog = safeMinutes >= 10,
            hasWeightLog = true,
            streakDays = 6
        )
    }

    val trainingTypes = listOf(
        "户外跑" to "出门跑路",
        "室内燃脂" to "客厅开战",
        "力量训练" to "肌肉伙伴加班",
        "拉伸恢复" to "给身体充电",
        "核心训练" to "腹肌观察计划",
        "快速训练" to "没时间也动一下"
    )

    val communityTabs = listOf("放纵餐", "训练日餐", "好物分享", "训练打卡")

    val achievements = listOf(
        "7 天打卡" to "连续打卡 7 天",
        "消耗达人" to "完成高热量消耗日",
        "早起之星" to "上午完成训练",
        "坚持不演" to "放纵餐后仍完成运动",
        "跑路成功" to "完成第一次户外跑"
    )

    val titles = listOf("火锅幸存者", "奶茶抵抗者", "自律钉子户", "跑步跑路王", "肌肉伙伴饲养员")
}
