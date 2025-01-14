![Hero Image](docs/media/mobile-ui-library-hero-image.png)

# Azure Communication UI Mobile Library for Android

Azure Communication [UI Mobile Library](https://docs.microsoft.com/en-us/azure/communication-services/concepts/ui-library/ui-library-overview) is an Azure Communication Services capability focused on providing UI components for common business-to-consumer and business-to-business calling interactions.


## Getting Started

Get started with Azure Communication Services by using the UI Library to integrate communication experiences into your applications. For detailed instructions to quickly integrate the UI Library functionalities visit the [Quick-start Documentation](https://docs.microsoft.com/en-us/azure/communication-services/quickstarts/ui-library/get-started-call?tabs=kotlin&pivots=platform-android).


### Prerequisites

- An Azure account with an active subscription. [Create an account for free](https://azure.microsoft.com/free/?WT.mc_id=A261C142F).
- An OS running [Android Studio](https://developer.android.com/studio).
- A deployed Communication Services resource. [Create a Communication Services resource](https://docs.microsoft.com/azure/communication-services/quickstarts/create-communication-resource).
- Azure Communication Services Token. [See example](https://docs.microsoft.com/azure/communication-services/tutorials/trusted-service-tutorial)

### Install the packages

In your app level (**app folder**) `build.gradle`, add the following lines to the dependencies and android sections.

```groovy
android {
    ...
    packagingOptions {
        pickFirst  'META-INF/*'
    }
    ...
}
```

```groovy
dependencies {
    ...
    implementation 'com.azure.android:azure-communication-ui-calling:1.1.0'
    ...
}
```

In your project gradle scripts add following lines to `repositories`. For `Android Studio (2020.*)` the `repositories` are in `settings.gradle` `dependencyResolutionManagement(Gradle version 6.8 or greater)`. If you are using old versions of `Android Studio (4.*)` then the `repositories` will be in project level `build.gradle` `allprojects{}`.

```groovy
repositories {
    ...
    mavenCentral()
    maven {
        url "https://pkgs.dev.azure.com/MicrosoftDeviceSDK/DuoSDK-Public/_packaging/Duo-SDK-Feed/maven/v1"
    }
    ...
}
```
Sync project with gradle files. (Android Studio -> File -> Sync Project With Gradle Files)


### Quick Sample 

Create `CallComposite` and launch it. Replace `<GROUP_CALL_ID>` with your group ID for your call, `<DISPLAY_NAME>` with your name, and  `<USER_ACCESS_TOKEN>` with your token. For full instructions check out our [quickstart](https://docs.microsoft.com/azure/communication-services/quickstarts/ui-library/get-started-composites?tabs=kotlin&pivots=platform-android) or get the completed [sample](https://github.com/Azure-Samples/communication-services-android-quickstarts/tree/main/ui-library-quick-start).

#### [Kotlin](#tab/kotlin)

```kotlin
val communicationTokenRefreshOptions = CommunicationTokenRefreshOptions({ "<USER_ACCESS_TOKEN>" }, true)
val communicationTokenCredential = CommunicationTokenCredential(communicationTokenRefreshOptions)

val locator: CallCompositeJoinLocator = CallCompositeGroupCallLocator(UUID.fromString("<GROUP_CALL_ID>"))
val remoteOptions = CallCompositeRemoteOptions(locator, communicationTokenCredential, "<DISPLAY_NAME>")
        
val callComposite: CallComposite = CallCompositeBuilder().build()
callComposite.launch(context, remoteOptions)
```

#### [Java](#tab/java)

```java
CommunicationTokenRefreshOptions communicationTokenRefreshOptions =
        new CommunicationTokenRefreshOptions(() -> "<USER_ACCESS_TOKEN>", true);

CommunicationTokenCredential communicationTokenCredential = 
        new CommunicationTokenCredential(communicationTokenRefreshOptions);

final CallCompositeJoinLocator locator =  new CallCompositeGroupCallLocator(UUID.fromString("<GROUP_CALL_ID>"));
final CallCompositeRemoteOptions remoteOptions =
                new CallCompositeRemoteOptions(locator, communicationTokenCredential, "<DISPLAY_NAME>");

CallComposite callComposite = new CallCompositeBuilder().build();
callComposite.launch(context, remoteOptions);
```

For more details on Mobile UI Library functionalities visit the [API Reference Documentation](https://azure.github.io/azure-sdk-for-android/azure-communication-mobileui/index.html).

### Accessibility

Previous Android API devices could perform accessibility differently comparing to the latest version. We ran through accessibility testing on previous Android API (21, 24, 27, 28) devices to detect the possible differences on accessibility performance.

#### [API 21](#tab/API21)
```API 21 
When focusing on buttons, screen reader will not announce "double tap to activate".
There is no initial focus on setup screen.
The state/selected change for audio device select menu and video/mic/switch camera buttons may not be announced.
The snackbar on setup screen with error message will not be focused and announced.
```

#### [API 27/28](#tab/API27_28)
``` API 27/28
The state/selected change for audio device select menu and video/mic/switch camera buttons may not be announced.
The snackbar on setup screen with error message may take more time to show up. 
```

## Contributing to the Library

Before developing and contributing to Communication Mobile UI Library, check out our [making a contribution guide](docs/contributing-guide.md).  
Included in this repository is a demo of using Mobile UI Library to start a call. You can find the detail of using and developing the UI Library in the [Demo Guide](azure-communication-ui/demo-app).

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/). For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments. Also, please check our [Contribution Policy](docs/contributing-guide.md). 

## Community Help and Support

If you find a bug or have a feature request, please raise the issue on [GitHub Issues](https://github.com/Azure/communication-ui-library-android/issues).

## Known Issues

Please refer to the [wiki](https://github.com/Azure/communication-ui-library-android/wiki/Known-Issues) for known issues related to the library.

## Ongoing work

The chat experience is a work in progress, please be aware that chat and callwithchat components are not complete and may not be in a working state. No support or assurances are provided at this time.

## Further Reading

* [Azure Communication UI Library Conceptual Documentation](https://docs.microsoft.com/azure/communication-services/concepts/ui-framework/ui-sdk-overview)
* [Azure Communication Service](https://docs.microsoft.com/en-us/azure/communication-services/overview)
* [Azure Communication Client and Server Architecture](https://docs.microsoft.com/en-us/azure/communication-services/concepts/client-and-server-architecture)
* [Azure Communication Authentication](https://docs.microsoft.com/en-us/azure/communication-services/concepts/authentication)
* [Azure Communication Service Troubleshooting](https://docs.microsoft.com/en-us/azure/communication-services/concepts/troubleshooting-info)
* [Azure Communication Service UI Calling Library Maven Releases](https://search.maven.org/artifact/com.azure.android/azure-communication-ui-calling)
* [Azure Communication Service Android Calling Hero Sample](https://github.com/Azure-Samples/communication-services-android-calling-hero)
