package com.github.sebrichards.postmark.util

import org.json4s._

import org.joda.time.DateTime

/**
 * Custom serializer for Joda's DateTime.
 *
 * Although json4s-ext provides Joda mapping, there are some issues:
 *   - Java's Date is used then converted to Joda
 *   - Postmark includes a timezone offset in their date format
 *   - Java's Date doesn't properly support ISO8601 before Java 7
 */
object DateTimeSerializer extends CustomSerializer[DateTime](format => (
  {
    case JString(s) => new DateTime(s)
    case JNull => null
  },
  {
    case d: DateTime => JString(d.toString())
  }
))