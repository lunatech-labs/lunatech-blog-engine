name := """lunatech-blog-engine"""
organization := "com.lunatech.blog"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.11"

libraryDependencies ++= Seq(
  guice, ws, ehcache,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  "org.asciidoctor" % "asciidoctorj" % "2.5.10",
  "com.47deg" %% "github4s" % "0.32.0",
  "com.typesafe.play" %% "play-json" % "2.9.4",
)

Global / onChangedBuildSource := ReloadOnSourceChanges
