package com.danielsanrocha.porygon.controllers

import java.time.LocalDateTime
import com.danielsanrocha.porygon.commons.Security
import com.danielsanrocha.porygon.models.internals.Payload
import com.danielsanrocha.porygon.models.requests.Credential
import com.danielsanrocha.porygon.models.responses.{Message, Token}
import com.danielsanrocha.porygon.services.UserService
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.twitter.finatra.http.Controller
import com.twitter.util.logging.Logger

import java.sql.Timestamp
import scala.concurrent.ExecutionContext

class LoginController(implicit val service: UserService, implicit val ec: ExecutionContext, implicit val security: Security) extends Controller {
  private val logging: Logger = Logger(this.getClass)
  private val jsonMapper = JsonMapper.builder().addModule(DefaultScalaModule).build()

  post("/login") { credential: Credential =>
    logging.debug("POST /login called")
    service.getByEmail(credential.email) map {
      case Some(user) =>
        Security.hash(credential.password) match {
          case user.password =>
            logging.debug("Valid password!")
            val payload = Payload(user.id, user.email, Timestamp.valueOf(LocalDateTime.now()))
            val payloadString = jsonMapper.writeValueAsString(payload)
            val payloadCrypto = security.encrypt(payloadString)
            response.ok(Token(payloadCrypto))
          case _ =>
            logging.debug("Invalid password!")
            response.forbidden(Message("Invalid password!"))
        }
      case None =>
        logging.debug(s"User with email '${credential.email}' not found")
        response.notFound(Message(s"User with email '${credential.email}' not found =("))
    }
  }
}
