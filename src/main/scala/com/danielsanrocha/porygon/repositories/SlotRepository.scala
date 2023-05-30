package com.danielsanrocha.porygon.repositories

import com.danielsanrocha.porygon.models.internals.{Slot, NewSlot, UpdateSlot}
import com.twitter.util.logging.Logger
import slick.jdbc.MySQLProfile.api._

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

class SlotRepository(implicit val client: Database, implicit val ec: ExecutionContext) {
  private val logging: Logger = Logger(this.getClass)

  logging.info("Creating SlotRepository...")

  class SlotTable(tag: Tag) extends Table[Slot](tag, "tb_slots") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("name")
    def description: Rep[String] = column[String]("description")
    def width: Rep[Int] = column[Int]("width")
    def height: Rep[Int] = column[Int]("height")
    def createDate: Rep[Timestamp] = column[Timestamp]("create_date")
    def updateDate: Rep[Timestamp] = column[Timestamp]("update_date")

    def * = (id, name, description, width, height, createDate, updateDate) <> (Slot.tupled, Slot.unapply)
  }

  private lazy val slots = TableQuery[SlotTable]

  def getById(id: Long): Future[Option[Slot]] = {
    logging.debug(s"Searching for slot with id $id...")
    client.run(slots.filter(_.id === id).result.headOption)
  }

  def getAll(offset: Long, limit: Long): Future[Seq[Slot]] = {
    logging.debug(s"Getting slots page with offset $offset and limit $limit...")
    client.run(slots.take(limit).drop(offset).result)
  }

  def create(slot: NewSlot): Future[Long] = {
    client.run(
      (slots.map(c => (c.name, c.description, c.width, c.height)) returning slots.map(_.id)) +=
        ((slot.name, slot.description, slot.width, slot.height))
    )
  }

  def update(id: Long, slot: UpdateSlot): Future[Unit] = {
    client.run(
      slots
        .filter(_.id === id)
        .map(s => (s.name, s.description, s.width, s.height))
        .update(slot.name, slot.description, slot.width, slot.height)
    ) map { _ => }
  }

  def delete(id: Long): Future[Unit] = {
    client.run(slots.filter(_.id === id).delete) map { _ => }
  }
}
