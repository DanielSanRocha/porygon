package com.danielsanrocha.porygon.controllers

import com.danielsanrocha.porygon.models.internals.{NewSlot, UpdateSlot}
import com.danielsanrocha.porygon.models.requests.{SlotRequest, IdRequest, PaginatedRequest, UpdateSlotRequest}
import com.danielsanrocha.porygon.models.responses.{Created, Message, PaginatedResponse}
import com.danielsanrocha.porygon.services.SlotService
import com.twitter.finatra.http.Controller
import com.twitter.util.logging.Logger

import scala.concurrent.ExecutionContext

class SlotController(implicit val service: SlotService, implicit val ec: ExecutionContext) extends Controller {
  private val logging: Logger = Logger(this.getClass)

  get("/api/slot/:id") { request: IdRequest =>
    logging.debug(s"GET /api/slot/${request.id} called!")
    service.getById(request.id) map {
      case Some(adv) => response.ok(adv)
      case None      => response.notFound(Message(s"Slot with id ${request.id} not found."))
    }
  }

  get("/api/slots") { request: PaginatedRequest =>
    logging.debug("GET /api/slots called!")
    service.getAll(request.offset, request.limit) map { slots =>
      PaginatedResponse(slots, slots.length)
    }
  }

  post("/api/slot") { request: SlotRequest =>
    logging.debug("POST /api/slot called!")
    service.create(NewSlot(request.name, request.description, request.width, request.height)) map { id =>
      logging.debug("New slot created with success!")
      response.ok(Created(id))
    }
  }

  put("/api/slot/:id") { request: UpdateSlotRequest =>
    logging.debug(s"PUT /api/slot/${request.id} called!")
    service.update(request.id, UpdateSlot(request.name, request.description, request.width, request.height)) map { _ =>
      logging.debug("Slot updated with success!")
      response.ok(Message("updated"))
    }
  }

  delete("/api/slot/:id") { request: IdRequest =>
    logging.debug(s"DELETE /api/slot/${request.id} called!")
    service.delete(request.id) map { _ =>
      logging.debug("Slot deleted with success!")
      response.ok(Message("deleted"))
    }
  }
}
