package com.danielsanrocha.porygon.controllers

import com.danielsanrocha.porygon.models.responses.Message
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class NotFoundController extends Controller {
  get("/:*") { _: Request => response.notFound(Message("Route not found!")) }
  post("/:*") { _: Request => response.notFound(Message("Route not found!")) }
  put("/:*") { _: Request => response.notFound(Message("Route not found!")) }
  delete("/:*") { _: Request => response.notFound(Message("Route not found!")) }
}
