/*
 * Copyright (c) 2023, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun NordicTheme(content: @Composable () -> Unit) {
    val darkColorPalette = darkColorScheme(
        primary = Color(0xFF00A9CE),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF6AD1E3),
        onPrimaryContainer = Color(0xFFFFFFFF),
        inversePrimary = Color(0xFF006780),
        secondary = Color(0xFF00A9CE),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFF00A9CE),
        onSecondaryContainer = Color(0xFFFFFFFF),
        tertiary = Color(0xFF670016),
        onTertiary = Color(0xFFFFB2B6),
        tertiaryContainer = Color(0xFFFFB2B6),
        onTertiaryContainer = Color(0xFF670016),
        background = Color(0xFF000000),
        onBackground = Color(0xFFE1E2E5),
        surface = Color(0xFF191C1E),
        onSurface = Color(0xFFE1E2E5),
        surfaceVariant = Color(0xFF40484B),
        onSurfaceVariant = Color(0xFFBFC8CC),
        inverseSurface = Color(0xFFE1E2E5),
        inverseOnSurface = Color(0xFF191C1E),
        error = Color(0xFFFFB4A9),
        onError = Color(0xFF000000),
        errorContainer = Color(0xFF930006),
        onErrorContainer = Color(0xFFFFDAD4),
        outline = Color(0xFF899296),
    )

    val lightColorPalette = lightColorScheme(
        primary = Color(0xFF00A9CE),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFB3EBFF),
        onPrimaryContainer = Color(0xFF001F29),
        inversePrimary = Color(0xFF57D5FC),
        secondary = Color(0xFF0077C8),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFF0077C8),
        onSecondaryContainer = Color(0xFFFFFFFF),
        tertiary = Color(0xFF0033A0),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFD0E4FF),
        onTertiaryContainer = Color(0xFF41000A),
        background = Color(0xFFFFFFFF),
        onBackground = Color(0xFF191C1E),
        surface = Color(0xFFF5F5F5),
        onSurface = Color(0xFF191C1E),
        surfaceVariant = Color(0xFFDCE4E8),
        onSurfaceVariant = Color(0xFF40484B),
        inverseSurface = Color(0xFF2E3133),
        inverseOnSurface = Color(0xFFF0F1F4),
        error = Color(0xFFBA1B1B),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD4),
        onErrorContainer = Color(0xFF410001),
        outline = Color(0xFF70787C),
    )

    val colorScheme = if (isSystemInDarkTheme()) {
        darkColorPalette
    } else {
        lightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = nordicTypography,
    ) {
        val background = colorScheme.background

        CompositionLocalProvider(
            LocalContentColor provides contentColorFor(background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = background),
            ) {
                content()
            }
        }
    }
}
