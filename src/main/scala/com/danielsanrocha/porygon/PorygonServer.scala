package com.danielsanrocha.porygon

import com.danielsanrocha.porygon.commons.Security
import com.danielsanrocha.porygon.controllers.{HealthcheckController, LoginController, OptionsController, UserController}
import com.danielsanrocha.porygon.filters.AuthenticationFilter
import com.danielsanrocha.porygon.repositories.UserRepository
import com.danielsanrocha.porygon.services.UserService
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.util.logging.Logger
import com.typesafe.config.{Config, ConfigFactory}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.ExecutionContext

class PorygonServer(implicit val client: Database, implicit val ec: ExecutionContext) extends HttpServer {
  private val logging: Logger = Logger(this.getClass)

  logging.info("Loading configurations...")
  private implicit val conf: Config = ConfigFactory.load()

  private val port = conf.getInt("api.port")
  private val serverName = conf.getString("api.name")
  private val adminDefaultPort = conf.getInt("api.admin_port")

  override protected def defaultHttpPort: String = s":${port}"
  override protected def defaultHttpServerName: String = serverName
  override def defaultAdminPort: Int = adminDefaultPort

  logging.info("Creating Security class")
  private val secret = conf.getString("api.secret")
  implicit private val security: Security = new Security(secret)

  logging.info("Creating Repositories...")
  implicit private val userRepository: UserRepository = new UserRepository()

  logging.info("Creating services...")
  implicit private val userService: UserService = new UserService()

  logging.info("Creating controllers...")
  private val healthcheckController = new HealthcheckController()
  private val optionsController = new OptionsController()
  private val loginController = new LoginController()
  private val userController = new UserController()

  logging.info("Creating filters...")
  private val authenticationFilter = new AuthenticationFilter()

  override protected def configureHttp(router: HttpRouter): Unit = {
    router.add(loginController)
    router.add(healthcheckController)
    router.add(optionsController)
    router.add(authenticationFilter, userController)
  }
}
