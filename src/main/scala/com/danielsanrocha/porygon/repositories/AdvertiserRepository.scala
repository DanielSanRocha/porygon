package com.danielsanrocha.porygon.repositories

import com.danielsanrocha.porygon.models.internals.{Advertiser, NewAdvertiser, UpdateAdvertiser}
import com.twitter.util.logging.Logger
import slick.jdbc.MySQLProfile.api._

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

class AdvertiserRepository(implicit val client: Database, implicit val ec: ExecutionContext) {
  private val logging: Logger = Logger(this.getClass)

  logging.info("Creating AdvertiserRepository...")

  class AdvertiserTable(tag: Tag) extends Table[Advertiser](tag, "tb_advertisers") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("name")
    def description: Rep[String] = column[String]("description")
    def createDate: Rep[Timestamp] = column[Timestamp]("create_date")
    def updateDate: Rep[Timestamp] = column[Timestamp]("update_date")

    def * = (id, name, description, createDate, updateDate) <> (Advertiser.tupled, Advertiser.unapply)
  }

  private lazy val advertisers = TableQuery[AdvertiserTable]

  def getById(id: Long): Future[Option[Advertiser]] = {
    logging.debug(s"Searching for advertiser with id $id...")
    client.run(advertisers.filter(_.id === id).result.headOption)
  }

  def getAll(offset: Long, limit: Long): Future[Seq[Advertiser]] = {
    logging.debug(s"Getting advertisers page with offset $offset and limit $limit...")
    client.run(advertisers.take(limit).drop(offset).result)
  }

  def create(advertiser: NewAdvertiser): Future[Long] = {
    client.run(
      (advertisers.map(s => (s.name, s.description)) returning advertisers.map(_.id)) +=
        ((advertiser.name, advertiser.description))
    )
  }

  def update(id: Long, advertiser: UpdateAdvertiser): Future[Unit] = {
    client.run(advertisers.filter(_.id === id).map(s => (s.name, s.description)).update(advertiser.name, advertiser.description)) map { _ => }
  }

  def delete(id: Long): Future[Unit] = {
    client.run(advertisers.filter(_.id === id).delete) map { _ => }
  }
}
