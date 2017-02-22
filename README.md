# postmark-scala

This library provides a scala interface to the [Postmark](http://postmarkapp.com) API.

Inspiration has been taken from https://github.com/jaredholdcroft/postmark-java, so thanks to Jared for that.

Please note that support for Scala 2.9.x was dropped at version 1.1.1.

## Usage

Add the dependency to SBT:

```scala
libraryDependencies += "com.github.sebrichards" %% "postmark-scala" % "1.3"
```

The PostmarkClient is simple enough to use:

```scala
import com.github.sebrichards.postmark.Attachment
import com.github.sebrichards.postmark.NameValueMap
import com.github.sebrichards.postmark.PostmarkClient
import com.github.sebrichards.postmark.PostmarkError
import com.github.sebrichards.postmark.PostmarkMessage
import com.github.sebrichards.postmark.PostmarkSuccess

import java.io.File

import org.apache.commons.codec.binary.Base64

val client = new PostmarkClient("POSTMARK_API_TEST")

val message = PostmarkMessage(

  // Required fields
  To = "Recipient <receipient@domain.com>",
  From = "Postmark Sender <sender@domain.com>",
  Subject = "Test E-Mail",
  TextBody = Some("Hello world"),
  HtmlBody = Some("<p>Hello world</p>"),

  // Optional mail fields
  Cc = Some("Another Recipient <another.recipient@domain.com>"),
  Bcc = Some("secret.recipient@domain.com"),
  ReplyTo = Some("reply@domain.com"),

  // Optional attachments
  Attachments = List(
    Attachment("Text File.txt", "text/plain", Base64.encodeBase64String("Hello world".getBytes)),
    Attachment(new File("picture.jpg"))
  ),

  // Optional Postmark fields
  Tag = Some("My Tag"),
  Headers = List(
    NameValueMap("key", "value"),
    NameValueMap("key2", "value2")
  ),
  TrackOpens = true

)

val result: Either[PostmarkError, PostmarkSuccess] = client.send(message)

client.destroy
```

Alternatively, you can use the `PostmarkAutoRetryClient` to automatically retry when there's an error at Postmark's end.
Note that the `send` method will block until either success or eventual failure.

All optional fields default to None/Nil.

Some additional points:

* You need to add your own flavour of SLF4J implementation as a dependency.
* If you're finished using the Postmark client, call the `destroy` method before dereferencing it.

## Postmark Responses

The client expects the following HTTP codes from Postmark: 200, 401, 422 and 500.

* For 200, the provided JSON is parsed into `PostmarkSuccess`.
* For 401/422, the provided JSON is parsed into `PostmarkError`.
* For 500, a `PostmarkError` is created using the response body.

Note that when using the `PostmarkAutoRetryClient`, responses with 401/422 are accepted as valid, thus the request will not be re-attempted.
Only responses with 500 are deemed worthy of a retry. If this isn't suitable for you, you can easily extend PostmarkClient with your own approach.
