package com.danielsanrocha.porygon.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class OptionsController extends Controller {
  options("/:*") { _: Request => response.ok("*") }
}
