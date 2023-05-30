package com.danielsanrocha.porygon.filters

import com.danielsanrocha.porygon.commons.Security
import com.danielsanrocha.porygon.models.internals.Payload
import com.danielsanrocha.porygon.models.responses.Message
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.context.Contexts
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future
import com.twitter.util.logging.Logger

class AuthenticationFilter(implicit val security: Security) extends SimpleFilter[Request, Response] {
  private val logging: Logger = Logger(this.getClass)
  private val jsonMapper = JsonMapper.builder().addModule(DefaultScalaModule).build()

  override def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    request.headerMap.get("Authorization") match {
      case None =>
        logging.warn("Authorization header missing! Forbidden!")
        val forbiddenResponse = Response()
        forbiddenResponse.statusCode = 403
        forbiddenResponse.setContentTypeJson()
        forbiddenResponse.setContentString(jsonMapper.writeValueAsString(Message("Missing Authorization header")))
        Future(forbiddenResponse)
      case Some(header) =>
        try {
          val decrypted = security.decrypt(header)
          val payload = jsonMapper.readValue(decrypted, classOf[Payload])
          Contexts.local.let(Payload, payload) { service(request) }
        } catch {
          case _: Throwable =>
            logging.warn("Invalid Authorization header! Forbidden!")
            val forbiddenResponse = Response()
            forbiddenResponse.statusCode = 403
            forbiddenResponse.setContentTypeJson()
            forbiddenResponse.setContentString(jsonMapper.writeValueAsString(Message("Authorization header invalid")))
            Future(forbiddenResponse)
        }
    }
  }
}
