package ui.server

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.scanner.CircularIcon

@Composable
fun SectionTitle(
    painter: Painter,
    title: String,
    modifier: Modifier = Modifier,
    menu: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        CircularIcon(painter)

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = title,
            textAlign = TextAlign.Start,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        menu?.invoke()
    }
}

@Composable
fun SectionTitle(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    menu: @Composable (() -> Unit)? = null,
) {
    SectionTitle(
        painter = rememberVectorPainter(image = icon),
        title = title,
        menu = menu,
        modifier = modifier
    )
}
