// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui.chatdemoapp

import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azure.android.communication.ui.chatdemoapp.launcher.ChatCompositeJavaLauncher
import com.azure.android.communication.ui.chatdemoapp.launcher.ChatCompositeKotlinLauncher
import com.azure.android.communication.ui.chatdemoapp.launcher.ChatCompositeLauncher
import com.azure.android.communication.ui.demoapp.UrlTokenFetcher
import java.util.concurrent.Callable

class ChatLauncherViewModel : ViewModel() {
    private var token: String? = null
    private val fetchResultInternal = MutableLiveData<Result<ChatCompositeLauncher?>>()

    val fetchResult: LiveData<Result<ChatCompositeLauncher?>> = fetchResultInternal
    var isKotlinLauncher = true; private set
    var isTokenFunctionOptionSelected = false; private set

    private fun launcher(tokenRefresher: Callable<String>) = if (isKotlinLauncher) {
        ChatCompositeKotlinLauncher(tokenRefresher)
    } else {
        ChatCompositeJavaLauncher(tokenRefresher)
    }

    fun destroy() {
        fetchResultInternal.value = Result.success(null)
    }

    fun setJavaLauncher() {
        isKotlinLauncher = false
    }

    fun setKotlinLauncher() {
        isKotlinLauncher = true
    }

    fun doLaunch(tokenFunctionURL: String, acsToken: String) {
        when {
            isTokenFunctionOptionSelected && urlIsValid(tokenFunctionURL) -> {
                token = null
                fetchResultInternal.postValue(
                    Result.success(
                        launcher(
                            UrlTokenFetcher(tokenFunctionURL)
                        )
                    )
                )
            }
            acsToken.isNotBlank() -> {
                token = acsToken
                fetchResultInternal.postValue(
                    Result.success(launcher(CachedTokenFetcher(acsToken)))
                )
            }
            else -> {
                fetchResultInternal.postValue(
                    Result.failure(IllegalStateException("Invalid Token function URL or acs Token"))
                )
            }
        }
    }

    private fun urlIsValid(url: String) = url.isNotBlank() && URLUtil.isValidUrl(url.trim())

    fun useTokenFunction() {
        isTokenFunctionOptionSelected = true
    }

    fun useAcsToken() {
        isTokenFunctionOptionSelected = false
    }
}