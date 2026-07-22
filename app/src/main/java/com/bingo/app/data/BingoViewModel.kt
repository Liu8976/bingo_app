package com.bingo.app.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bingo.app.model.BodyRecordsSummary
import com.bingo.app.model.TodayFitnessSummary
import com.bingo.app.model.TrainingOption
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 页面共用的状态持有者，把 [BingoRepository] 的数据以 [StateFlow] 形式提供给 Compose。
 *
 * 之前每个屏幕各自持有/写死自己的数据；现在今日战况（首页）和身体档案（记录页）
 * 共享同一份由 DataStore 持久化的数据，调试用的分钟数调节也会真实写入而不只是内存态。
 */
class BingoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BingoRepository(application)

    val todaySummary: StateFlow<TodayFitnessSummary> = repository.todaySummary
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TodayFitnessSummary(
                exerciseMinutes = 0,
                caloriesBurned = 0,
                completedTaskCount = 0,
                totalTaskCount = 5,
                hasFoodLog = false,
                hasWeightLog = false,
                waterCups = 0,
                streakDays = 0
            )
        )

    val bodyRecords: StateFlow<BodyRecordsSummary> = repository.bodyRecords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BodyRecordsSummary()
        )

    fun setDebugMinutes(minutes: Int) {
        viewModelScope.launch { repository.setExerciseMinutes(minutes) }
    }

    fun logWeight(weightKg: Double) {
        viewModelScope.launch { repository.logWeight(weightKg) }
    }

    fun logFoodIntake(calories: Int) {
        viewModelScope.launch { repository.logFoodIntake(calories) }
    }

    fun addWaterCup() {
        viewModelScope.launch { repository.addWaterCup() }
    }

    fun completeTraining(training: TrainingOption) {
        viewModelScope.launch {
            repository.completeTraining(training.durationMinutes, training.estimatedCalories)
        }
    }
}
