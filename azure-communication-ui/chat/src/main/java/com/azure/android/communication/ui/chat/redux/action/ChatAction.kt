// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.chat.redux.action

import com.azure.android.communication.ui.chat.models.MessageInfoModel

internal sealed class ChatAction : Action {
    class StartChat : ChatAction()
    class Initialization : ChatAction()
    class Initialized : ChatAction()
    class TopicUpdated(val topic: String) : ChatAction()
    class SendMessage(val messageInfoModel: MessageInfoModel) : ChatAction()
    class MessageSent(val messageInfoModel: MessageInfoModel) : ChatAction()
}