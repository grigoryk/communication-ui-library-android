// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.callwithchatdemoapp

import com.azure.android.communication.ui.calling.CallCompositeEventHandler
import com.azure.android.communication.ui.calling.models.CallCompositeErrorEvent
import java.lang.ref.WeakReference

// Handles forwarding of error messages to the CallLauncherActivity
//
// CallLauncherActivity is loosely coupled and will detach the weak reference after disposed.
class CallLauncherActivityErrorHandler(callLauncherActivity: CallWithChatLauncherActivity) :
    CallCompositeEventHandler<CallCompositeErrorEvent> {

    private val activityWr: WeakReference<CallWithChatLauncherActivity> =
        WeakReference(callLauncherActivity)

    override fun handle(it: CallCompositeErrorEvent) {
        println("================= application is logging exception =================")
        println(it.cause)
        println(it.errorCode)
        activityWr.get()?.showAlert(it.errorCode.toString() + " " + it.cause?.message)
        println("====================================================================")
    }
}
