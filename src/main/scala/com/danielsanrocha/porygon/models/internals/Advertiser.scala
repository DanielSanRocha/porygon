package com.danielsanrocha.porygon.models.internals

import java.sql.Timestamp

case class Advertiser(
    id: Long,
    name: String,
    description: String,
    createDate: Timestamp,
    updateDate: Timestamp
)
