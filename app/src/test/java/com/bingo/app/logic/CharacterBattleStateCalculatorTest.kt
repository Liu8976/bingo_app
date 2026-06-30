package com.bingo.app.logic

import com.bingo.app.mock.MockBingoData
import com.bingo.app.model.FatMonsterState
import com.bingo.app.model.MuscleBuddyState
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterBattleStateCalculatorTest {
    @Test
    fun zeroMinutesMapsToNormalStates() {
        val state = CharacterBattleStateCalculator.calculate(MockBingoData.todaySummary(0))

        assertEquals(FatMonsterState.Normal, state.fatMonsterState)
        assertEquals(MuscleBuddyState.Normal, state.muscleBuddyState)
        assertEquals("脂肪怪正在嚣张", state.battleTitle)
    }

    @Test
    fun tenMinutesMapsToTeasingAndReady() {
        val state = CharacterBattleStateCalculator.calculate(MockBingoData.todaySummary(10))

        assertEquals(FatMonsterState.Teasing, state.fatMonsterState)
        assertEquals(MuscleBuddyState.Ready, state.muscleBuddyState)
    }

    @Test
    fun twentyFiveMinutesMapsToNervousAndActive() {
        val state = CharacterBattleStateCalculator.calculate(MockBingoData.todaySummary(25))

        assertEquals(FatMonsterState.Nervous, state.fatMonsterState)
        assertEquals(MuscleBuddyState.Active, state.muscleBuddyState)
    }

    @Test
    fun thirtyFiveMinutesMapsToWeakenedAndPowered() {
        val state = CharacterBattleStateCalculator.calculate(MockBingoData.todaySummary(35))

        assertEquals(FatMonsterState.Weakened, state.fatMonsterState)
        assertEquals(MuscleBuddyState.Powered, state.muscleBuddyState)
    }

    @Test
    fun fiftyMinutesMapsToDefeatedAndVictory() {
        val state = CharacterBattleStateCalculator.calculate(MockBingoData.todaySummary(50))

        assertEquals(FatMonsterState.Defeated, state.fatMonsterState)
        assertEquals(MuscleBuddyState.Victory, state.muscleBuddyState)
    }
}
