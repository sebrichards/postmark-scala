// ------------------------ Project info ------------------------

name := "postmark-scala"

description := "Scala binding for the Postmark API"

version := "1.2"

organization := "com.github.sebrichards"

// ------------------------ Compiler settings ------------------------

scalaVersion := "2.11.0"

crossScalaVersions := Seq("2.10.0", "2.11.0")

scalacOptions ++= Seq("-feature", "-deprecation")

// ------------------------ Dependencies ------------------------

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(

  // Commons
  "org.apache.httpcomponents" % "httpclient" % "4.5",
  "commons-io" % "commons-io" % "2.4",

  // JSON
  "org.json4s" %% "json4s-native" % "3.2.11",

  // Joda
  "joda-time" % "joda-time" % "2.8",
  "org.joda" % "joda-convert" % "1.7",

  // Logging
  "org.slf4j" % "slf4j-api" % "1.7.12",

  // Testing
  "org.specs2" %% "specs2" % "2.4.17" % "test")

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
