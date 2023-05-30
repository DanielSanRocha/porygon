package com.danielsanrocha.porygon.controllers

import com.danielsanrocha.porygon.models.responses.Message
import com.danielsanrocha.porygon.services.UploadService
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.http.request.RequestUtils

import scala.concurrent.{ExecutionContext, Future}

class UploadController(implicit val service: UploadService, implicit val ec: ExecutionContext) extends Controller {
  post("/upload") { request: Request =>
    RequestUtils.multiParams(request).get("file") match {
      case Some(item) =>
        item.filename match {
          case Some(filename) =>
            service.save(filename, item.data) map { upload =>
              response.ok(upload)
            }
          case None => Future { response.badRequest(Message("missing filename in multipart request!")) }
        }
      case None => Future { response.badRequest(Message("missing file in multipart request!")) }
    }
  }
}
