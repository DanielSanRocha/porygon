package com.danielsanrocha.porygon.repositories

import com.danielsanrocha.porygon.models.internals.User
import com.twitter.util.logging.Logger
import slick.jdbc.MySQLProfile.api._

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

class UserRepository(implicit val client: Database, implicit val ec: ExecutionContext) {
  private val logging: Logger = Logger(this.getClass)

  logging.info("Creating UserRepository...")

  class UserTable(tag: Tag) extends Table[User](tag, "tb_users") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("name")
    def email: Rep[String] = column[String]("email")
    def password: Rep[String] = column[String]("password")
    def createDate: Rep[Timestamp] = column[Timestamp]("create_date")
    def updateDate: Rep[Timestamp] = column[Timestamp]("update_date")

    def * = (id, name, email, password, createDate, updateDate) <> (User.tupled, User.unapply)
  }

  private lazy val users = TableQuery[UserTable]

  def getByEmail(email: String): Future[Option[User]] = {
    logging.debug(s"Searching for user with email ${email}...")
    client.run(users.filter(_.email === email).result.headOption) map {
      case Some(user) =>
        logging.debug(s"Found user with email ${email}!")
        Some(user)
      case None =>
        logging.debug(s"User with email ${email} not found =(")
        None
    }
  }
}
