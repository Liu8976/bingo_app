package com.bingo.app.mock

import com.bingo.app.logic.FitnessSummaryCalculator
import com.bingo.app.model.ExerciseRecord
import com.bingo.app.model.FollowAlongVideo
import com.bingo.app.model.StrengthBodyArea
import com.bingo.app.model.TodayFitnessSummary
import com.bingo.app.model.TrainingOption
import java.time.LocalDate

/**
 * 演示/占位数据的集中存放处。
 *
 * 这里的内容分两类：
 * 1. 用户真实会变化的数据（体重、连续天数、运动分钟等）——已经迁移到
 *    [com.bingo.app.data.BingoRepository]，不再从这里读取。
 * 2. 展示型静态内容（排行榜他人数据、广场帖子、称号文案等）——本来就应该来自
 *    后端接口，目前接口未就绪，先集中在这里占位，方便未来整体替换为网络请求，
 *    也避免像之前那样散落在各个 Composable 函数里手写字面量。
 */
object MockBingoData {
    val debugMinutes = listOf(0, 10, 25, 35, 50)

    fun todaySummary(minutes: Int): TodayFitnessSummary {
        val safeMinutes = minutes.coerceAtLeast(0)
        val today = LocalDate.now().toEpochDay()
        val history = if (safeMinutes > 0) {
            listOf(ExerciseRecord(today, safeMinutes, safeMinutes * FitnessSummaryCalculator.CALORIES_PER_MINUTE))
        } else {
            emptyList()
        }
        return FitnessSummaryCalculator.calculate(
            exerciseMinutes = safeMinutes,
            hasFoodLog = safeMinutes >= 10,
            hasWeightLog = true,
            waterCups = if (safeMinutes >= 15) 8 else 0,
            exerciseHistory = history,
            todayEpochDay = today
        )
    }

    val trainingTypes = listOf(
        TrainingOption("outdoor", "户外训练", "出门跑路", 30, "中等强度", 240),
        TrainingOption("burn", "室内燃脂", "客厅开战", 25, "中高强度", 200),
        TrainingOption("strength", "力量训练", "肌肉在加班", 35, "中等强度", 280),
        TrainingOption("stretch", "热身拉伸", "身体要激活也要放松", 15, "低强度", 120),
        TrainingOption("core", "核心训练", "腹肌小宝贝在等你", 20, "中等强度", 160),
        TrainingOption("quick", "快速训练", "让脂肪排队下岗", 10, "中等强度", 80)
    )

    val recommendedTraining = trainingTypes[1]

    val strengthBodyAreas = listOf(
        StrengthBodyArea(
            id = "arms",
            title = "手臂",
            subtitle = "肱二头肌、肱三头肌与前臂",
            focus = "改善手臂力量和线条",
            videos = listOf(
                FollowAlongVideo("arms_curl", "站姿弯举节奏练", 45, "入门", "手肘贴近身体，收紧核心。", listOf("双臂自然下垂", "屈肘向上弯举", "慢速还原")),
                FollowAlongVideo("arms_triceps", "窄距推墙臂屈伸", 52, "进阶", "身体保持直线，不要耸肩。", listOf("双手窄距撑墙", "屈肘靠近墙面", "发力推回起点"))
            )
        ),
        StrengthBodyArea(
            id = "shoulders",
            title = "肩部",
            subtitle = "三角肌前束、中束与后束",
            focus = "提升肩部稳定和上肢姿态",
            videos = listOf(
                FollowAlongVideo("shoulders_press", "徒手肩推激活", 40, "入门", "肋骨回收，手臂沿身体两侧上推。", listOf("屈肘打开手臂", "头顶伸直双臂", "控制下落")),
                FollowAlongVideo("shoulders_raise", "侧平举脉冲练", 50, "进阶", "手臂抬至肩高，肩膀远离耳朵。", listOf("双臂侧平举", "小幅向上脉冲", "缓慢放下放松"))
            )
        ),
        StrengthBodyArea(
            id = "chest",
            title = "胸部",
            subtitle = "胸大肌、胸小肌与推力链",
            focus = "增强上肢推力和胸部控制",
            videos = listOf(
                FollowAlongVideo("chest_wall_push", "站姿推墙练习", 48, "入门", "头、背、髋保持一条直线。", listOf("双手撑墙略宽于肩", "屈肘靠近墙面", "胸部发力推开")),
                FollowAlongVideo("chest_knee_push", "跪姿俯卧撑节奏练", 55, "进阶", "核心收紧，手肘斜向身体后侧。", listOf("跪姿支撑准备", "胸口下降", "推起并呼气"))
            )
        ),
        StrengthBodyArea(
            id = "back",
            title = "背部",
            subtitle = "背阔肌、斜方肌与竖脊肌",
            focus = "改善背部力量和含胸姿态",
            videos = listOf(
                FollowAlongVideo("back_pull", "徒手下拉夹背", 42, "入门", "想象把手肘拉向裤兜。", listOf("双臂举过头顶", "屈肘向下拉", "夹背停留一秒")),
                FollowAlongVideo("back_swimmer", "俯卧游泳式", 56, "进阶", "颈部保持延伸，不要猛抬头。", listOf("俯卧伸展四肢", "对侧手脚抬起", "交替切换"))
            )
        ),
        StrengthBodyArea(
            id = "abs",
            title = "腹部",
            subtitle = "腹直肌、腹斜肌与深层核心",
            focus = "加强躯干稳定和腹部控制",
            videos = listOf(
                FollowAlongVideo("abs_dead_bug", "死虫式核心激活", 54, "入门", "腰背贴地，动作慢而稳定。", listOf("仰卧抬起四肢", "伸展对侧手脚", "收回后换边")),
                FollowAlongVideo("abs_plank", "短时平板支撑", 38, "进阶", "收紧臀腹，避免塌腰或抬髋。", listOf("肘撑建立直线", "持续均匀呼吸", "膝盖落地放松"))
            )
        ),
        StrengthBodyArea(
            id = "glutes",
            title = "臀部 / 髋部",
            subtitle = "臀大肌、臀中肌与髋部稳定肌",
            focus = "增强髋部稳定和臀肌发力",
            videos = listOf(
                FollowAlongVideo("glutes_bridge", "臀桥顶峰收缩", 50, "入门", "脚跟踩稳，顶起时不要过度挺腰。", listOf("仰卧屈膝准备", "臀部发力抬髋", "顶峰停顿后落下")),
                FollowAlongVideo("glutes_abduction", "站姿髋外展", 58, "进阶", "骨盆保持正向，脚尖朝前。", listOf("单脚稳定站立", "另一侧腿向外抬", "控制收回并换边"))
            )
        ),
        StrengthBodyArea(
            id = "legs",
            title = "腿部",
            subtitle = "股四头肌、腘绳肌与小腿",
            focus = "提升下肢力量和膝踝稳定",
            videos = listOf(
                FollowAlongVideo("legs_squat", "自重深蹲节奏练", 45, "入门", "膝盖对准脚尖，重心落在全脚掌。", listOf("双脚与肩同宽", "屈髋屈膝下蹲", "脚掌发力站起")),
                FollowAlongVideo("legs_lunge", "交替后撤弓步", 57, "进阶", "前侧膝盖稳定，躯干保持直立。", listOf("自然站立准备", "单腿向后撤步", "回正后左右交替"))
            )
        ),
        StrengthBodyArea(
            id = "full_body",
            title = "全身",
            subtitle = "上下肢与核心协同",
            focus = "用短组合唤醒全身肌群",
            videos = listOf(
                FollowAlongVideo("full_body_combo", "深蹲推举组合", 55, "入门", "先稳定下蹲，再连贯向上推举。", listOf("完成一次深蹲", "站起同时双臂上推", "回到起点重复")),
                FollowAlongVideo("full_body_bear", "熊爬肩碰节奏练", 58, "进阶", "膝盖离地少许，骨盆保持稳定。", listOf("四点支撑抬膝", "交替触碰对侧肩", "放下膝盖短暂休息"))
            )
        )
    )

    val communityTabs = listOf("放纵餐", "训练日餐", "好物分享", "训练打卡")

    val achievements = listOf(
        "7 天打卡" to "连续打卡 7 天",
        "消耗达人" to "完成高热量消耗日",
        "早起之星" to "上午完成训练",
        "坚持不演" to "放纵餐后仍完成运动",
        "跑路成功" to "完成第一次户外跑"
    )

    // 与 achievements 前 4 项按顺序对应的徽标符号。
    val achievementBadges = listOf("7", "🔥", "☀", "♕")

    val titles = listOf("火锅幸存者", "奶茶抵抗者", "自律钉子户", "跑步跑路王", "肌肉伙伴饲养员")

    // 离线 MVP 的演示个人资料。
    val profileHero = ProfileHero(
        displayName = "小B同学",
        levelLabel = "Lv.3 初级脂肪猎人",
        currentXp = 560,
        targetXp = 1200
    )

    // 离线 MVP 的演示排行榜。key 对应 TrainingScreen.LeaderboardCategory.title。
    val leaderboardRows: Map<String, List<LeaderboardRunner>> = mapOf(
        "总榜" to listOf(
            LeaderboardRunner("奶茶抵抗者", "18.4 km", "本周户外跑 5 次", "领跑中"),
            LeaderboardRunner("跑步跑路王", "16.9 km", "平均配速 6'18\"", "稳得很"),
            LeaderboardRunner("自律钉子户", "14.2 km", "连续 4 天完成训练", "前三"),
            LeaderboardRunner("火锅幸存者", "9.8 km", "今天刚追加 2.0km", "追击中"),
            LeaderboardRunner("我", "8.6 km", "再跑 1.2km 可超越前一名", "第 18 名", isMe = true),
            LeaderboardRunner("肌肉伙伴饲养员", "7.9 km", "晚间训练已预约", "紧跟")
        ),
        "进步榜" to listOf(
            LeaderboardRunner("火锅幸存者", "+68%", "上周 4.1km，本周 6.9km", "进步王"),
            LeaderboardRunner("小蛋白", "+54%", "室内燃脂次数翻倍", "猛冲"),
            LeaderboardRunner("我", "+32%", "本周训练更稳定了", "第 9 名", isMe = true),
            LeaderboardRunner("跑步跑路王", "+27%", "多完成 2 次户外跑", "加速"),
            LeaderboardRunner("奶茶抵抗者", "+19%", "周末补了一段长跑", "稳住")
        ),
        "坚持榜" to listOf(
            LeaderboardRunner("自律钉子户", "14 天", "每天至少 20 分钟", "不掉线"),
            LeaderboardRunner("奶茶抵抗者", "11 天", "训练日历全亮", "连胜"),
            LeaderboardRunner("跑步跑路王", "8 天", "晨跑习惯养成中", "稳"),
            LeaderboardRunner("我", "6 天", "今天完成后继续连上", "第 12 名", isMe = true),
            LeaderboardRunner("火锅幸存者", "5 天", "放纵餐后也没停", "靠谱")
        ),
        "燃脂榜" to listOf(
            LeaderboardRunner("跑步跑路王", "1,420 kcal", "本周 6 次训练", "燃脂王"),
            LeaderboardRunner("自律钉子户", "1,160 kcal", "力量和跑步都安排了", "高能"),
            LeaderboardRunner("奶茶抵抗者", "980 kcal", "奶茶债快还清了", "优秀"),
            LeaderboardRunner("我", "680 kcal", "再燃 90 kcal 可超越前一名", "第 15 名", isMe = true),
            LeaderboardRunner("火锅幸存者", "610 kcal", "今晚还有一练", "追上来")
        )
    )

    // 离线 MVP 的演示社区内容。
    val communityPosts = listOf(
        CommunityPost(
            name = "火锅幸存者",
            time = "2小时前",
            tag = "放纵餐",
            title = "火锅局幸存报告",
            body = "今天吃了火锅，但没喝奶茶，算不算赢一半？",
            reply = "脂肪怪：这局我本来优势很大。",
            imageType = 0,
            likes = 236,
            comments = 38
        ),
        CommunityPost(
            name = "奶茶戒断中",
            time = "4小时前",
            tag = "训练日餐",
            title = "今天给肌肉伙伴发工资",
            body = "鸡蛋、牛肉、玉米，蛋白质安排上了。",
            reply = "肌肉伙伴：这顿我认可。",
            imageType = 1,
            likes = 198,
            comments = 26
        )
    )
}

data class ProfileHero(
    val displayName: String,
    val levelLabel: String,
    val currentXp: Int,
    val targetXp: Int
)

data class LeaderboardRunner(
    val name: String,
    val value: String,
    val note: String,
    val badge: String,
    val isMe: Boolean = false
)

data class CommunityPost(
    val name: String,
    val time: String,
    val tag: String,
    val title: String,
    val body: String,
    val reply: String,
    val imageType: Int,
    val likes: Int,
    val comments: Int
)
