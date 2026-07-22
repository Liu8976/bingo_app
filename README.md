# Bingo

Bingo 是一个使用 Kotlin 和 Jetpack Compose 开发的 Android 健身应用原型。产品通过“脂肪怪”和“肌肉伙伴”的角色状态，把运动、饮食、饮水和体重记录转化为每日反击进度。

## 当前范围

当前版本是本地离线 MVP，不依赖账号、后端或网络权限：

- “今日”展示角色战况、每日任务和本地健康摘要。
- “训练”提供六种本地训练选项，完成后写入当天运动记录。
- “记录”支持体重、饮食录入，以及真实体重趋势和日/周/月运动汇总。
- “广场”使用演示内容，支持分类筛选，不提供发布、评论或收藏。
- “我的”展示演示用户、称号和成就。

排行榜、广场帖子和个人资料属于集中管理的演示数据，不代表真实联网结果。

## 代码结构

- `app/src/main/java/com/bingo/app/data`：DataStore 持久化与共享 ViewModel。
- `app/src/main/java/com/bingo/app/logic`：今日任务和角色战斗状态计算。
- `app/src/main/java/com/bingo/app/model`：应用数据模型。
- `app/src/main/java/com/bingo/app/mock`：离线演示内容。
- `app/src/main/java/com/bingo/app/ui`：共享 Compose 组件。
- `app/src/main/java/com/bingo/app/ui/screen`：五个主页面。

## 本地数据

运动、体重、今日饮食和饮水数据存储在 Preferences DataStore 中。按日数据带日期，跨天后不会继续显示为“今日”；体重和运动历史最多保留最近 180 条。

Debug 构建的首页会显示运动分钟状态切换器，用于快速检查角色状态。该入口不会出现在非 Debug 构建中。

## 构建

使用 JDK 17 或更高版本，在仓库根目录运行：

```powershell
.\gradlew.bat assembleDebug "-Dkotlin.compiler.execution.strategy=in-process"
```

## 当前限制

- 不包含训练视频或计时器；训练完成由用户主动确认。
- 不包含登录、云同步、真实排行榜和真实社区互动。
- 不包含健康数据分享或导出。
- 大尺寸角色位图仍保留原始资源，后续需要在不损失设计质量的前提下专项优化。

产品规划和页面规格见仓库根目录的 PRD 与内容策略文档。
