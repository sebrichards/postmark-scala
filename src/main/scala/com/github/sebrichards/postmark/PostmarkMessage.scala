package com.github.sebrichards.postmark

/**
 * A Postmark e-mail.
 *
 * E-mail addresses can take one of the following formats:
 *   - name@domain.com
 *   - <Name> name@domain.com
 */
case class PostmarkMessage (

  // Core
  From: String,
  To: String,
  Subject: String,
  HtmlBody: Option[String] = None,
  TextBody: Option[String] = None,

  // Mail extras
  Cc: Option[String] = None,
  Bcc: Option[String] = None,
  ReplyTo: Option[String] = None,
  Attachments: List[Attachment] = Nil,

  // Special
  Tag: Option[String] = None,
  Headers: List[NameValueMap] = Nil,
  TrackOpens: Boolean = false

)
