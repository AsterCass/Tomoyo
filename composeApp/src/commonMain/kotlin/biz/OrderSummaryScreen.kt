/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package biz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.cancel
import tomoyo.composeapp.generated.resources.cupcakes
import tomoyo.composeapp.generated.resources.flavor
import tomoyo.composeapp.generated.resources.new_cupcake_order
import tomoyo.composeapp.generated.resources.order_details
import tomoyo.composeapp.generated.resources.pickup_date
import tomoyo.composeapp.generated.resources.quantity
import tomoyo.composeapp.generated.resources.send

/**
 * This composable expects [orderUiState] that represents the order state, [onCancelButtonClicked]
 * lambda that triggers canceling the order and passes the final order to [onSendButtonClicked]
 * lambda
 */
@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val numberOfCupcakes = stringResource(
        Res.string.cupcakes, // TODO: plurals
        orderUiState.quantity
    )
    //Load and format a string resource with the parameters.
    val orderSummary = stringResource(
        Res.string.order_details,
        numberOfCupcakes,
        orderUiState.flavor,
        orderUiState.date,
        orderUiState.quantity
    )
    val newOrder = stringResource(Res.string.new_cupcake_order)
    //Create a list of order summary to display
    val items = listOf(
        // Summary line 1: display selected quantity
        Pair(stringResource(Res.string.quantity), numberOfCupcakes),
        // Summary line 2: display selected flavor
        Pair(stringResource(Res.string.flavor), orderUiState.flavor),
        // Summary line 3: display selected pickup date
        Pair(stringResource(Res.string.pickup_date), orderUiState.date)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                Text(item.first.uppercase())
                Text(text = item.second, fontWeight = FontWeight.Bold)
                HorizontalDivider(thickness = 1.dp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            FormattedPriceLabel(
                subtotal = orderUiState.price,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSendButtonClicked(newOrder, orderSummary) }
                ) {
                    Text(stringResource(Res.string.send))
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(Res.string.cancel))
                }
            }
        }
    }
}

