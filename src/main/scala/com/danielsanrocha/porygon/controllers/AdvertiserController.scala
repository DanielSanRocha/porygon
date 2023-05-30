package com.danielsanrocha.porygon.controllers

import com.danielsanrocha.porygon.models.internals.{NewAdvertiser, UpdateAdvertiser}
import com.danielsanrocha.porygon.models.requests.{AdvertiserRequest, IdRequest, PaginatedRequest, UpdateAdvertiserRequest}
import com.danielsanrocha.porygon.models.responses.{Created, Message, PaginatedResponse}
import com.danielsanrocha.porygon.services.AdvertiserService
import com.twitter.finatra.http.Controller
import com.twitter.util.logging.Logger

import scala.concurrent.ExecutionContext

class AdvertiserController(implicit val service: AdvertiserService, implicit val ec: ExecutionContext) extends Controller {
  private val logging: Logger = Logger(this.getClass)

  get("/api/advertiser/:id") { request: IdRequest =>
    logging.debug(s"GET /api/advertiser/${request.id} called!")
    service.getById(request.id) map {
      case Some(adv) => response.ok(adv)
      case None      => response.notFound(Message(s"Advertiser with id ${request.id} not found."))
    }
  }

  get("/api/advertisers") { request: PaginatedRequest =>
    logging.debug("GET /api/advertisers called!")
    service.getAll(request.offset, request.limit) map { advertisers =>
      PaginatedResponse(advertisers, advertisers.length)
    }
  }

  post("/api/advertiser") { request: AdvertiserRequest =>
    logging.debug("POST /api/advertiser called!")
    service.create(NewAdvertiser(request.name, request.description)) map { id =>
      logging.debug("New advertiser created with success!")
      response.ok(Created(id))
    }
  }

  put("/api/advertiser/:id") { request: UpdateAdvertiserRequest =>
    logging.debug(s"PUT /api/advertiser/${request.id} called!")
    service.update(request.id, UpdateAdvertiser(request.name, request.description)) map { _ =>
      logging.debug("Advertiser updated with success!")
      response.ok(Message("updated"))
    }
  }

  delete("/api/advertiser/:id") { request: IdRequest =>
    logging.debug(s"DELETE /api/advertiser/${request.id} called!")
    service.delete(request.id) map { _ =>
      logging.debug("Advertiser deleted with success!")
      response.ok(Message("deleted"))
    }
  }
}
