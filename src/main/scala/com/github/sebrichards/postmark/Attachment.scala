package com.github.sebrichards.postmark

import java.io.File

import java.net.URLConnection

import org.apache.commons.io.FileUtils
import org.apache.commons.codec.binary.Base64

/** An e-mail attachment */
case class Attachment(

  /** Filename, including extension */
  Name: String,

  /** Mime-type */
  ContentType: String,

  /** Base64 encoded data */
  Content: String,

  /** ID, for inline images */
  ContentID: Option[String] = None)

object Attachment {

  /** Create an attachment from a file */
  def apply(file: File) = {

    val filename: String = file.getName

    val contentType: String =
      Option(URLConnection.guessContentTypeFromName(filename)).
      getOrElse("application/octet-stream")

    val bytes: Array[Byte] = FileUtils.readFileToByteArray(file)
    val bytesString: String = Base64.encodeBase64String(bytes)

    new Attachment(filename, contentType, bytesString)
  }

}