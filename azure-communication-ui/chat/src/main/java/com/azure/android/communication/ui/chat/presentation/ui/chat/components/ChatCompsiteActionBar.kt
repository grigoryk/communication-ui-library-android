// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.chat.presentation.ui.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun ChatCompositeActionBar() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row {}
    }
}

@Preview
@Composable
internal fun PreviewChatCompositeActionBar() {
    Column {
        ChatCompositeActionBar()
    }
}