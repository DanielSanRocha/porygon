package com.danielsanrocha.porygon.services

import com.danielsanrocha.porygon.models.internals.{NewSlot, Slot, UpdateSlot}
import com.danielsanrocha.porygon.repositories.SlotRepository
import com.twitter.util.logging.Logger

import scala.concurrent.Future

class SlotService(implicit val repository: SlotRepository) {
  private val logging: Logger = Logger(this.getClass)

  logging.info("Creating SlotService...")

  def getAll(offset: Long, limit: Long): Future[Seq[Slot]] = {
    repository.getAll(offset, limit)
  }

  def getById(id: Long): Future[Option[Slot]] = {
    repository.getById(id)
  }

  def create(slot: NewSlot): Future[Long] = {
    repository.create(slot)
  }

  def update(id: Long, slot: UpdateSlot): Future[Unit] = {
    repository.update(id, slot)
  }

  def delete(id: Long): Future[Unit] = {
    repository.delete(id)
  }
}
