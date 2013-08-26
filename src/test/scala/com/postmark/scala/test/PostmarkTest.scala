package com.postmark.scala.test

import org.specs2.mutable._

import com.postmark.scala._

import org.apache.commons.codec.binary.Base64

class PostmarkTest extends Specification {

  val validMessage = PostmarkMessage(
    To = "Recipient <receipient@domain.com>",
    From = "Postmark Sender <test@domain.com>",
    Subject = "Test E-Mail",
    HtmlBody = "<p>Hello world</p>"
  )

  val invalidMessage = PostmarkMessage(
    To = "invalid",
    From = "invalid",
    Subject = "Test E-Mail",
    HtmlBody = "<p>Hello World</p>"
  )

  "An incorrect API key" should {
    "return error code 0" in {
      val client = new PostmarkClient("INVALID_API_KEY")
      val response = client.send(validMessage)
      client.destroy

      response.left.map(_.ErrorCode) must beLeft(0)
    }
  }

  "An invalid e-mail address" should {
    "return error code 300" in {
      val client = new PostmarkClient("POSTMARK_API_TEST")
      val response = client.send(invalidMessage)
      client.destroy

      response.left.map(_.ErrorCode) must beLeft(300)
    }
  }

  "Valid setup" should {
    "succeed" in {
      val client = new PostmarkClient("POSTMARK_API_TEST")
      val response = client.send(validMessage)
      client.destroy

      response must beRight
    }
  }

}