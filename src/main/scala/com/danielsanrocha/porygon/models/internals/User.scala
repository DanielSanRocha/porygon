package com.danielsanrocha.porygon.models.internals

import java.sql.Timestamp

case class User(
    id: Long,
    name: String,
    email: String,
    password: String,
    createDate: Timestamp,
    updateDate: Timestamp
)
