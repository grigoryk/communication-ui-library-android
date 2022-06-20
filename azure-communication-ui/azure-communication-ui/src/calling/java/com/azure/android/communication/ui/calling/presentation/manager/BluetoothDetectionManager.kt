// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.calling.presentation.manager

import android.app.Activity
import android.content.Context
import com.azure.android.communication.ui.calling.redux.Store
import com.azure.android.communication.ui.calling.redux.action.LocalParticipantAction
import com.azure.android.communication.ui.calling.redux.state.ReduxState
import android.os.Bundle
import com.azure.android.communication.ui.calling.redux.middleware.bluetooth.BluetoothDetectorImpl

// Glues the Detector and Redux Together
// Listens for Bluetooth devices and dispatches info
// when they are detected
internal class BluetoothDetectionManager(
    context: Context,
    store: Store<ReduxState>
) {
    private val detector = BluetoothDetectorImpl(context) { available: Boolean, name: String ->
        store.dispatch(
            LocalParticipantAction.AudioDeviceBluetoothSCOAvailable(
                available,
                name
            )
        )
    }

    // Hook for onCreate
    // Starts detecting devices
    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            detector.start()
        }
    }

    // Call when the Activity is finishing (i.e. call is done)
    fun onDestroy(activity: Activity) {
        if (activity.isFinishing) {
            detector.stop()
        }
    }
}