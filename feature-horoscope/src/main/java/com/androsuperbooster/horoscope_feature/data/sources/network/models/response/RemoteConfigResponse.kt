package com.androsuperbooster.horoscope_feature.data.sources.network.models.response

import com.androsuperbooster.horoscope_feature.domain.model.ZodiacEntity

data class RemoteConfigZodiacsResponse(val en: List<ZodiacEntity>, val tr: List<ZodiacEntity>)

data class RemoteConfigActionResponse(
    val id: String?,
    var details: String?,
    val action: ActionValue?,
    val accessOnlyFor: List<String>?
)

data class ActionValue(val tr: String, val en: String)
