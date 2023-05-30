package com.danielsanrocha.porygon.controllers

import com.danielsanrocha.porygon.models.requests.FilenameRequest
import com.danielsanrocha.porygon.services.UploadService
import com.twitter.finatra.http.Controller

import scala.concurrent.ExecutionContext

class DownloadController(implicit val service: UploadService, implicit val ec: ExecutionContext) extends Controller {
  get("/download/:filename") { request: FilenameRequest =>
    service.load(request.filename) map { data =>
      response.ok(data)
    }
  }
}
