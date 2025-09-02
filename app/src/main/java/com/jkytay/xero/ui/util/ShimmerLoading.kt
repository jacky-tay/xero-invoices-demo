package com.jkytay.xero.ui.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.shimmerLoading(
    durationMillis: Int = 1000,
    cornerRadiusDp: Dp = 8.dp,
): Modifier {
    val transition = rememberInfiniteTransition()

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer loader",
    )

    val colors = remember {
        listOf(
            Color.Gray.copy(alpha = 0.2f),
            Color.Gray.copy(alpha = 0.5f),
            Color.Gray.copy(alpha = 0.2f),
        )
    }
    val density = LocalDensity.current

    return drawBehind {
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(x = translateAnimation, y = translateAnimation),
                end = Offset(x = translateAnimation + 100f, y = translateAnimation + 100f),
            ),
            cornerRadius = with(density) {
                CornerRadius(cornerRadiusDp.toPx())
            }
        )
    }
}