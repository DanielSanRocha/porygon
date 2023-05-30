package com.danielsanrocha.porygon.models.requests

import com.twitter.finatra.http.annotations.QueryParam

case class PaginatedRequest(
    @QueryParam limit: Int,
    @QueryParam offset: Int
)
