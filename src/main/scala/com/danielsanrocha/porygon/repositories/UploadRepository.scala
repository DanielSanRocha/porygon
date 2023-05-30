package com.danielsanrocha.porygon.repositories

import java.io.{BufferedInputStream, BufferedOutputStream, FileInputStream, FileOutputStream}
import java.nio.file.Paths

class UploadRepository(val folder: String) {
  def save(filename: String, data: Array[Byte]): Unit = {
    val fullpath = Paths.get(folder, filename).toAbsolutePath.toString
    val bos = new BufferedOutputStream(new FileOutputStream(fullpath))
    bos.write(data)
    bos.close()
  }

  def load(filename: String): Array[Byte] = {
    val fullpath = Paths.get(folder, filename).toAbsolutePath.toString
    val bis = new BufferedInputStream(new FileInputStream(fullpath))
    bis.readAllBytes()
  }
}
