package com.example.covid19.data.entity

import androidx.annotation.StringRes
import com.example.covid19.R

sealed class TimestampModel {
    data class AllTime(@StringRes val timestamp: Int = R.string.all_time) : TimestampModel()
    data class Week(@StringRes val timestamp: Int = R.string.week, val days: Int = 7) : TimestampModel()
    data class Month(@StringRes val timestamp: Int = R.string.month, val days: Int = 31) : TimestampModel()
    data class TwoWeeks(@StringRes val timestamp: Int = R.string.two_weeks, val days: Int = 14) : TimestampModel()

    companion object {
        val dropdownContentTimeStamp = arrayListOf(
            AllTime().timestamp,
            Week().timestamp,
            Month().timestamp,
            TwoWeeks().timestamp
        )
        fun getTimestamp(@StringRes timestamp: Int) = when (timestamp) {
            AllTime().timestamp -> AllTime()
            Week().timestamp -> Week()
            Month().timestamp -> Month()
            TwoWeeks().timestamp -> TwoWeeks()
            else -> AllTime()
        }
    }
}
