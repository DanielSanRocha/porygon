package com.danielsanrocha.porygon.models.requests

case class CreativeRequest(
    name: String,
    filename: String,
    description: String,
    width: Int,
    height: Int
)
