package com.danielsanrocha.porygon.commons

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object Security {
  def hash(s: String): String = {
    val bytes = MessageDigest.getInstance("MD5").digest(s.getBytes)
    Base64.getEncoder.withoutPadding().encodeToString(bytes)
  }
}

class Security(secret: String) {
  private val secretHashed = Security.hash(secret).getBytes("UTF-8")
  private val key = util.Arrays.copyOf(secretHashed, 16)
  private val secretKey = new SecretKeySpec(key, "AES")

  def encrypt(s: String): String = {
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    Base64.getEncoder.encodeToString(cipher.doFinal(s.getBytes("UTF-8")))
  }

  def decrypt(s: String): String = {
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    new String(cipher.doFinal(Base64.getDecoder.decode(s)), StandardCharsets.UTF_8)
  }
}
