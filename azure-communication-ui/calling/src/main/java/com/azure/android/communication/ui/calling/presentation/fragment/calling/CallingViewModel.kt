// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.calling.presentation.fragment.calling

import com.azure.android.communication.ui.calling.presentation.fragment.BaseViewModel
import com.azure.android.communication.ui.calling.presentation.fragment.calling.banner.BannerViewModel
import com.azure.android.communication.ui.calling.presentation.fragment.calling.controlbar.ControlBarViewModel
import com.azure.android.communication.ui.calling.presentation.fragment.calling.hangup.LeaveConfirmViewModel
import com.azure.android.communication.ui.calling.presentation.fragment.calling.header.InfoHeaderViewModel
import com.azure.android.communication.ui.calling.presentation.fragment.calling.hold.OnHoldOverlayViewModel
import com.azure.android.communication.ui.calling.presentation.fragment.calling.lobby.LobbyOverlayViewModel
import com.azure.android.communication.ui.calling.presentation.fragment.calling.localuser.LocalParticipantViewModel
import com.azure.android.communication.ui.calling.presentation.fragment.calling.participant.grid.ParticipantGridViewModel
import com.azure.android.communication.ui.calling.presentation.fragment.calling.participantlist.ParticipantListViewModel
import com.azure.android.communication.ui.calling.presentation.fragment.common.audiodevicelist.AudioDeviceListViewModel
import com.azure.android.communication.ui.calling.presentation.fragment.factories.CallingViewModelFactory
import com.azure.android.communication.ui.calling.redux.Store
import com.azure.android.communication.ui.calling.redux.state.CallingStatus
import com.azure.android.communication.ui.calling.redux.state.LifecycleStatus
import com.azure.android.communication.ui.calling.redux.state.ReduxState
import kotlinx.coroutines.CoroutineScope

internal class CallingViewModel(
    store: Store<ReduxState>,
    callingViewModelProvider: CallingViewModelFactory,
) :
    BaseViewModel(store) {

    private val participantGridViewModel =
        callingViewModelProvider.participantGridViewModel
    private val controlBarViewModel = callingViewModelProvider.controlBarViewModel
    private val confirmLeaveOverlayViewModel =
        callingViewModelProvider.confirmLeaveOverlayViewModel
    private val localParticipantViewModel =
        callingViewModelProvider.localParticipantViewModel
    private val floatingHeaderViewModel = callingViewModelProvider.floatingHeaderViewModel
    private val audioDeviceListViewModel =
        callingViewModelProvider.audioDeviceListViewModel
    private val participantListViewModel =
        callingViewModelProvider.participantListViewModel
    private val bannerViewModel = callingViewModelProvider.bannerViewModel
    private val lobbyOverlayViewModel = callingViewModelProvider.lobbyOverlayViewModel
    private val holdOverlayViewModel = callingViewModelProvider.onHoldOverlayViewModel
    private val errorInfoViewModel = callingViewModelProvider.snackBarViewModel

    fun getLobbyOverlayViewModel(): LobbyOverlayViewModel {
        return lobbyOverlayViewModel
    }

    fun getHoldOverlayViewModel(): OnHoldOverlayViewModel {
        return holdOverlayViewModel
    }

    fun getParticipantGridViewModel(): ParticipantGridViewModel {
        return participantGridViewModel
    }

    fun getAudioDeviceListViewModel(): AudioDeviceListViewModel {
        return audioDeviceListViewModel
    }

    fun getControlBarViewModel(): ControlBarViewModel {
        return controlBarViewModel
    }

    fun getConfirmLeaveOverlayViewModel(): LeaveConfirmViewModel {
        return confirmLeaveOverlayViewModel
    }

    fun getLocalParticipantViewModel(): LocalParticipantViewModel {
        return localParticipantViewModel
    }

    fun getFloatingHeaderViewModel(): InfoHeaderViewModel {
        return floatingHeaderViewModel
    }

    fun getParticipantListViewModel(): ParticipantListViewModel {
        return participantListViewModel
    }

    fun getBannerViewModel(): BannerViewModel {
        return bannerViewModel
    }

    fun getErrorInfoViewModel() = errorInfoViewModel

    fun switchFloatingHeader() {
        floatingHeaderViewModel.switchFloatingHeader()
    }

    fun requestCallEnd() {
        confirmLeaveOverlayViewModel.requestExitConfirmation()
    }

    override fun init(coroutineScope: CoroutineScope) {
        val state = store.getCurrentState()

        controlBarViewModel.init(
            state.permissionState,
            state.localParticipantState.cameraState,
            state.localParticipantState.audioState
        )

        localParticipantViewModel.init(
            state.localParticipantState.displayName,
            state.localParticipantState.audioState.operation,
            state.localParticipantState.videoStreamID,
            state.remoteParticipantState.participantMap.count(),
            state.callState.callingStatus,
            state.localParticipantState.cameraState.device,
        )

        floatingHeaderViewModel.init(
            state.callState.callingStatus,
            state.remoteParticipantState.participantMap.count()
        )
        audioDeviceListViewModel.init(
            state.localParticipantState.audioState
        )
        bannerViewModel.init(
            state.callState
        )

        participantListViewModel.init(
            state.remoteParticipantState.participantMap,
            state.localParticipantState
        )

        lobbyOverlayViewModel.init(state.callState.callingStatus)
        holdOverlayViewModel.init(state.callState.callingStatus, state.audioSessionState.audioFocusStatus)

        participantGridViewModel.init(state.callState.callingStatus)

        super.init(coroutineScope)
    }

    override suspend fun onStateChange(state: ReduxState) {

        if (state.lifecycleState.state == LifecycleStatus.BACKGROUND) {
            participantGridViewModel.clear()
            localParticipantViewModel.clear()
            return
        }

        controlBarViewModel.update(
            state.permissionState,
            state.localParticipantState.cameraState,
            state.localParticipantState.audioState,
            state.callState.callingStatus
        )

        localParticipantViewModel.update(
            state.localParticipantState.displayName,
            state.localParticipantState.audioState.operation,
            state.localParticipantState.videoStreamID,
            state.remoteParticipantState.participantMap.count(),
            state.callState.callingStatus,
            state.localParticipantState.cameraState.device,
        )

        audioDeviceListViewModel.update(
            state.localParticipantState.audioState,
        )

        lobbyOverlayViewModel.update(state.callState.callingStatus)
        holdOverlayViewModel.update(state.callState.callingStatus, state.audioSessionState.audioFocusStatus)

        participantGridViewModel.updateIsLobbyOverlayDisplayed(state.callState.callingStatus)

        if (state.callState.callingStatus == CallingStatus.LOCAL_HOLD) {
            participantGridViewModel.update(
                System.currentTimeMillis(),
                mapOf(),
            )
            floatingHeaderViewModel.dismiss()
            participantListViewModel.closeParticipantList()
            localParticipantViewModel.update(
                state.localParticipantState.displayName,
                state.localParticipantState.audioState.operation,
                state.localParticipantState.videoStreamID,
                0,
                state.callState.callingStatus,
                state.localParticipantState.cameraState.device,
            )
        }

        if (shouldUpdateRemoteParticipantsViewModels(state)) {
            participantGridViewModel.update(
                state.remoteParticipantState.modifiedTimestamp,
                state.remoteParticipantState.participantMap,
            )

            floatingHeaderViewModel.update(
                state.remoteParticipantState.participantMap.count()
            )

            participantListViewModel.update(
                state.remoteParticipantState.participantMap,
                state.localParticipantState
            )

            bannerViewModel.update(state.callState)

            state.localParticipantState.cameraState.error?.let {
                errorInfoViewModel.updateCallCompositeError(it)
            }
        }
        updateOverlayDisplayedState(state.callState.callingStatus)
    }

    private fun shouldUpdateRemoteParticipantsViewModels(state: ReduxState) =
        state.callState.callingStatus == CallingStatus.CONNECTED

    private fun updateOverlayDisplayedState(callingStatus: CallingStatus) {
        floatingHeaderViewModel.updateIsOverlayDisplayed(callingStatus)
        bannerViewModel.updateIsOverlayDisplayed(callingStatus)
        localParticipantViewModel.updateIsOverlayDisplayed(callingStatus)
    }
}
