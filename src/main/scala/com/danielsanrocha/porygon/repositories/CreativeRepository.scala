package com.danielsanrocha.porygon.repositories

import com.danielsanrocha.porygon.models.internals.{Creative, NewCreative, UpdateCreative}
import com.twitter.util.logging.Logger
import slick.jdbc.MySQLProfile.api._

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

class CreativeRepository(implicit val client: Database, implicit val ec: ExecutionContext) {
  private val logging: Logger = Logger(this.getClass)

  logging.info("Creating CreativeRepository...")

  class CreativeTable(tag: Tag) extends Table[Creative](tag, "tb_creatives") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("name")
    def filename: Rep[String] = column[String]("filename")
    def description: Rep[String] = column[String]("description")
    def width: Rep[Int] = column[Int]("width")
    def height: Rep[Int] = column[Int]("height")
    def createDate: Rep[Timestamp] = column[Timestamp]("create_date")
    def updateDate: Rep[Timestamp] = column[Timestamp]("update_date")

    def * = (id, name, filename, description, width, height, createDate, updateDate) <> (Creative.tupled, Creative.unapply)
  }

  private lazy val creatives = TableQuery[CreativeTable]

  def getById(id: Long): Future[Option[Creative]] = {
    logging.debug(s"Searching for creative with id $id...")
    client.run(creatives.filter(_.id === id).result.headOption)
  }

  def getAll(offset: Long, limit: Long): Future[Seq[Creative]] = {
    logging.debug(s"Getting creatives page with offset $offset and limit $limit...")
    client.run(creatives.take(limit).drop(offset).result)
  }

  def create(creative: NewCreative): Future[Long] = {
    client.run(
      (creatives.map(c => (c.name, c.filename, c.description, c.width, c.height)) returning creatives.map(_.id)) +=
        ((creative.name, creative.filename, creative.description, creative.width, creative.height))
    )
  }

  def update(id: Long, creative: UpdateCreative): Future[Unit] = {
    client.run(
      creatives
        .filter(_.id === id)
        .map(c => (c.name, c.filename, c.description, c.width, c.height))
        .update(creative.name, creative.filename, creative.description, creative.width, creative.height)
    ) map { _ => }
  }

  def delete(id: Long): Future[Unit] = {
    client.run(creatives.filter(_.id === id).delete) map { _ => }
  }
}
