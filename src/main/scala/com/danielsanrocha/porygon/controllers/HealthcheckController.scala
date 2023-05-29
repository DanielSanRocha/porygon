package com.danielsanrocha.porygon.controllers

import com.danielsanrocha.porygon.models.responses.Message
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.logging.Logger

class HealthcheckController extends Controller {
  private val logging: Logger = Logger(this.getClass)

  get("/healthcheck") { _: Request =>
    logging.debug("Healthcheck called!")
    response.ok(Message("Ok"))
  }
}
