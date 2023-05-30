package com.danielsanrocha.porygon.models.requests

import com.twitter.finatra.http.annotations.RouteParam

case class UpdateAdvertiserRequest(
    @RouteParam id: Long,
    name: String,
    description: String
)
