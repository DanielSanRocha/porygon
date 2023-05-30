package com.danielsanrocha.porygon.models.internals

import java.sql.Timestamp

case class Slot(
    id: Long,
    name: String,
    description: String,
    width: Int,
    height: Int,
    createDate: Timestamp,
    updateDate: Timestamp
)
