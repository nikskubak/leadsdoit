package com.respire.mvi.domain.model

data class SettingsEntity(
    val themeMode: Int? = null,
    val isNotificationEnabled: Boolean? = null,
    val privacyPolicyUrl: String? = null
)
