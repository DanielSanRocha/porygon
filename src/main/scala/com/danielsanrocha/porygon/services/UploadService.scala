package com.danielsanrocha.porygon.services

import com.danielsanrocha.porygon.exceptions.{BadRequestError, NotFoundError}
import com.danielsanrocha.porygon.models.internals.Upload
import com.danielsanrocha.porygon.repositories.UploadRepository
import com.twitter.util.logging.Logger

import java.io.{ByteArrayInputStream, FileNotFoundException}
import javax.imageio.ImageIO
import scala.concurrent.{ExecutionContext, Future}

class UploadService(implicit val repository: UploadRepository, implicit val ec: ExecutionContext) {
  private val logging: Logger = Logger(this.getClass)

  logging.info("Creating UploadService...")

  def save(originalFilename: String, data: Array[Byte]): Future[Upload] = {
    Future {
      val extension = originalFilename.split("\\.").last
      val filename = s"${java.util.UUID.randomUUID.toString}.$extension"
      try {
        val is = new ByteArrayInputStream(data)
        val bi = ImageIO.read(is)

        val upload = Upload(filename, bi.getWidth, bi.getHeight)
        repository.save(filename, data)
        upload
      } catch {
        case _: Throwable =>
          throw new BadRequestError("file is not a valid image")
      }
    }
  }

  def load(filename: String): Future[Array[Byte]] = {
    Future {
      try {
        repository.load(filename)
      } catch {
        case _: FileNotFoundException => throw new NotFoundError("file not found.")
      }
    }
  }
}
