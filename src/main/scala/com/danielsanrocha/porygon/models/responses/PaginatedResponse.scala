package com.danielsanrocha.porygon.models.responses

case class PaginatedResponse[T](
    data: Seq[T],
    count: Long
)
