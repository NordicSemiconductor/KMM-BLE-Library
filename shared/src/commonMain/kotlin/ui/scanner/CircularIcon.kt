package ui.scanner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
fun CircularIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    enabled: Boolean = true,
) {
    Image(
        painter = painter,
        contentDescription = null,
        colorFilter = if (enabled) {
            ColorFilter.tint(MaterialTheme.colorScheme.contentColorFor(backgroundColor))
        } else {
            ColorFilter.tint(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f))
        },
        modifier = modifier
            .background(
                color = if (enabled) {
                    backgroundColor
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                },
                shape = CircleShape
            )
            .padding(8.dp)
    )
}

@Composable
fun CircularIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    enabled: Boolean = true,
) {
    CircularIcon(
        painter = rememberVectorPainter(image = imageVector),
        modifier = modifier,
        backgroundColor = backgroundColor,
        enabled = enabled
    )
}
