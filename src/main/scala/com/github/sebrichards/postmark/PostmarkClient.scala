package com.github.sebrichards.postmark

import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils

import org.json4s._
import org.json4s.jackson.Serialization

import org.slf4j.LoggerFactory

import com.github.sebrichards.postmark.util.DateTimeSerializer
import java.nio.charset.StandardCharsets

/**
 * A client for Postmark.
 * 
 * @param serverToken The API key
 */
class PostmarkClient(serverToken: String) {

  protected val logger = LoggerFactory.getLogger(getClass)

  private val postUrl = "http://api.postmarkapp.com/email"

  private implicit val formats = Serialization.formats(NoTypeHints) + DateTimeSerializer

  protected val client: HttpClient = new DefaultHttpClient

  /** Send an e-mail */
  def send(message: PostmarkMessage): Either[PostmarkError, PostmarkSuccess] = {

    // Create post request
    val httpPost = new HttpPost(postUrl)

    // Add headers
    httpPost.addHeader("Accept", "application/json")
    httpPost.addHeader("Content-Type", "application/json; charset=utf-8")
    httpPost.addHeader("X-Postmark-Server-Token", serverToken)

    // Add message
    val messageJson = Serialization.write(message)
    httpPost.setEntity(new StringEntity(messageJson, StandardCharsets.UTF_8))

    // Execute
    val httpResponse = client.execute(httpPost)

    // Get HTTP status code
    val statusCode = httpResponse.getStatusLine.getStatusCode

    // Read body as string
    val responseBody = EntityUtils.toString(httpResponse.getEntity)

    // Process
    statusCode match {

      // OK
      case 200 => {
        val success = Serialization.read[PostmarkSuccess](responseBody)

        logger.info("Sent e-mail to " + message.To + " [" + success.MessageID + "]")

        Right(success)
      }

      // Unauthorized / Unprocessable Entity
      case 401 | 422 => {
        val error = Serialization.read[PostmarkError](responseBody)

        logger.error("Unable to send e-mail to " + message.To + " - (" + error.ErrorCode + ") " + error.Message)

        Left(error)
      }

      // Server error (500, 502, etc)
      case sc => {
        val error = PostmarkError(sc, httpResponse.getStatusLine.getReasonPhrase)

        logger.error("Unable to send e-mail to " + message.To + " - (" + error.ErrorCode + ") " + error.Message)

        Left(error)
      }
    }
  }

  /** Clean up resources */
  def destroy {
    client.getConnectionManager.shutdown
  }

}