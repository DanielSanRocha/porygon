package com.danielsanrocha.porygon.filters

import com.danielsanrocha.porygon.exceptions.{BadRequestError, ForbiddenError, InternalServerError, NotFoundError}
import com.danielsanrocha.porygon.models.responses.Message
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finatra.jackson.caseclass.exceptions.CaseClassMappingException
import com.twitter.util.Future
import com.twitter.util.logging.Logger

class ExceptionHandlerFilter() extends SimpleFilter[Request, Response] {
  private val logging: Logger = Logger(this.getClass)
  private val jsonMapper = JsonMapper.builder().addModule(DefaultScalaModule).build();

  override def apply(request: Request, service: Service[Request, Response]): Future[Response] = {

    service(request) rescue { case e: Throwable =>
      logging.error(s"Exception: ${e.getMessage}")

      val statusCode = e match {
        case _: InternalServerError       => 500
        case _: ForbiddenError            => 403
        case _: NotFoundError             => 404
        case _: BadRequestError           => 400
        case _: JsonParseException        => 400
        case _: NumberFormatException     => 400
        case _: CaseClassMappingException => 400
        case _: Exception                 => 500
      }

      val errorResponse = Response()
      errorResponse.statusCode = statusCode
      errorResponse.setContentTypeJson()
      errorResponse.setContentString(jsonMapper.writeValueAsString(Message(e.getMessage)))
      Future(errorResponse)
    }
  }
}
