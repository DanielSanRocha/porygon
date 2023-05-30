package com.danielsanrocha.porygon.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

case class CreativeRequest(
    @JsonProperty("id_advertiser") idAdvertiser: Long,
    name: String,
    filename: String,
    description: String,
    width: Int,
    height: Int
)
