package com.danielsanrocha.porygon

import com.danielsanrocha.porygon.commons.Security
import com.danielsanrocha.porygon.controllers.{
  AdvertiserController,
  CreativeController,
  DownloadController,
  HealthcheckController,
  LoginController,
  NotFoundController,
  OptionsController,
  SlotController,
  UploadController,
  UserController
}
import com.danielsanrocha.porygon.filters.{AuthenticationFilter, CORSFilter, ExceptionHandlerFilter}
import com.danielsanrocha.porygon.repositories.{AdvertiserRepository, CreativeRepository, SlotRepository, UploadRepository, UserRepository}
import com.danielsanrocha.porygon.services.{AdvertiserService, CreativeService, SlotService, UploadService, UserService}
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

  private val uploadFolder = conf.getString("api.upload.folder")

  logging.info("Creating Repositories...")
  implicit private val uploadRepository: UploadRepository = new UploadRepository(uploadFolder)
  implicit private val userRepository: UserRepository = new UserRepository()
  implicit private val advertiserRepository: AdvertiserRepository = new AdvertiserRepository()
  implicit private val creativeRepository: CreativeRepository = new CreativeRepository()
  implicit private val slotRepository: SlotRepository = new SlotRepository()

  logging.info("Creating services...")
  implicit private val uploadService: UploadService = new UploadService()
  implicit private val userService: UserService = new UserService()
  implicit private val advertiserService: AdvertiserService = new AdvertiserService()
  implicit private val creativeService: CreativeService = new CreativeService()
  implicit private val slotService: SlotService = new SlotService()

  logging.info("Creating controllers...")
  private val healthcheckController = new HealthcheckController()
  private val optionsController = new OptionsController()
  private val loginController = new LoginController()
  private val userController = new UserController()
  private val notFoundController = new NotFoundController()
  private val uploadController = new UploadController()
  private val advertiserController = new AdvertiserController()
  private val creativeController = new CreativeController()
  private val downloadController = new DownloadController()
  private val slotController = new SlotController()

  logging.info("Creating filters...")
  private val authenticationFilter = new AuthenticationFilter()
  private val corsFilter = new CORSFilter()
  private val exceptionFilter = new ExceptionHandlerFilter()

  override protected def configureHttp(router: HttpRouter): Unit = {
    router.filter(corsFilter)
    router.filter(exceptionFilter)
    router.add(loginController)
    router.add(healthcheckController)
    router.add(authenticationFilter, uploadController)
    router.add(authenticationFilter, userController)
    router.add(authenticationFilter, advertiserController)
    router.add(authenticationFilter, creativeController)
    router.add(authenticationFilter, slotController)
    router.add(downloadController)
    router.add(notFoundController)
    router.add(optionsController)
  }
}
