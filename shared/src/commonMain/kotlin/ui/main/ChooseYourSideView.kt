package ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val ANIMATION_DURATION = 500

@Composable
fun ChooseYourSideView(
    icon: Painter,
    isSelected: Boolean,
    info: String,
    selectAction: () -> Unit,
) {
    val animateStateButtonColor = animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        },
        animationSpec = tween(ANIMATION_DURATION, 0, LinearEasing)
    )

    val animateStateTextButtonColor = animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        },
        animationSpec = tween(ANIMATION_DURATION, 0, LinearEasing)
    )

    val cardColors = CardDefaults.outlinedCardColors(
        containerColor = animateStateButtonColor.value,
        contentColor = animateStateTextButtonColor.value,
        disabledContainerColor = animateStateButtonColor.value,
        disabledContentColor = animateStateTextButtonColor.value
    )

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { selectAction() },
        colors = cardColors
    ) {

        Row(modifier = Modifier.padding(16.dp)) {
            Icon(
                painter = icon,
                contentDescription = "",
                tint = animateStateTextButtonColor.value,
                modifier = Modifier.size(40.dp)
            )

            Text(
                info,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                color = animateStateTextButtonColor.value
            )
        }
    }
}
