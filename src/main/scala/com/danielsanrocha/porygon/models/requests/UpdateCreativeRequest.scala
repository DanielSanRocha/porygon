package com.danielsanrocha.porygon.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.twitter.finatra.http.annotations.RouteParam

case class UpdateCreativeRequest(
    @RouteParam id: Long,
    @JsonProperty("id_advertiser") idAdvertiser: Long,
    name: String,
    filename: String,
    description: String,
    width: Int,
    height: Int
)
