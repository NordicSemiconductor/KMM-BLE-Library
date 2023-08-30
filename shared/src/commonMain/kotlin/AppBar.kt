import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@ExperimentalMaterial3Api
@Composable
fun NordicAppBar(
    text: String,
    onNavigationButtonClick: (() -> Unit)? = null,
    onHamburgerButtonClick: (() -> Unit)? = null,
    showBackButton: Boolean = onNavigationButtonClick != null,
    backButtonIcon: ImageVector = Icons.Default.ArrowBack,
    showHamburgerButton: Boolean = onHamburgerButtonClick != null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val appBarColor = if (!isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.primary
    } else {
        Color(0xFF333f48)
    }
    TopAppBar(
        title = { Text(text) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = appBarColor,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            onNavigationButtonClick?.takeIf { showBackButton }?.let { action ->
                IconButton(onClick = { action() }) {
                    Icon(
                        imageVector = backButtonIcon,
                        contentDescription = "Icon",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            } ?: run {
                onHamburgerButtonClick?.takeIf { showHamburgerButton }?.let { action ->
                    IconButton(onClick = { action() }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
    )
}
