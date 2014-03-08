package com.github.sebrichards.postmark.test

import org.specs2.mutable._

import com.github.sebrichards.postmark._

import org.apache.commons.codec.binary.Base64

class PostmarkTest extends Specification {

  val validMessage = PostmarkMessage(
    To = "Recipient <receipient@domain.com>",
    From = "Postmark Sender <test@domain.com>",
    Subject = "Test E-Mail",
    HtmlBody = Some("<p>Hello world</p>")
  )

  val validTextMessage = PostmarkMessage(
    To = "Recipient <receipient@domain.com>",
    From = "Postmark Sender <test@domain.com>",
    Subject = "Test E-Mail",
    TextBody = Some("Hello world")
  )

  val validMultipartMessage = PostmarkMessage(
    To = "Recipient <receipient@domain.com>",
    From = "Postmark Sender <test@domain.com>",
    Subject = "Test E-Mail",
    TextBody = Some("Hello world"),
    HtmlBody = Some("<p>Hello world</p>")
  )

  val invalidMessage = PostmarkMessage(
    To = "invalid",
    From = "invalid",
    Subject = "Test E-Mail",
    HtmlBody = Some("<p>Hello World</p>")
  )

  "An incorrect API key" should {
    "return error code 10" in {
      val client = new PostmarkClient("INVALID_API_KEY")
      val response = client.send(validMessage)
      client.destroy

      response.left.map(_.ErrorCode) must beLeft(10)
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

  "Valid text-only setup" should {
    "succeed" in {
      val client = new PostmarkClient("POSTMARK_API_TEST")
      val response = client.send(validTextMessage)
      client.destroy

      response must beRight
    }
  }

  "Valid multipart setup" should {
    "succeed" in {
      val client = new PostmarkClient("POSTMARK_API_TEST")
      val response = client.send(validMultipartMessage)
      client.destroy

      response must beRight
    }
  }
}
