package com.danielsanrocha.porygon.controllers

import com.danielsanrocha.porygon.models.internals.{NewCreative, UpdateCreative}
import com.danielsanrocha.porygon.models.requests.{CreativeRequest, IdRequest, PaginatedRequest, UpdateCreativeRequest}
import com.danielsanrocha.porygon.models.responses.{Created, Message, PaginatedResponse}
import com.danielsanrocha.porygon.services.CreativeService
import com.twitter.finatra.http.Controller
import com.twitter.util.logging.Logger

import scala.concurrent.ExecutionContext

class CreativeController(implicit val service: CreativeService, implicit val ec: ExecutionContext) extends Controller {
  private val logging: Logger = Logger(this.getClass)

  get("/api/creative/:id") { request: IdRequest =>
    logging.debug(s"GET /api/creative/${request.id} called!")
    service.getById(request.id) map {
      case Some(adv) => response.ok(adv)
      case None      => response.notFound(Message(s"Creative with id ${request.id} not found."))
    }
  }

  get("/api/creatives") { request: PaginatedRequest =>
    logging.debug("GET /api/creatives called!")
    service.getAll(request.offset, request.limit) map { creatives =>
      PaginatedResponse(creatives, creatives.length)
    }
  }

  post("/api/creative") { request: CreativeRequest =>
    logging.debug("POST /api/creative called!")
    service.create(NewCreative(request.name, request.filename, request.description, request.width, request.height)) map { id =>
      logging.debug("New creative created with success!")
      response.ok(Created(id))
    }
  }

  put("/api/creative/:id") { request: UpdateCreativeRequest =>
    logging.debug(s"PUT /api/creative/${request.id} called!")
    service.update(request.id, UpdateCreative(request.name, request.filename, request.description, request.width, request.height)) map { _ =>
      logging.debug("Creative updated with success!")
      response.ok(Message("updated"))
    }
  }

  delete("/api/creative/:id") { request: IdRequest =>
    logging.debug(s"DELETE /api/creative/${request.id} called!")
    service.delete(request.id) map { _ =>
      logging.debug("Creative deleted with success!")
      response.ok(Message("deleted"))
    }
  }
}
