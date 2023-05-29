package com.danielsanrocha.porygon.models.internals

import com.twitter.finagle.context.Contexts

import java.sql.Timestamp

case class Payload(
    userId: Long,
    email: String,
    createdAt: Timestamp
)

object Payload extends Contexts.local.Key[Payload]
