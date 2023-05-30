package com.danielsanrocha.porygon.services

import com.danielsanrocha.porygon.models.internals.{Creative, NewCreative, UpdateCreative}
import com.danielsanrocha.porygon.repositories.CreativeRepository
import com.twitter.util.logging.Logger

import scala.concurrent.Future

class CreativeService(implicit val repository: CreativeRepository) {
  private val logging: Logger = Logger(this.getClass)

  logging.info("Creating CreativeService...")

  def getAll(offset: Long, limit: Long): Future[Seq[Creative]] = {
    repository.getAll(offset, limit)
  }

  def getById(id: Long): Future[Option[Creative]] = {
    repository.getById(id)
  }

  def create(creative: NewCreative): Future[Long] = {
    repository.create(creative)
  }

  def update(id: Long, creative: UpdateCreative): Future[Unit] = {
    repository.update(id, creative)
  }

  def delete(id: Long): Future[Unit] = {
    repository.delete(id)
  }
}
