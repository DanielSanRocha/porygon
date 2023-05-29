package com.danielsanrocha.porygon.controllers

import com.danielsanrocha.porygon.models.internals.Payload
import com.danielsanrocha.porygon.models.responses.{Message, UserResponse}
import com.danielsanrocha.porygon.services.UserService
import com.twitter.finagle.context.Contexts
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

import scala.concurrent.ExecutionContext

class UserController(implicit val service: UserService, implicit val ec: ExecutionContext) extends Controller {
  get("/user") { _: Request =>
    val payload = Contexts.local.get(Payload).head
    service.getByEmail(payload.email) map {
      case Some(user) => response.ok(UserResponse(user))
      case None       => response.internalServerError(Message("We did not find you in the server. Maybe you were deleted?"))
    }
  }
}
