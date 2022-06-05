#!/usr/bin/env bash

unset ANDROID_SERIAL
DEVICE=($(adb devices | grep "device$" | sed -e "s|device||g"))

setLocalProperty() {
  cat ./local.properties | sed -e "/^$1=/d" > ./temp_file
  printf "$1=\"$2\"\n" >> ./temp_file
  mv -f ./temp_file ./local.properties
}

if [ -z "$DEVICE" ]; then
  ./installEmulator.sh
fi

cd ..
setLocalProperty "USER_NAME" "Test User"
#Replace ACS Token with expired token
setLocalProperty "ACS_TOKEN_EXPIRED" "$1"

# run Ui tests with the required parameters
./gradlew clean build
./gradlew connectedCallingDebugAndroidTest --stacktrace -Pandroid.testInstrumentationRunnerArguments.class=com.azure.android.communication.ui.callingcompositedemoapp.CallingCompositeBaselineUiTest#testJoinGroupCallWithVideoDisabled -Pandroid.testInstrumentationRunnerArguments.teamsUrl="$2" -Pandroid.testInstrumentationRunnerArguments.tokenFunctionUrl="$3"

# clean up
if [ -z "$DEVICE" ]; then
  adb emu kill
  $ANDROID_HOME/tools/bin/avdmanager delete avd -n xamarin_android_emulator
fi
