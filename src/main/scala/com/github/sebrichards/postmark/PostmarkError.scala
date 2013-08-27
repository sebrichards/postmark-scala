package com.github.sebrichards.postmark

/** Details of a failed e-mail attempt */
case class PostmarkError (
  ErrorCode: Int,
  Message: String
)