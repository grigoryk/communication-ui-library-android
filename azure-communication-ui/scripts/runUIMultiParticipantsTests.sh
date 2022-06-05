#!/usr/bin/env bash

unset ANDROID_SERIAL
DEVICE=($(adb devices | grep "device$" | sed -e "s|device||g"))


DEVICE1=($(adb devices | grep "device$" | head -1 | sed -e "s|device||g"))
DEVICE2=($(adb devices | grep "device$" | tail -1 | sed -e "s|device||g"))

cd ..
./gradlew clean build
export ANDROID_SERIAL=$DEVICE1
./gradlew connectedCallingDebugAndroidTest --stacktrace -Pandroid.testInstrumentationRunnerArguments.class=com.azure.android.communication.ui.callingcompositedemoapp.participant.SingleRemoteParticipantTest#testJoinGroupCallWithVideoOffAndRemoteParticipantVideoOn -Pandroid.testInstrumentationRunnerArguments.teamsUrl="$2" -Pandroid.testInstrumentationRunnerArguments.tokenFunctionUrl="$3" &
sleep 20
export ANDROID_SERIAL=$DEVICE2 
./gradlew connectedCallingDebugAndroidTest --stacktrace -Pandroid.testInstrumentationRunnerArguments.class=com.azure.android.communication.ui.callingcompositedemoapp.participant.SingleRemoteParticipantTest#testJoinGroupCallWithVideoOnAndRemoteParticipantVideoOff -Pandroid.testInstrumentationRunnerArguments.teamsUrl="$2" -Pandroid.testInstrumentationRunnerArguments.tokenFunctionUrl="$3" &
wait