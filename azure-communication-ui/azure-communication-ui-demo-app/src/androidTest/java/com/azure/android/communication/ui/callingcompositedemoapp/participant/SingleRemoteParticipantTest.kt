// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.callingcompositedemoapp.participant

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.azure.android.communication.ui.callingcompositedemoapp.BaseUiTest
import com.azure.android.communication.ui.callingcompositedemoapp.robots.HomeScreenRobot
import com.azure.android.communication.ui.callingcompositedemoapp.util.CallIdentifiersHelper
import com.azure.android.communication.ui.callingcompositedemoapp.util.RunWhenScreenOffOrLockedRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SingleRemoteParticipantTest : BaseUiTest() {
    private val identifier: String = "e1c3f490-e4fc-11ec-86f7-e50b7e8d7095"

    @get:Rule
    val screenLockRule = RunWhenScreenOffOrLockedRule()

    @Test
    fun testJoinGroupCallWithVideoOffAndRemoteParticipantVideoOn() {
        val setupScreen = HomeScreenRobot()
            .setGroupIdOrTeamsMeetingUrl(identifier)
            .setAcsToken(CallIdentifiersHelper.getACSToken())
            .clickLaunchButton()

        setupScreen.turnCameraOff()

        val callScreen = setupScreen.clickJoinCallButton()
        callScreen
            .showParticipantList()
            .dismissParticipantList()
            .showLocalParticipantPip()
            .showRemoteParticipantGrid()
            .showRemoteParticipantVideoViewDisplayName()
            .showRemoteParticipantVideoViewMicIndicator()
            .sleep()
            .clickEndCall()
            .clickLeaveCall()
    }

    @Test
    fun testJoinGroupCallWithVideoOnAndRemoteParticipantVideoOff() {
        val setupScreen = HomeScreenRobot()
            .setGroupIdOrTeamsMeetingUrl(identifier)
            .setAcsToken(CallIdentifiersHelper.getACSToken())
            .clickLaunchButton()

        setupScreen.turnCameraOn()

        val callScreen = setupScreen.clickJoinCallButton()
        callScreen
            .showParticipantList()
            .dismissParticipantList()
            .showLocalParticipantPip()
            .showSwitchCameraButton()
            .showRemoteParticipantGrid()
            .showRemoteParticipantAvatar()
            .sleep()
            .clickEndCall()
            .clickLeaveCall()
    }
}
