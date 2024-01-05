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

package ui.server

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.NordicColors

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AdvertiseView(state: ServerState, viewModel: ServerViewModel) {
    OutlinedCard(modifier = Modifier.padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle(
                painter = painterResource("ic_broadcast.xml"),
                title = ServerStrings.characteristics
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val icon = if (state.isAdvertising) {
                    painterResource("ic_advertisements.xml")
                } else {
                    painterResource("ic_advertisements_off.xml")
                }

                val color = if (state.isAdvertising) {
                    NordicColors.green
                } else {
                    NordicColors.gray
                }

                Icon(
                    painter = icon,
                    contentDescription = "",
                    tint = color,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = ServerStrings.advertisement_state + state.isAdvertising.toDisplayString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))

                if (state.isAdvertising) {
                    Button(
                        onClick = { viewModel.stopAdvertise() },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(ServerStrings.stop)
                    }
                } else {
                    Button(
                        onClick = { viewModel.advertise() },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(ServerStrings.advertise)
                    }
                }
            }
        }
    }
}

@Composable
fun Boolean.toDisplayString(): String {
    return when (this) {
        true -> ServerStrings.state_on
        false -> ServerStrings.state_off
    }
}
