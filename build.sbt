// ------------------------ Project info ------------------------

name := "postmark-scala"

description := "Scala binding for the Postmark API"

version := "1.3"

organization := "com.github.sebrichards"

// ------------------------ Compiler settings ------------------------

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.1")

scalacOptions ++= Seq("-feature", "-deprecation")

// ------------------------ Dependencies ------------------------

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(

  // Commons
  "org.apache.httpcomponents" % "httpclient" % "4.5.3",
  "commons-io" % "commons-io" % "2.5",

  // JSON
  "org.json4s" %% "json4s-native" % "3.5.0",

  // Joda
  "joda-time" % "joda-time" % "2.9.7",
  "org.joda" % "joda-convert" % "1.8.1",

  // Logging
  "org.slf4j" % "slf4j-api" % "1.7.23",

  // Testing
  "org.specs2" %% "specs2-core" % "3.8.8" % "test",
  "org.slf4j" % "slf4j-nop" % "1.7.23" % "test")

// ------------------------ Publishing ------------------------

publishMavenStyle := true

publishArtifact in Test := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

licenses := Seq("MIT" -> url("http://seb.mit-license.org/license.txt"))

homepage := Some(url("https://github.com/sebrichards/postmark-scala"))

pomExtra := (
  <scm>
    <url>https://github.com/sebrichards/postmark-scala</url>
    <connection>scm:git:git://github.com/sebrichards/postmark-scala.git</connection>
  </scm>
  <developers>
    <developer>
      <id>sebrichards</id>
      <name>Seb Richards</name>
      <url>https://github.com/sebrichards</url>
    </developer>
  </developers>
)
