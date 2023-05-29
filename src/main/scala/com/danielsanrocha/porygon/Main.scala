package com.danielsanrocha.porygon

import com.danielsanrocha.porygon.commons.Security
import com.twitter.util.logging.Logger
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.io.Source

object Main extends App {
  private val usage = """
  Usage

  start: Start the server
  createTables: Create tables on the database
  hash: Hash a password
  """

  private val logging: Logger = Logger(this.getClass)

  if (args.length == 0) {
    println(usage)
  } else {
    implicit val client: Database = Database.forConfig("api.mysql")

    args(0) match {
      case "start" =>
        logging.info("Loading slick MySQLClient...")
        implicit val ec: ExecutionContext = ExecutionContext.global

        logging.info("Starting server...")
        val server = new PorygonServer()
        server.main(args)
      case "createTables" =>
        logging.info("Creating tables...")

        logging.info("Creating tb_users table...")
        val userQuery = Source.fromResource("queries/CreateUsersTable.sql").mkString
        Await.result(client.run(sqlu"#$userQuery"), Duration.Inf)
        logging.info("created!")

      case "hash" =>
        args.length match {
          case 2 => println(s"Hash: ${Security.hash(args(1))}")
          case _ => println("Missing parameters or too much parameters to function hash. Ex: java -jar main.jar hash <password>")
        }
    }
  }
}
