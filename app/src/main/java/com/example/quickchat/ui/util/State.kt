package com.example.quickchat.ui.util

import androidx.annotation.StringRes
import com.example.quickchat.R

object State {
    /**
     * Error state holding values for error ui
     */
    data class ErrorState(
        val hasError: Boolean = false,
        @StringRes val errorMessageStringResource: Int = R.string.empty_string,
    )
}