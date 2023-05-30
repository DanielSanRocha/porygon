package com.danielsanrocha.porygon.models.requests

import com.twitter.finatra.http.annotations.RouteParam

case class UpdateCreativeRequest(
    @RouteParam id: Long,
    name: String,
    filename: String,
    description: String,
    width: Int,
    height: Int
)
