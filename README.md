README
======

This library provides a scala interface to the [Postmark](http://postmarkapp.com) API.

Inspiration has been taken from https://github.com/jaredholdcroft/postmark-java, so thanks to Jared for that.

Hopefully the JARs will be up on maven central soon, but see releases for now.

Usage
-----

The PostmarkClient is simple enough to use:

```scala
import com.postmark.scala.Attachment
import com.postmark.scala.NameValueMap
import com.postmark.scala.PostmarkClient
import com.postmark.scala.PostmarkMessage

import java.io.File

import org.apache.commons.codec.binary.Base64

val client = new PostmarkClient("POSTMARK_API_TEST")

val message = PostmarkMessage(

  // Required fields
  To = "Recipient <receipient@domain.com>",
  From = "Postmark Sender <sender@domain.com>",
  Subject = "Test E-Mail",
  HtmlBody = "<p>Hello world</p>",

  // Optional mail fields
  Cc = Some("Another Recipient <another.recipient@domain.com>"),
  Bcc = Some("secret.recipient@domain.com"),
  ReplyTo = Some("reply@domain.com"),

  // Optional attachments
  Attachments = List(
    Attachment("Text File.txt", "text/plain", Base64.encodeBase64String("Hello world".getBytes)),
    Attachment(new java.io.File("picture.jpg"))
  ),

  // Optional Postmark fields
  Tag = Some("My Tag"),
  Headers = List(
    NameValueMap("key", "value"),
    NameValueMap("key2", "value2")
  )

)

val result = client.send(message)

client.destroy
```

Alternatively, you can use the `PostmarkAutoRetryClient` to automatically retry when there's an error at Postmark's end.
Note that the `send` method will block until either success or eventual failure.

All optional fields default to None/Nil.

Some additional points:

* You need to add your own flavour of SLF4J implementation as a dependency.
* If you're finished using the Postmark client, call the `destroy` method before dereferencing it.

Postmark Responses
------------------

The client expects the following HTTP codes from Postmark: 200, 401, 422 and 500.

* For 200, the provided JSON is parsed into `PostmarkSuccess`.
* For 401/422, the provided JSON is parsed into `PostmarkError`.
* For 500, a `PostmarkError` is created using the response body.

Note that when using the `PostmarkAutoRetryClient`, responses with 401/422 are accepted as valid, thus the request will not be re-attempted.
Only responses with 500 are deemed worth of a retry. If this isn't suitable for you, you can easily extend PostmarkClient with your own approach.
