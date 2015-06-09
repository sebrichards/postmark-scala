package com.github.sebrichards.postmark

import java.nio.charset.StandardCharsets

import org.apache.http.client.HttpClient
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils

import org.json4s.Formats
import org.json4s.NoTypeHints
import org.json4s.native.Serialization

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.github.sebrichards.postmark.util.DateTimeSerializer

/**
 * A client for Postmark.
 *
 * @param serverToken The API key
 */
class PostmarkClient(serverToken: String) {

  protected val logger: Logger = LoggerFactory.getLogger(getClass)

  private val postUrl: String = "http://api.postmarkapp.com/email"

  private implicit val formats: Formats = Serialization.formats(NoTypeHints) + DateTimeSerializer

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
    val messageJson: String = Serialization.write(message)
    httpPost.setEntity(new StringEntity(messageJson, StandardCharsets.UTF_8))

    // Execute
    val httpResponse: HttpResponse = client.execute(httpPost)

    // Get HTTP status code
    val statusCode: Int = httpResponse.getStatusLine.getStatusCode

    // Read body as string
    val responseBody: String = EntityUtils.toString(httpResponse.getEntity)

    // Process
    statusCode match {

      // OK
      case 200 => {
        val success: PostmarkSuccess = Serialization.read[PostmarkSuccess](responseBody)

        logger.info("Sent e-mail to " + message.To + " [" + success.MessageID + "]")

        Right(success)
      }

      // Unauthorized / Unprocessable Entity
      case 401 | 422 => {
        val error: PostmarkError = Serialization.read[PostmarkError](responseBody)

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