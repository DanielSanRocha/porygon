package com.danielsanrocha.porygon.services

import com.danielsanrocha.porygon.models.internals.User
import com.danielsanrocha.porygon.repositories.UserRepository
import com.twitter.util.logging.Logger

import scala.concurrent.Future

class UserService(implicit val repository: UserRepository) {
  private val logging: Logger = Logger(this.getClass)

  logging.info("Creating UserService...")

  def getByEmail(email: String): Future[Option[User]] = {
    repository.getByEmail(email)
  }

  def getAll(offset: Long, limit: Long): Future[Seq[User]] = {
    repository.getAll(offset, limit)
  }

  def getById(id: Long): Future[Option[User]] = {
    repository.getById(id)
  }
}
