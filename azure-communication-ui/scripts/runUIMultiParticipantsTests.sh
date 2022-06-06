#!/usr/bin/env bash
unset ANDROID_SERIAL


if [ -z "$DEVICE" ]; then
  ./installEmulatorWithArgs.sh first_emulator
  ./installEmulatorWithArgs.sh second_emulator
fi

setLocalProperty() {
  cat ./local.properties | sed -e "/^$1=/d" > ./temp_file
  printf "$1=\"$2\"\n" >> ./temp_file
  mv -f ./temp_file ./local.properties
}

cd ..
setLocalProperty "USER_NAME" "Test User"
#Replace ACS Token with expired token
setLocalProperty "ACS_TOKEN_EXPIRED" "$1"

DEVICE1=($(adb devices | grep "device$" | head -1 | sed -e "s|device||g"))
DEVICE2=($(adb devices | grep "device$" | tail -1 | sed -e "s|device||g"))

./gradlew clean assembleDebug

export ANDROID_SERIAL=$DEVICE1
./gradlew connectedCallingDebugAndroidTest --stacktrace -Pandroid.testInstrumentationRunnerArguments.class=com.azure.android.communication.ui.callingcompositedemoapp.participant.SingleRemoteParticipantTest#testJoinGroupCallWithVideoOffAndRemoteParticipantVideoOn -Pandroid.testInstrumentationRunnerArguments.teamsUrl="$2" -Pandroid.testInstrumentationRunnerArguments.tokenFunctionUrl="$3" &
sleep 40
export ANDROID_SERIAL=$DEVICE2 
./gradlew connectedCallingDebugAndroidTest --stacktrace -Pandroid.testInstrumentationRunnerArguments.class=com.azure.android.communication.ui.callingcompositedemoapp.participant.SingleRemoteParticipantTest#testJoinGroupCallWithVideoOnAndRemoteParticipantVideoOff -Pandroid.testInstrumentationRunnerArguments.teamsUrl="$2" -Pandroid.testInstrumentationRunnerArguments.tokenFunctionUrl="$3" &
wait

adb -s first_emulator emu kill
adb -s second_emulator emu kill
$ANDROID_HOME/tools/bin/avdmanager delete avd -n first_emulator
$ANDROID_HOME/tools/bin/avdmanager delete avd -n second_emulator