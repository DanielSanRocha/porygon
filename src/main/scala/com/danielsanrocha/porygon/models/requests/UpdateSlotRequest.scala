package com.danielsanrocha.porygon.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.twitter.finatra.http.annotations.RouteParam

case class UpdateSlotRequest(
    @RouteParam id: Long,
    name: String,
    description: String,
    width: Int,
    height: Int
)
