package com.github.sebrichards.postmark

import org.apache.http.HttpResponse
import org.apache.http.client.ServiceUnavailableRetryStrategy
import org.apache.http.impl.client.AutoRetryHttpClient
import org.apache.http.protocol.HttpContext

/**
 * A client for Postmark, with automatic retry.
 * 
 * @param serverToken The API key
 * @param retryInterval Milliseconds between each retry
 * @param maxRetries Number of retries to make
 */
class PostmarkAutoRetryClient(
    serverToken: String,
    retryInterval: Long,
    maxRetries: Int)
  extends PostmarkClient(serverToken) {

  // Define a retry strategy
  private val retryStrategy = new ServiceUnavailableRetryStrategy {

    def retryRequest(res: HttpResponse, executionCount: Int, ctx: HttpContext): Boolean = {

      val validResponse = res.getStatusLine.getStatusCode match {
        // Accept 200, 401, 422
        case 200 | 401 | 422 => true
        case _ => false
      } 

      val retry = !validResponse && executionCount <= maxRetries

      if (retry)
        logger.warn("Attempt failed " + executionCount + " time(s), will retry in " + retryInterval + "ms.")

      retry
    }

    def getRetryInterval: Long = retryInterval

  }

  // Override client
  override protected val client = new AutoRetryHttpClient(retryStrategy)

}