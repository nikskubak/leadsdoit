package com.androsuperbooster.horoscope_feature.data.repo

import android.content.Context
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.androsuperbooster.horoscope_feature.data.HawkKeys
import com.androsuperbooster.horoscope_feature.domain.repo.InstallReferrerRepository
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class InstallReferrerRepositoryImpl @Inject constructor(val context: Context) :
    InstallReferrerRepository {

    override fun getInstallReferrerParams(): Flow<Map<String, String>?> {
        return callbackFlow {
            val referrerClient = InstallReferrerClient.newBuilder(context).build()

            val listener = object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    when (responseCode) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            val installReferrer = referrerClient.installReferrer.installReferrer
                            Log.e("InstallReferrer", "$installReferrer")
                            if(!installReferrer.isNullOrEmpty()){
                                Hawk.put(HawkKeys.INSTALL_REFERRER, installReferrer)
                            }
                            trySend(parseReferrer(installReferrer))
                        }

                        InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                            Log.e("InstallReferrer", "FEATURE_NOT_SUPPORTED")
                            trySend(null)
                        }

                        InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                            Log.e("InstallReferrer", "SERVICE_UNAVAILABLE")
                            trySend(null)
                        }

                        else -> {
                            Log.e("InstallReferrer", "Unknown response code: $responseCode")
                            trySend(null)
                        }
                    }
                    referrerClient.endConnection()
                }

                override fun onInstallReferrerServiceDisconnected() {
                    Log.e("InstallReferrer", "onInstallReferrerServiceDisconnected")
                    trySend(null)
                }
            }

            referrerClient.startConnection(listener)

            awaitClose {
                // Clean up resources if needed when the flow is canceled
                referrerClient.endConnection()
            }
        }
    }

    fun parseReferrer(referrer: String): Map<String, String> {
        return referrer.split("&").mapNotNull {
            val pair = it.split("=")
            if (pair.size == 2) pair[0] to pair[1] else null
        }.toMap()
    }
}