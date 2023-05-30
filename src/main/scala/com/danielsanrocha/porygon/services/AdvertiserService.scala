package com.danielsanrocha.porygon.services

import com.danielsanrocha.porygon.models.internals.{Advertiser, NewAdvertiser, UpdateAdvertiser}
import com.danielsanrocha.porygon.repositories.AdvertiserRepository
import com.twitter.util.logging.Logger

import scala.concurrent.Future

class AdvertiserService(implicit val repository: AdvertiserRepository) {
  private val logging: Logger = Logger(this.getClass)

  logging.info("Creating AdvertiserService...")

  def getAll(offset: Long, limit: Long): Future[Seq[Advertiser]] = {
    repository.getAll(offset, limit)
  }

  def getById(id: Long): Future[Option[Advertiser]] = {
    repository.getById(id)
  }

  def create(advertiser: NewAdvertiser): Future[Long] = {
    repository.create(advertiser)
  }

  def update(id: Long, advertiser: UpdateAdvertiser): Future[Unit] = {
    repository.update(id, advertiser)
  }

  def delete(id: Long): Future[Unit] = {
    repository.delete(id)
  }
}
