package com.danielsanrocha.porygon.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

case class SlotRequest(
    name: String,
    description: String,
    width: Int,
    height: Int
)
