package com.androsuperbooster.horoscope.data.sources.network.models.response

import com.androsuperbooster.horoscope.domain.model.ZodiacEntity

data class RemoteConfigZodiacsResponse(val en: List<ZodiacEntity>, val tr: List<ZodiacEntity>)

data class RemoteConfigActionResponse(val id: String?,
                  var details: String?,
                  val action: ActionValue?)

data class ActionValue(val tr : String, val en : String)
