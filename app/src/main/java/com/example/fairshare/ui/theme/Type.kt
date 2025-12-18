package com.example.fairshare.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Typography configuration for the FairShare application.
 *
 * Defines text styles following Material Design 3 type scale guidelines.
 * Uses the default system font family with customized sizes and weights.
 *
 * Currently defines:
 * - bodyLarge: Primary body text style (16sp, normal weight)
 *
 * Additional text styles (titleLarge, labelSmall, etc.) can be added
 * by uncommenting and customizing the provided templates.
 */
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)