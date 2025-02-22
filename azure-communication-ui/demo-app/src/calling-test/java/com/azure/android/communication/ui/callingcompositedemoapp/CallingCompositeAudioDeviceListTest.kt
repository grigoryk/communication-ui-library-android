// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.callingcompositedemoapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.azure.android.communication.ui.callingcompositedemoapp.robots.HomeScreenRobot
import com.azure.android.communication.ui.callingcompositedemoapp.robots.SetupScreenRobot
import com.azure.android.communication.ui.callingcompositedemoapp.util.CallIdentifiersHelper
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CallingCompositeAudioDeviceListTest : BaseUiTest() {

    @Test
    fun selectDefaultAudioDevice() {
        joinGroupSetupScreen()
            .selectAndroidAudioDevice(true)
            .verifyIsAndroidAudioDevice()
            .navigateUpFromSetupScreen()
    }

    @Test
    fun selectSpeakerAudioDevice() {
        joinGroupSetupScreen()
            .selectSpeakerAudioDevice(false)
            .verifyIsSpeakerAudioDevice()
            .navigateUpFromSetupScreen()
    }

    private fun joinGroupSetupScreen(): SetupScreenRobot {
        val setupScreen = HomeScreenRobot()
            .setGroupIdOrTeamsMeetingUrl(CallIdentifiersHelper.getGroupId())
            .setAcsToken(CallIdentifiersHelper.getACSToken())
            .clickLaunchButton()

        setupScreen
            .turnCameraOn()
            .tapSpeakerIcon()

        return setupScreen
    }
}
