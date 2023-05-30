package com.danielsanrocha.porygon.models.internals

import java.sql.Timestamp

case class Creative(
    id: Long,
    name: String,
    filename: String,
    description: String,
    width: Int,
    height: Int,
    createDate: Timestamp,
    updateDate: Timestamp
)
