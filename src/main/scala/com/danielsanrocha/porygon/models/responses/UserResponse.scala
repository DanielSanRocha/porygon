package com.danielsanrocha.porygon.models.responses

import com.danielsanrocha.porygon.models.internals.User

import java.sql.Timestamp

case class UserResponse(
    id: Long,
    name: String,
    email: String,
    createdAt: Timestamp
)

object UserResponse {
  def apply(user: User): UserResponse = UserResponse(user.id, user.name, user.email, user.createDate)
}
