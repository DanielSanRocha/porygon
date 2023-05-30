package com.danielsanrocha.porygon.models.requests

import com.twitter.finatra.http.annotations.RouteParam

case class FilenameRequest(
    @RouteParam filename: String
)
