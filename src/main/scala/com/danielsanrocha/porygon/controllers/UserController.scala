package com.danielsanrocha.porygon.controllers

import com.danielsanrocha.porygon.models.internals.Payload
import com.danielsanrocha.porygon.models.requests.{IdRequest, PaginatedRequest}
import com.danielsanrocha.porygon.models.responses.{Message, PaginatedResponse, UserResponse}
import com.danielsanrocha.porygon.services.UserService
import com.twitter.finagle.context.Contexts
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.logging.Logger

import scala.concurrent.ExecutionContext

class UserController(implicit val service: UserService, implicit val ec: ExecutionContext) extends Controller {
  private val logging: Logger = Logger(this.getClass)

  get("/api/user/:id") { request: IdRequest =>
    logging.debug(s"GET /api/user/${request.id} called!")
    service.getById(request.id) map {
      case Some(user) => response.ok(UserResponse(user))
      case None       => response.notFound(Message(s"User with id ${request.id} not found."))
    }
  }

  get("/api/user") { _: Request =>
    logging.debug("GET /api/user called!")
    val payload = Contexts.local.get(Payload).head
    service.getByEmail(payload.email) map {
      case Some(user) => response.ok(UserResponse(user))
      case None       => response.internalServerError(Message("We did not find you in the server. Maybe you were deleted?"))
    }
  }

  get("/api/users") { request: PaginatedRequest =>
    logging.debug("GET /api/users called!")
    service.getAll(request.offset, request.limit) map { users =>
      PaginatedResponse(users map { u => UserResponse(u) }, users.length)
    }
  }
}
