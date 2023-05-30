package com.danielsanrocha.porygon.models.internals

import java.sql.Timestamp

case class NewSlot(
    name: String,
    description: String,
    width: Int,
    height: Int
)
