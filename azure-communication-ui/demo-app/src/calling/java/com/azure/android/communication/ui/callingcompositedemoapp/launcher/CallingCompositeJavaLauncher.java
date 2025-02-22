// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.callingcompositedemoapp.launcher;

import com.azure.android.communication.common.CommunicationTokenCredential;
import com.azure.android.communication.common.CommunicationTokenRefreshOptions;
import com.azure.android.communication.ui.calling.CallComposite;
import com.azure.android.communication.ui.calling.CallCompositeBuilder;
import com.azure.android.communication.ui.calling.models.CallCompositeGroupCallLocator;
import com.azure.android.communication.ui.calling.models.CallCompositeJoinLocator;
import com.azure.android.communication.ui.calling.models.CallCompositeLocalOptions;
import com.azure.android.communication.ui.calling.models.CallCompositeLocalizationOptions;
import com.azure.android.communication.ui.calling.models.CallCompositeRemoteOptions;
import com.azure.android.communication.ui.calling.models.CallCompositeSetupScreenViewData;
import com.azure.android.communication.ui.calling.models.CallCompositeTeamsMeetingLinkLocator;
import com.azure.android.communication.ui.callingcompositedemoapp.CallLauncherActivity;
import com.azure.android.communication.ui.callingcompositedemoapp.CallLauncherActivityErrorHandler;
import com.azure.android.communication.ui.callingcompositedemoapp.R;
import com.azure.android.communication.ui.callingcompositedemoapp.RemoteParticipantJoinedHandler;
import com.azure.android.communication.ui.callingcompositedemoapp.features.AdditionalFeatures;
import com.azure.android.communication.ui.callingcompositedemoapp.features.SettingsFeatures;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Callable;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class CallingCompositeJavaLauncher implements CallingCompositeLauncher {
    private final Callable<String> tokenRefresher;

    public CallingCompositeJavaLauncher(final Callable<String> tokenRefresher) {
        this.tokenRefresher = tokenRefresher;
    }

    @Override
    public void launch(final CallLauncherActivity callLauncherActivity,
                       final String displayName,
                       final UUID groupId,
                       final String meetingLink,
                       final Function1<? super String, Unit> showAlert) {

        final CallCompositeBuilder builder = new CallCompositeBuilder();

        SettingsFeatures.initialize(callLauncherActivity.getApplicationContext());

        final String selectedLanguage = SettingsFeatures.language();
        final Locale locale = SettingsFeatures.locale(selectedLanguage);

        builder.localization(new CallCompositeLocalizationOptions(locale,
                SettingsFeatures.getLayoutDirection()));

        if (AdditionalFeatures.Companion.getSecondaryThemeFeature().getActive()) {
            builder.theme(R.style.MyCompany_Theme_Calling);
        }

        final CallComposite callComposite = builder.build();
        callComposite.addOnErrorEventHandler(new CallLauncherActivityErrorHandler(callLauncherActivity));

        if (SettingsFeatures.getRemoteParticipantPersonaInjectionSelection()) {
            callComposite.addOnRemoteParticipantJoinedEventHandler(
                    new RemoteParticipantJoinedHandler(callComposite, callLauncherActivity));
        }

        final CommunicationTokenRefreshOptions communicationTokenRefreshOptions =
                new CommunicationTokenRefreshOptions(tokenRefresher, true);
        final CommunicationTokenCredential communicationTokenCredential =
                new CommunicationTokenCredential(communicationTokenRefreshOptions);

        final CallCompositeJoinLocator locator = groupId != null
                ? new CallCompositeGroupCallLocator(groupId)
                : new CallCompositeTeamsMeetingLinkLocator(meetingLink);

        final CallCompositeRemoteOptions remoteOptions =
                new CallCompositeRemoteOptions(locator, communicationTokenCredential, displayName);


        final CallCompositeLocalOptions localOptions = new CallCompositeLocalOptions()
                .setParticipantViewData(SettingsFeatures
                        .getParticipantViewData(callLauncherActivity.getApplicationContext()))
                .setSetupScreenViewData(
                        new CallCompositeSetupScreenViewData()
                            .setTitle(SettingsFeatures.getTitle())
                            .setSubtitle(SettingsFeatures.getSubtitle()));

        callComposite.launch(callLauncherActivity, remoteOptions, localOptions);
    }
}
