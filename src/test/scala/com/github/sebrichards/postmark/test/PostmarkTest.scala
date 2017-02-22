package com.github.sebrichards.postmark.test

import org.specs2.mutable._

import com.github.sebrichards.postmark._

import org.apache.commons.codec.binary.Base64

class PostmarkTest extends Specification {

  val validBase = PostmarkMessage(
    To = "Recipient <receipient@domain.com>",
    From = "Postmark Sender <test@domain.com>",
    Subject = "Test E-Mail")

  val validHtmlMessage = validBase.copy(
    HtmlBody = Some("<p>Hello world</p>"))

  val validTextMessage = validBase.copy(
    TextBody = Some("Hello world"))

  val validMultipartMessage = validBase.copy(
    TextBody = Some("Hello world"),
    HtmlBody = Some("<p>Hello world</p>"))

  val invalidEmailAddress = PostmarkMessage(
    To = "invalid",
    From = "invalid",
    Subject = "Test E-Mail",
    HtmlBody = Some("<p>Hello World</p>"))

  "An incorrect API key" should {
    "return error code 10" in {
      val client = new PostmarkClient("INVALID_API_KEY")
      val response = client.send(validMultipartMessage)
      client.destroy

      response.left.map(_.ErrorCode) must beLeft(10)
    }
  }

  "A HTML-only message" should {
    "succeed" in {
      val client = new PostmarkClient("POSTMARK_API_TEST")
      val response = client.send(validHtmlMessage)
      client.destroy

      response must beRight
    }
  }

  "A text-only message" should {
    "succeed" in {
      val client = new PostmarkClient("POSTMARK_API_TEST")
      val response = client.send(validTextMessage)
      client.destroy

      response must beRight
    }
  }

  "A multipart message" should {
    "succeed" in {
      val client = new PostmarkClient("POSTMARK_API_TEST")
      val response = client.send(validMultipartMessage)
      client.destroy

      response must beRight
    }
  }

  "An invalid e-mail address" should {
    "return error code 300" in {
      val client = new PostmarkClient("POSTMARK_API_TEST")
      val response = client.send(invalidEmailAddress)
      client.destroy

      response.left.map(_.ErrorCode) must beLeft(300)
    }
  }

  "A missing body part" should {
    "return error code 300" in {
      val client = new PostmarkClient("POSTMARK_API_TEST")
      val response = client.send(validBase)
      client.destroy

      response.left.map(_.ErrorCode) must beLeft(300)
    }
  }
}
