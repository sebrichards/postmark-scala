name := "postmark-scala"

description := "Scala binding for the Postmark API"

version := "1.1.2"

organization := "com.github.sebrichards"

scalaVersion := "2.11.0"

crossScalaVersions := Seq("2.10.0", "2.11.0")

libraryDependencies ++= Seq(
  // Commons
  "org.apache.httpcomponents" % "httpclient" % "4.2.5",
  "commons-io" % "commons-io" % "2.2",
  // JSON
  "org.json4s" %% "json4s-core" % "3.2.9",
  "org.json4s" %% "json4s-jackson" % "3.2.9",
  // Joda
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.4",
  // Logging
  "org.slf4j" % "slf4j-api" % "1.7.5",
  // Testing
  "org.specs2" %% "specs2" % "2.3.11" % "test"
)

publishMavenStyle := true

publishArtifact in Test := false

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
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
